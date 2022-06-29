package com.qfedu.fmmall.service;

import com.qfedu.fmmall.entity.Orders;
import com.qfedu.fmmall.vo.ResultVO;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author chen
 * @date 2022/3/1-16:12
 * @Description:
 */
public interface OrderService {

    public Map<String,String> addOrder(String cids, Orders order) throws SQLException;

    public int updateOrderStatus(String orderId,String status);

    public ResultVO getOrderById(String orderId);

    public void closeOrder(String orderId);

    public ResultVO listOrders(String userId,String status,int pageNum,int limit);

    public ResultVO getOrdersByOid(String orderId);

}
