package com.qfedu.fmmall.controller;

import com.qfedu.fmmall.service.CategoryService;
import com.qfedu.fmmall.service.IndexImgService;
import com.qfedu.fmmall.service.ProductService;
import com.qfedu.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chen
 * @date 2021/8/18-15:21
 */
@RestController
@CrossOrigin
@RequestMapping("/index")
@Api(value = "提供首页数据显示所需的接口",tags = "首页管理")
public class IndexController {

    @Autowired
    private IndexImgService indexImgService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/indeximg")
    @ApiOperation("首页轮播图接口")
    public ResultVO listIndexImgs(){
        return indexImgService.listIndexImgs();
    }

    @GetMapping("/category")
    @ApiOperation("商品分类查询接口")
    public ResultVO listCategoryInfo(){
        return categoryService.listCategories();
    }

    @GetMapping("/list-recommend")
    @ApiOperation("首页商品推荐查询")
    public ResultVO listRecommend(){
        ResultVO resultVO = productService.listRecommendProducts();
        return resultVO;
    }

    @GetMapping("/list-top6")
    @ApiOperation("首页商品分类推荐查询")
    public ResultVO listFirstCategories(){
        ResultVO resultVO = categoryService.listFirstLevelCategories();
        return resultVO;
    }
}
