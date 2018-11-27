package org.zerock.interceptor;

import lombok.extern.java.Log;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zacconding
 * @Date 2017-12-25
 * @GitHub : https://github.com/zacscoding
 */
@Log
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("## pre handle");

        String dest = request.getParameter("dest");

        if (dest != null) {
            log.info("## exist dest : " + dest);
            request.getSession().setAttribute("dest", dest);
        }

        return super.preHandle(request, response, handler);
    }
}
