package com.qfedu.fmmall.controller;

import com.qfedu.fmmall.entity.Users;
import com.qfedu.fmmall.service.UserService;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chen
 * @date 2021/8/6-9:25
 */
@RestController
@RequestMapping("/user")
@Api(value = "提供用户登录和注册接口",tags = "用户管理" )
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "String",name = "password",value = "用户登录密码",required = true)
    })
    @GetMapping("/login")
    public ResultVO login(@RequestParam("username") String username,@RequestParam(value = "password") String password){
        ResultVO resultVO = userService.checkLogin(username, password);
        return resultVO;
    }


    @ApiOperation(value = "用户注册接口")
    @PostMapping("/regist")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "username",value = "用户注册账号",required = true),
            @ApiImplicitParam(dataType = "String",name = "password",value = "用户注册密码",required = true)
    })
    public ResultVO regist(@RequestBody Users user){
        ResultVO resultVO = userService.userRegist(user.getUsername(), user.getPassword());
        return resultVO;
    }

    @ApiOperation(value = "校验token是否过期接口")
    @GetMapping("/check")
    public ResultVO userTokenCheck(@RequestHeader("token") String token){
        return new ResultVO(ResStatus.OK,"success",null);
    }

    @ApiOperation(value = "根据用户Id查询用户信息")
    @GetMapping("/quaryById")
    public ResultVO quaryUserInfoById(@RequestHeader("token") String token,int userId){
        return userService.quaryUserInfo(userId);
    }

    @ApiOperation(value = "根据用户Id修改用户信息")
    @PostMapping("/modifyById")
    public ResultVO modifyUserInfoById(@RequestHeader("token") String token, @RequestBody Users user){
        return userService.modifyUserInfo(user);
    }

}
