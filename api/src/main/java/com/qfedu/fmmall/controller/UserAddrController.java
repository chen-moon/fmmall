package com.qfedu.fmmall.controller;

import com.qfedu.fmmall.entity.UserAddr;
import com.qfedu.fmmall.entity.Users;
import com.qfedu.fmmall.service.UserAddrService;
import com.qfedu.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chen
 * @date 2021/11/12-15:14
 * @Description:
 */

@RestController
@CrossOrigin
@Api(value = "提供收货地址相关接口",tags = "收货地址管理")
@RequestMapping("/useraddr")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @ApiOperation(value = "根据地址Id获取用户地址信息")
    @GetMapping("/list")
    @ApiImplicitParam(dataType = "int",name = "userId",value = "用户ID",required = true)
    public ResultVO listAddr(Integer userId, @RequestHeader("token") String token){
        return userAddrService.listUserAddrsByUid(userId);
    }

    @ApiOperation(value = "根据地址Id修改地址信息")
    @PostMapping("/modifyAddr")
    public ResultVO modifyUserInfoById(@RequestHeader("token") String token, @RequestBody UserAddr userAddr){
        return userAddrService.modifiedAddrs(userAddr);
    }

    @ApiOperation(value = "添加地址信息")
    @PostMapping("/addAddrs")
    public ResultVO addAddrs(@RequestHeader("token") String token, @RequestBody UserAddr userAddr){
        System.out.println(userAddr);
        return userAddrService.addAddrs(userAddr);
    }

    @ApiOperation(value = "查询所有地址信息")
    @GetMapping("/listAll")
    public ResultVO listAddr(@RequestHeader("token") String token){
        return userAddrService.getTopAddrId();
    }


}
