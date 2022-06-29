package com.qfedu.fmmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShoppingCartVO {
    private Integer cartId;
    private String productId;
    private String skuId;
    private String skuProps;
    private String userId;
    private String cartNum;
    private String cartTime;
    private BigDecimal productPrice;

    //添加商品相关信息
    private String productName;
    private String productImg;
    private double originalPrice;
    private double sellPrice;
    private String skuName;
    private Integer skuStock;
}