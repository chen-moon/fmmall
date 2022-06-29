package com.qfedu.fmmall.service;

import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @date 2021/11/3-19:47
 * @Description:
 */
@Repository
public interface ProductCommontsService {

    /**
     * 根据商品id查询分页评论信息
     * @param productId --商品id
     * @param pageNum   --页码
     * @param limit     --一个页显示的条数
     * @return          --分页评论信息
     */
    public ResultVO listCommontsByProductId(String productId,int pageNum,int limit);

    public ResultVO getCommentsCountByProductId(String productId);

}
