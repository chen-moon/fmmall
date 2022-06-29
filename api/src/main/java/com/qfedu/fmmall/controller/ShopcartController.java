package com.qfedu.fmmall.controller;

import com.qfedu.fmmall.dao.ShoppingCartMapper;
import com.qfedu.fmmall.entity.ShoppingCart;
import com.qfedu.fmmall.service.ShoppingCartService;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chen
 * @date 2021/8/13-14:43
 */
@RestController
@CrossOrigin
@RequestMapping("/shopcart")
@Api(value = "提供购物车业务相关接口",tags = "购物车管理" )
public class ShopcartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation(value = "添加购物车接口")
    public ResultVO addShoppingCart(@RequestBody ShoppingCart cart,@RequestHeader("token") String token) {
        return shoppingCartService.addShoppingCart(cart);
    }

    @GetMapping("/list")
    @ApiOperation(value = "购物车列表接口")
    public ResultVO list(Integer userId,@RequestHeader String token){
        System.out.println(token);
        return shoppingCartService.listShoppingCartsByUserId(userId);
    }

    @PutMapping("/update/{cid}/{cnum}")
    @ApiOperation(value = "购物车数量修改接口")
    public ResultVO updateNum(@PathVariable("cid") Integer cid,@PathVariable("cnum") Integer cnum,@RequestHeader String token){
        return shoppingCartService.updateShoppingCart(cid,cnum);
    }

    @GetMapping("/listbycids")
    @ApiImplicitParam(dataType = "String",name = "cids",value = "商品订单id",required = true)
    @ApiOperation(value = "根据订单id查询接口")
    public ResultVO listByCids(String cids,@RequestHeader("token") String token){
        return shoppingCartService.listShoppingCartsByCids(cids);
    }

    @DeleteMapping("/del")
    @ApiOperation(value = "删除购物车接口")
    public ResultVO deleteShopCartById(@RequestHeader("token") String token,int cartId) {
        return shoppingCartService.deleteShopCartById(cartId);
    }

}
