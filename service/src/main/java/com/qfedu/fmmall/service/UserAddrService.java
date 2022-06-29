package com.qfedu.fmmall.service;

import com.qfedu.fmmall.entity.UserAddr;
import com.qfedu.fmmall.vo.ResultVO;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @date 2021/11/12-15:09
 * @Description:
 */
@Repository
public interface UserAddrService {

    public ResultVO listUserAddrsByUid(int userId);

    public ResultVO modifiedAddrs(UserAddr userAddr);

    public ResultVO getTopAddrId();

    public ResultVO addAddrs(UserAddr userAddr);

}
