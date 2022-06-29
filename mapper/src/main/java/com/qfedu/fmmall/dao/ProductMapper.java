package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.Product;
import com.qfedu.fmmall.entity.ProductVO;
import com.qfedu.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper extends GeneralDAO<Product> {
    public List<ProductVO> selectRecommendProducts();

    public List<ProductVO> selectTop6ByCategory(int cid);

    public List<ProductVO> selectProductByCaegoryId(@Param("cid") int cid,
                                                  @Param("start") int start,
                                                  @Param("limit") int limit);

    public List<String> selectBrandByCategoryId(int cid);

    //模糊查询
    public List<ProductVO> selectProductByKeyWord(@Param("kw") String keyword,
                                                  @Param("start") int start,
                                                  @Param("limit") int limit);
    //根据关键字查询相关商品的品牌列表
    public List<String> selectBrandByKeyWord(String kw);
}