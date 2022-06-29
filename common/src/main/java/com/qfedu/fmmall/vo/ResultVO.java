package com.qfedu.fmmall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chen
 * @date 2021/8/6-9:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO {

    //响应码
    private int code;

    //返回信息
    private String msg;

    //返回对象数据
    private Object data;

}
