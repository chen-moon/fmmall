package com.qfedu.fmmall.service;

import com.qfedu.fmmall.entity.Users;
import com.qfedu.fmmall.vo.ResultVO;


//第四步    在mapper文件写完后，进行service服务层接口创建
//第五步    之后去service实现类中对服务进行完善



/**
 * @author chen
 * @date 2021/8/6-9:03
 */
public interface UserService {

    //用户注册
    public ResultVO userRegist(String username,String password);

    //用户登录
    public ResultVO checkLogin(String username, String password);

    //根据用户id查询用户信息
    public ResultVO quaryUserInfo(int userId);

    //修改用户信息
    public ResultVO modifyUserInfo(Users user);

}
