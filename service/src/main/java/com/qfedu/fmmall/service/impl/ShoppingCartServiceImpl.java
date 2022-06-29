package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.ShoppingCartMapper;
import com.qfedu.fmmall.entity.ShoppingCart;
import com.qfedu.fmmall.entity.ShoppingCartVO;
import com.qfedu.fmmall.service.ShoppingCartService;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chen
 * @date 2021/11/6-10:23
 * @Description:
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss");

    @Override
    public ResultVO addShoppingCart(ShoppingCart cart) {
        cart.setCartTime(sdf.format(new Date()));
        int i = shoppingCartMapper.insert(cart);
        if (i>0){
            return new ResultVO(ResStatus.OK,"success",null);
        }else {
            return new ResultVO(ResStatus.NO,"fail",null);

        }
    }

    @Override
    public ResultVO listShoppingCartsByUserId(int userId) {
        List<ShoppingCartVO> shoppingCarts = shoppingCartMapper.selectShopcartByUserId(userId);
        return new ResultVO(ResStatus.OK,"success",shoppingCarts);
    }

    @Override
    public ResultVO updateShoppingCart(int cid, int cnum) {
        int i = shoppingCartMapper.updateCartNumByCartIdInt(cid, cnum);
        if (i>0){
            return new ResultVO(ResStatus.OK,"update success",null);
        }else {
            return new ResultVO(ResStatus.NO,"update fail",null);
        }
    }

    @Override
    public ResultVO listShoppingCartsByCids(String cids) {
        if (cids == null){
            return new ResultVO(ResStatus.NO,"fail",null);
        }
        String[] split = cids.split(",");
        List<Integer> cartIds = new ArrayList<>();
        for (String s : split) {
            cartIds.add(Integer.parseInt(s));
        }

        List<ShoppingCartVO> shoppingCartVOS = shoppingCartMapper.selectShopCartByCids(cartIds);
        return new ResultVO(ResStatus.OK,"success",shoppingCartVOS);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResultVO deleteShopCartById(int cartId) {
        Example example = new Example(ShoppingCart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cartId",cartId);
        int i = shoppingCartMapper.deleteByExample(example);
        ResultVO resultVO = null;
        if (i>0){
            resultVO = new ResultVO(ResStatus.OK,"success","删除成功");
        }else {
            resultVO = new ResultVO(ResStatus.NO,"fail","删除失败");
        }
        return resultVO;
    }
}
