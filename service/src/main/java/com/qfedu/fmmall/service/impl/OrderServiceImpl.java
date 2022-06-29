package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.OrderItemMapper;
import com.qfedu.fmmall.dao.OrdersMapper;
import com.qfedu.fmmall.dao.ProductSkuMapper;
import com.qfedu.fmmall.dao.ShoppingCartMapper;
import com.qfedu.fmmall.entity.*;
import com.qfedu.fmmall.service.OrderService;
import com.qfedu.fmmall.utils.PageHelper;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * @author chen
 * @date 2022/3/1-19:52
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Transactional
    @Override
    public Map<String, String> addOrder(String cids, Orders order) throws SQLException {
        HashMap<String, String> map = new HashMap<>();
        //1.根据cids查询当前订单中关联的购物车记录详情（包括库存）
        //按“,"切割cids，拿出订单中购物车的信息主键
        String[] arr = cids.split(",");
        List<Integer> cidsList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            cidsList.add(Integer.parseInt(arr[i]));
        }
        List<ShoppingCartVO> list = shoppingCartMapper.selectShopCartByCids(cidsList);

        //1.校验库存
        boolean flag = true;
        String untitled = "";
        for (ShoppingCartVO sc : list) {
            if (Integer.parseInt(sc.getCartNum()) > sc.getSkuStock()) {
                flag = false;
            }
            //获取所有商品的名称，用”，“隔开
            untitled += sc.getProductName() + ",";
        }

        //2.保存订单
        if (flag) {
            //库存充足
            order.setUntitled(untitled);
            order.setCreateTime(new Date());
            //生成订单编号
            String orderId = UUID.randomUUID().toString().replace("-", "");
            order.setOrderId(orderId);
            order.setStatus("1");
            int i = ordersMapper.insert(order);

            //3.生成商品快照
            if (i > 0) {
                for (ShoppingCartVO sc : list) {
                    String itemId = System.currentTimeMillis() + "" + (new Random().nextInt(89999) + 10000);
                    int cnum = Integer.parseInt(sc.getCartNum());
                    OrderItem orderItem = new OrderItem(itemId, orderId, sc.getProductId(), sc.getProductName(), sc.getProductImg(), sc.getSkuId(), sc.getSkuName(), BigDecimal.valueOf(sc.getSellPrice()), Integer.parseInt(sc.getCartNum()), BigDecimal.valueOf(cnum * sc.getSellPrice()), new Date(), new Date(), 0);
                    int j = orderItemMapper.insert(orderItem);
                }

                //4.删减库存
                for (ShoppingCartVO sc : list) {
                    String skuId = sc.getSkuId();
                    //扣减后的库存 = 原来的库存 - 购买数量
                    int newStock = sc.getSkuStock() - Integer.parseInt(sc.getCartNum());

                    ProductSku productSku = new ProductSku();
                    productSku.setSkuId(skuId);
                    productSku.setStock(newStock);
                    int k = productSkuMapper.updateByPrimaryKeySelective(productSku);
                }
            }

            //5.删除购物车记录--当购物车中购买成功后，购物车中删除对应的商品
            for (Integer cid : cidsList) {
                int m = shoppingCartMapper.deleteByPrimaryKey(cid);
            }

            map.put("orderId", orderId);
            map.put("productName", untitled);

            return map;
        } else {
            //库存不足
            return null;
        }
    }

    @Override
    public int updateOrderStatus(String orderId, String status) {
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setStatus(status);
        int i = ordersMapper.updateByPrimaryKeySelective(order);
        return i;
    }

    @Override
    public ResultVO getOrderById(String orderId) {
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        return new ResultVO(ResStatus.OK, "success", order.getStatus());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void closeOrder(String orderId) {
        synchronized (this) {
            //如果取消订单则将状态改为6
            Orders updateOrders = new Orders();
            updateOrders.setOrderId(orderId);
            updateOrders.setStatus("6");//已关闭
            updateOrders.setCloseType(1);
            ordersMapper.updateByPrimaryKeySelective(updateOrders);

            Example example1 = new Example(OrderItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orderId", orderId);
            List<OrderItem> orderItems = orderItemMapper.selectByExample(example1);
            //还原库存
            for (int j = 0; j < orderItems.size(); j++) {
                OrderItem orderItem = orderItems.get(j);
                ProductSku productSku = productSkuMapper.selectByPrimaryKey(orderItem.getSkuId());
                productSku.setSkuId(orderItem.getSkuId());
                productSku.setStock(productSku.getStock() + orderItem.getBuyCounts());
                productSkuMapper.updateByPrimaryKeySelective(productSku);
            }
        }
    }

    @Override
    public ResultVO listOrders(String userId, String status, int pageNum, int limit) {
        //分页查询
        int start = (pageNum - 1) * limit;
        List<OrdersVO> ordersVOS = ordersMapper.selectOrders(userId, status, start, limit);

        //查询总记录数
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userId", userId);
        if (status != null && !"".equals(status)) {
            criteria.andLike("status", status);
        }
        int count = ordersMapper.selectCountByExample(example);

        int pageCount = count % limit == 0 ? count / limit : count / limit + 1;

        PageHelper<OrdersVO> pageHelper = new PageHelper<>(count, pageCount, ordersVOS);

        return new ResultVO(ResStatus.OK, "success", pageHelper);
    }

    @Override
    public ResultVO getOrdersByOid(String orderId) {

        return null;
    }


}
