package com.qfedu.fmmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author chen
 * @date 2021/8/19-15:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryVO {
    private Integer categoryId;
    private String categoryName;
    private Integer categoryLevel;
    private Integer parentId;
    private String categoryIcon;
    private String categorySlogan;
    private String categoryPic;
    private String categoryBgColor;

    //首页商品类别
    private List<CategoryVO> categories;

    //首页分类商品类别推荐
    private List<ProductVO> products;

}
