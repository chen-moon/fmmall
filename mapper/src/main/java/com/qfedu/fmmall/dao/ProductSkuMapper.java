package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.ProductSku;
import com.qfedu.fmmall.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSkuMapper extends GeneralDAO<ProductSku> {

    /**
     * 查询当前商品，所有商品参数中最低的商品参数
     * @param productId
     * @return
     */
    public List<ProductSku> selectLowerestPriceByProductId(String productId);

}