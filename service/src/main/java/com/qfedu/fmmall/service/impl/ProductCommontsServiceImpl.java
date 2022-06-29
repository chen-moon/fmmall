package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.ProductCommentsMapper;
import com.qfedu.fmmall.entity.ProductComments;
import com.qfedu.fmmall.entity.ProductCommentsVO;
import com.qfedu.fmmall.service.ProductCommontsService;
import com.qfedu.fmmall.utils.PageHelper;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

/**
 * @author chen
 * @date 2021/11/3-19:49
 * @Description:
 */
@Service
public class ProductCommontsServiceImpl implements ProductCommontsService {

    @Autowired
    private ProductCommentsMapper productCommentsMapper;

    @Override
    public ResultVO listCommontsByProductId(String productId,int pageNum,int limit) {
//        List<ProductCommentsVO> productComments = productCommentsMapper.getCommentsByProductId(productId);

        //获取总记录数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);

        int count = productCommentsMapper.selectCountByExample(example);

        //根据前端传来的页的大小进行判断页码--limit==pageSize
        int pageCount = count%limit==0 ? count/limit:(count/limit)+1;

        //获取分页数据
        //计算分页的起始位置
        int start = (pageNum-1)*limit;
        List<ProductCommentsVO> comments = productCommentsMapper.getCommentsByProductId(productId, start, limit);

        return new ResultVO(ResStatus.OK,"success", new PageHelper<>(count, pageCount, comments));
    }

    @Override
    public ResultVO getCommentsCountByProductId(String productId) {
        Example example1 = new Example(ProductComments.class);
        Example.Criteria criteria1 = example1.createCriteria();

        //1.获取评论总数
        criteria1.andEqualTo("productId",productId);
        int total = productCommentsMapper.selectCountByExample(example1);

        //2.获取好评数量
        criteria1.andEqualTo("commType",1);
        int goodTotal = productCommentsMapper.selectCountByExample(example1);

        //3.获取中评数量
        Example example2 = new Example(ProductComments.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("productId",productId);
        criteria2.andEqualTo("commType",0);
        int midTotal = productCommentsMapper.selectCountByExample(example2);

        //4.获取差评数量
        Example example3 = new Example(ProductComments.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("productId",productId);
        criteria3.andEqualTo("commType",-1);
        int badTotal = productCommentsMapper.selectCountByExample(example3);

        //5.计算好评率
        double percent = Double.parseDouble(goodTotal+"")/Double.parseDouble(total+"");
        String percentVal = (percent+"").substring(0,(percent+"").lastIndexOf(".")+2);

        //6.封装返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("goodTotal",goodTotal);
        map.put("midTotal",midTotal);
        map.put("badTotal",badTotal);
        map.put("percentVal",percentVal);

        return new ResultVO(ResStatus.OK,"success",map);
    }
}
