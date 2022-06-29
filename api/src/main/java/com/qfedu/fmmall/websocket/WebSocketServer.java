package com.qfedu.fmmall.websocket;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chen
 * @date 2022/4/15-15:46
 * @Description:
 */
@Component
@ServerEndpoint("/websocket/{oid}")
public class WebSocketServer {

    private static ConcurrentHashMap<String,Session> sessionMap = new ConcurrentHashMap<>();

    //前端发送请求建立websocket谅解，就会执行@OnOpen方法
    @OnOpen
    public void open(@PathParam("oid") String orderId, Session session){
        sessionMap.put(orderId,session);
    }

    //前端关闭页面或者主动断开链接时，都会执行@OnClose方法
    @OnClose
    public void close(@PathParam("oid") String orderId){
        sessionMap.remove(orderId);
    }

    public static void sendMessage(String orderId,String message){
        try {
            Session session = sessionMap.get(orderId);
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
