package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.ProductImgMapper;
import com.qfedu.fmmall.dao.ProductMapper;
import com.qfedu.fmmall.dao.ProductParamsMapper;
import com.qfedu.fmmall.dao.ProductSkuMapper;
import com.qfedu.fmmall.entity.*;
import com.qfedu.fmmall.service.ProductService;
import com.qfedu.fmmall.utils.PageHelper;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 * @date 2021/8/24-16:37
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImgMapper productImgMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductParamsMapper productParamsMapper;

    @Override
    public ResultVO listRecommendProducts() {
        List<ProductVO> productVOS = productMapper.selectRecommendProducts();
        ResultVO resultVO = new ResultVO(ResStatus.OK, "success", productVOS);
        return resultVO;
    }

    @Override
    public ResultVO getProductBasicInfo(String productId) {

        //1.根据前端传来的商品Id查找对应的商品(只有一个)
        //使用Example创建查找条件
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        //根据商品id
        criteria.andEqualTo("productId",productId);
        //确定商品id的前提下，还必须确保商品的状态是上架的
        criteria.andEqualTo("productStatus",1);

        //查询
        List<Product> product = productMapper.selectByExample(example);
        //进行判断，如果没有对应商品，返回失败信息，如果存在商品，返回对应的商品详情
        if (product.size()>0) {
            //2.根据商品id查找对应的商品图片
            Example example1 = new Example(ProductImg.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("itemId",productId);

            List<ProductImg> productImgs = productImgMapper.selectByExample(example1);

            //3.根据商品id查找对应的商品套餐sku
            Example example2 = new Example(ProductSku.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("productId",productId);
            criteria2.andEqualTo("status",1);

            List<ProductSku> productSkus = productSkuMapper.selectByExample(example2);

            //得到三个商品信息后，使用HashMap进行封装
            Map<String, Object> basicInfo = new HashMap<>();

            basicInfo.put("product",product.get(0));
            basicInfo.put("productImgs",productImgs);
            basicInfo.put("productSkus",productSkus);

            return new ResultVO(ResStatus.OK,"success",basicInfo);
        }else {
            return new ResultVO(ResStatus.NO,"商品不存在!",null);
        }
    }

    @Override
    public ResultVO getProductParamsById(String productId) {

        Example example = new Example(ProductParams.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);

        List<ProductParams> productParams = productParamsMapper.selectByExample(example);
        if (productParams.size()>0){
            return new ResultVO(ResStatus.OK,"success",productParams.get(0));
        }else {
            return new ResultVO(ResStatus.NO,"该产品可能为三无产品",null);
        }
    }

    @Override
    public ResultVO getProductsByCategoryId(int categoryId, int pageNum, int limit) {
        //查询分页数据
        int start = (pageNum-1) * limit;
        List<ProductVO> productVOList = productMapper.selectProductByCaegoryId(categoryId, start, limit);
        //查询当前类别下的商品总记录数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        int count = productMapper.selectCountByExample(example);
        //计算总页数
        int pageCount = count%limit==0?count/limit:count/limit+1;

        PageHelper<ProductVO> pageHelper = new PageHelper<>(count,pageCount,productVOList);

        return new ResultVO(ResStatus.OK,"SUCCESS",pageHelper);
    }

    @Override
    public ResultVO listBrands(int cid) {
        List<String> brands = productMapper.selectBrandByCategoryId(cid);
        return new ResultVO(ResStatus.OK,"success",brands);
    }

    @Override
    public ResultVO searchProduct(String kw, int pageNum, int limit) {
        //查询搜索结果
        kw = "%"+kw+"%";
        int start = (pageNum -1 )*limit;
        List<ProductVO> productVOS = productMapper.selectProductByKeyWord(kw, start, limit);

        //查询总记录数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("productName",kw);
        int count = productMapper.selectCountByExample(example);
        //计算总记录数
        int pageCount = count%limit==0?count/limit:count/limit+1;

        PageHelper<ProductVO> pageHelper = new PageHelper<>(count,pageCount,productVOS);
        return new ResultVO(ResStatus.OK,"success",pageHelper);
    }

    @Override
    public ResultVO listBrands(String kw) {
        kw = "%"+kw+"%";
        List<String> brands = productMapper.selectBrandByKeyWord(kw);
        return new ResultVO(ResStatus.OK,"success",brands);
    }
}
