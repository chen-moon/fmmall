package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.ShoppingCart;
import com.qfedu.fmmall.entity.ShoppingCartVO;
import com.qfedu.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartMapper extends GeneralDAO<ShoppingCart> {

    public List<ShoppingCartVO> selectShopcartByUserId(int userId);

    public int updateCartNumByCartIdInt(@Param("cid") int cid,
                                        @Param("cnum") int cnum);

    public List<ShoppingCartVO> selectShopCartByCids(List<Integer> cids);

}