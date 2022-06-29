package com.qfedu.fmmall.service.job;

import com.github.wxpay.sdk.WXPay;
import com.qfedu.fmmall.dao.OrderItemMapper;
import com.qfedu.fmmall.dao.OrdersMapper;
import com.qfedu.fmmall.dao.ProductSkuMapper;
import com.qfedu.fmmall.entity.OrderItem;
import com.qfedu.fmmall.entity.Orders;
import com.qfedu.fmmall.entity.ProductSku;
import com.qfedu.fmmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 * @date 2022/4/15-16:45
 * @Description:
 */
@Component
public class OrderTimeoutCheckJob {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderService orderService;

    private WXPay wxPay =  new WXPay(new MyPayConfig());

    //http://cron.qqe2.com
    @Scheduled(cron = "0/60 * * * * ?")
    public void checkAndClose(){
        try {
            System.out.println("===========循环");
            //查询超过30mins订单状态依然为待支付状态的订单
            Example example = new Example(Orders.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status","1");

            //使用最早时间和数据库中的时间进行比较
            Date date = new Date((System.currentTimeMillis()-30*60*1000));
            criteria.andLessThan("createTime",date);

            List<Orders> orders = ordersMapper.selectByExample(example);

            //访问微信平台接口，确认当前订单的最终支付状态
            for (int i = 0; i < orders.size(); i++) {
                Orders order = orders.get(i);
                HashMap<String, String> params = new HashMap<>();
                params.put("out_trade_no", order.getOrderId());
                Map<String, String> resp = wxPay.orderQuery(params);

                if ("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){
                    //如果订单已经支付，则将支付状态改为2
                    Orders updateOrders = new Orders();
                    updateOrders.setOrderId(order.getOrderId());
                    updateOrders.setStatus("2");
                    ordersMapper.updateByPrimaryKeySelective(updateOrders);
                }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){

                    Map<String,String> map = wxPay.closeOrder(params);
                    System.out.println(map);

                    //关闭订单
                    orderService.closeOrder(order.getOrderId());

                }

            }
        } catch (Exception e) {
                e.printStackTrace();
        }

    }

}
