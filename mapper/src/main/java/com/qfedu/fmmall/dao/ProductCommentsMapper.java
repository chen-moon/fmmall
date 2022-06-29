package com.qfedu.fmmall.dao;

import com.qfedu.fmmall.entity.ProductComments;
import com.qfedu.fmmall.entity.ProductCommentsVO;
import com.qfedu.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentsMapper extends GeneralDAO<ProductComments> {

    public List<ProductCommentsVO> getCommentsByProductId(@Param("productId") String productId,
                                                          @Param("start") int start,
                                                          @Param("limit") int limit);

}