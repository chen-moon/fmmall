package com.qfedu.fmmall.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.github.wxpay.sdk.WXPay;
import com.qfedu.fmmall.config.MyPayConfig;
import com.qfedu.fmmall.dao.OrdersMapper;
import com.qfedu.fmmall.entity.Orders;
import com.qfedu.fmmall.service.OrderService;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen
 * @date 2022/3/2-10:26
 * @Description:
 */
@RestController
@CrossOrigin
@RequestMapping("/order")
@Api(value = "提供订单相关接口",tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("订单添加接口")
    @PostMapping("/add")
    public ResultVO add(String cids, @RequestBody Orders order){
        ResultVO resultVO = null;
        try {
            Map<String,String> orderInfo = orderService.addOrder(cids, order);
            //微信支付，申请微信支付链接
            WXPay wxPay = new WXPay(new MyPayConfig());
            HashMap<String,String> data = new HashMap<>();
            //设置支付内容
            data.put("body",orderInfo.get("productName"));              //商品描述
            data.put("out_trade_no",orderInfo.get("orderId"));            //使用当前用户订单的编号作为当前支付交易的交易编号
            data.put("fee_type","CNY");             //支付币种
            // data.put("total_fee",order.getActualAmount()*100+"");           //支付金额
            data.put("total_fee","1");           //支付金额
            data.put("trade_type","NATIVE");        //支付类型
            data.put("notify_url","http://8.130.103.175:8080/pay/callback");  //设置支付完成时的回调方法接口
            Map<String, String> resp = wxPay.unifiedOrder(data);
            orderInfo.put("payUrl",resp.get("code_url"));
            resultVO = new ResultVO(ResStatus.OK,"交易成功!",orderInfo);
        } catch (SQLException throwables) {
            resultVO = new ResultVO(ResStatus.NO,"下单失败！",null);
        } catch (Exception e) {
            resultVO = new ResultVO(ResStatus.NO,"交易失败！",null);
        }
        return resultVO;
    }

    @ApiOperation("查询订单状态接口")
    @GetMapping("/status/{oid}")
    public ResultVO getOrderStatus(@PathVariable("oid") String orderId,@RequestHeader("token") String token){
        ResultVO resultVO = orderService.getOrderById(orderId);
        return resultVO;
    }

    @ApiOperation("订单查询接口")
    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(dataType = "String",name = "status",value = "订单状态",required = true),
            @ApiImplicitParam(dataType = "int",name = "pageNum",value = "当前页码",required = true),
            @ApiImplicitParam(dataType = "int",name = "limit",value = "每页显示的条数",required = true)
    })
    public ResultVO list(@RequestHeader("token") String token,
                         String userId,String status,int pageNum,int limit){
        return orderService.listOrders(userId,status,pageNum,limit);
    }

    @ApiOperation("根据Id订单查询接口")
    @GetMapping("listById")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "userId",value = "用户id",required = true)
    })
    public ResultVO list(@RequestHeader("token") String token,String orderId) {
        return orderService.getOrderById(orderId);
    }

}
