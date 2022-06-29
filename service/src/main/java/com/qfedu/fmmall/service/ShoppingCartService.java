package com.qfedu.fmmall.service;

import com.qfedu.fmmall.entity.ShoppingCart;
import com.qfedu.fmmall.vo.ResultVO;

import java.util.List;

/**
 * @author chen
 * @date 2021/11/6-10:22
 * @Description:
 */
public interface ShoppingCartService {

    public ResultVO addShoppingCart(ShoppingCart cart);

    public ResultVO listShoppingCartsByUserId(int userId);

    public ResultVO updateShoppingCart(int cid,int cnum);

    public ResultVO listShoppingCartsByCids(String cids);

    public ResultVO deleteShopCartById(int cartId);

}
