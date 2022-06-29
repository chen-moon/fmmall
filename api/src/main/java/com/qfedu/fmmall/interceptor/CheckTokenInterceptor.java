package com.qfedu.fmmall.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qfedu.fmmall.vo.ResStatus;
import com.qfedu.fmmall.vo.ResultVO;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author chen
 * @date 2021/8/17-11:12
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //浏览器的预检机制，先发一次请求确认连接是否畅通，并且预检请求的method="OPTIONS"，
        //只有当预检请求得到回应，浏览器才会发送第二次请求，真正的请求
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)){
            return true;
        }

        String token = request.getHeader("token");
        if (token  == null || token.equals("null") || token.equals("")){
            ResultVO resultVO = new ResultVO(ResStatus.LOGIN_FAIL_NOT, "请先登录。。。", null);
            doResponse(response,resultVO);
        }else {
            try {
                JwtParser parser = Jwts.parser();
                parser.setSigningKey("QIANfeng6666");//解析token的SigningKey必须和生成token时设置密码一样

                //如果token正确（加密密码正确，有效期没有过）则正常执行，执行后可以返回Claims对象，否则抛出异常
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                return true;
            }catch (ExpiredJwtException e){
                ResultVO resultVO = new ResultVO(ResStatus.LOGIN_OVERDUE,"token已过期，请重新登录。。。",null);
                doResponse(response,resultVO);
            }catch (UnsupportedJwtException e){
                ResultVO resultVO = new ResultVO(ResStatus.NO,"Tonken不合法，请⾃重！",null);
                doResponse(response,resultVO);
            }catch (Exception e){
                ResultVO resultVO = new ResultVO(ResStatus.LOGIN_FAIL_NOT,"请重新登录！",null);
                doResponse(response,resultVO);
            }
        }
        return false;
    }
    public void doResponse(HttpServletResponse response,ResultVO resultVO) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            String s = new ObjectMapper().writeValueAsString(resultVO);
            out.print(s);
            out.flush();
            out.close();
    }
}

