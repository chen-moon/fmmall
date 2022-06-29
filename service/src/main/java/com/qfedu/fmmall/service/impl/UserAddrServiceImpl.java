package com.qfedu.fmmall.service.impl;

import com.qfedu.fmmall.dao.UserAddrMapper;
import com.qfedu.fmmall.entity.UserAddr;
import com.qfedu.fmmall.service.UserAddrService;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chen
 * @date 2021/11/12-15:10
 * @Description:
 */
@Service
public class UserAddrServiceImpl implements UserAddrService {

    @Autowired
    private UserAddrMapper userAddrMapper;

    @Override
    public ResultVO listUserAddrsByUid(int userId) {

        Example example = new Example(UserAddr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);

        List<UserAddr> userAddrs = userAddrMapper.selectByExample(example);

        if (userAddrs.size()>0){
            return new ResultVO(ResStatus.OK,"success",userAddrs);
        }else {
            return new ResultVO(ResStatus.NO,"fail",null);
        }
    }

    @Override
    public ResultVO modifiedAddrs(UserAddr userAddr) {
        // if (userAddr.getAddrId() != null || !"".equals(userAddr.getAddrId())){
        //
        // }
        int i = userAddrMapper.updateByPrimaryKey(userAddr);
        if (i>0){
            return new ResultVO(ResStatus.OK,"success",null);
        }else {
            return new ResultVO(ResStatus.NO,"fail",null);
        }
    }

    @Override
    public ResultVO getTopAddrId() {
        List<UserAddr> userAddrs = userAddrMapper.selectAll();
        String addrId = userAddrs.get(userAddrs.size() - 1).getAddrId();
        return new ResultVO(ResStatus.OK,"success",addrId);
    }

    @Override
    public ResultVO addAddrs(UserAddr userAddr) {
        int i = userAddrMapper.insert(userAddr);
        if (i>0){
            return new ResultVO(ResStatus.OK,"success",null);
        }else {
            return new ResultVO(ResStatus.NO,"fail",null);
        }
    }
}
