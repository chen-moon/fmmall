package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.UsersMapper;
import com.qfedu.fmmall.entity.Users;
import com.qfedu.fmmall.service.UserService;
import com.qfedu.fmmall.utils.MD5Utils;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author chen
 * @date 2021/8/6-9:06
 */
//给service设置单例模式，实现加锁操作
@Scope("singleton")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;


    //用户注册
    @Override
    public ResultVO userRegist(String username, String password) {

        //先由用户名查找用户信息判断是否用户已经注册
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        List<Users> users = usersMapper.selectByExample(example);
//        Users user = users.get(0);

        //对注册方法进行加锁，避免两个用户同时注册然后使用相同的用户名
        synchronized (this) {
            //用户名不存在就进行用户注册
            if (users.size() == 0) {
                //对将要存入数据库的密码进行MD5加密操作
                String md5password = MD5Utils.md5(password);
                Users user = new Users();
                user.setUsername(username);
                user.setPassword(md5password);
                user.setUserImg("default.jpg");
                user.setUserRegtime(new Date());
                user.setUserModtime(new Date());

                usersMapper.insertUseGeneratedKeys(user);
                return new ResultVO(ResStatus.OK, "注册成功", user);
            } else {
                return new ResultVO(ResStatus.NO, "用户名已经存在", null);
            }
        }

    }


    //用户登录
    @Override
    public ResultVO checkLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        List<Users> users = usersMapper.selectByExample(example);
//        Users user = users.get(0);
        //判断用户名是否存在
        if (users.size() == 0) {
            return new ResultVO(ResStatus.NO, "用户名不存在", null);
        } else {
            //判断密码是否正确
            //将密码加密然后跟数据库中的密码相比较
            String md5password = MD5Utils.md5(password);
            if (users.get(0).getPassword().equals(md5password)) {

                JwtBuilder builder = Jwts.builder();

                HashMap<String, Object> map = new HashMap<>();

                map.put("key1", "v1");
                map.put("key2", "v2");

                String token = builder.setSubject(username)                                //主题，就是token中携带的数据
                        .setIssuedAt(new Date())                                            //设置token的生成时间
                        .setId(users.get(0).getUserId() + "")                                //设置token的id，这里将用户id设置为token的id
                        .setClaims(map)
                        .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))//设置token过期时间，这里设置为一天
                        .signWith(SignatureAlgorithm.HS256, "QIANfeng6666")                 //设置token的加密方式和加密密码
                        .compact();

                return new ResultVO(ResStatus.OK, token, users.get(0));
            } else {
                return new ResultVO(ResStatus.NO, "密码错误", null);
            }
        }
    }

    @Override
    public ResultVO quaryUserInfo(int userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        if (user != null){
            return new ResultVO(ResStatus.OK,"success",user);
        }else {
            return new ResultVO(ResStatus.NO,"fail",user);
        }
    }

    @Override
    public ResultVO modifyUserInfo(Users user) {
        String md5password = MD5Utils.md5(user.getPassword());
        user.setPassword(md5password);
        System.out.println(user);
        int i = usersMapper.updateByPrimaryKeySelective(user);
        if (i>0){
            return new ResultVO(ResStatus.OK,"success","修改成功");
        }else {
            return new ResultVO(ResStatus.NO,"fail","修改失败");
        }
    }
}
