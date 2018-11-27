package org.zerock.security;

import lombok.extern.java.Log;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zacconding
 * @Date 2017-12-25
 * @GitHub : https://github.com/zacscoding
 */
@Log
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public LoginSuccessHandler() {
        log.info("## LoginSuccecssHandler()");
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        log.info("## --------------determineTargetUrl------------------------");
        Object dest = request.getSession().getAttribute("dest");

        String nextUrl = null;
        if (dest != null) {
            request.getSession().removeAttribute("dest");
            nextUrl = (String) dest;
        } else {
            nextUrl = super.determineTargetUrl(request, response);
        }

        log.info("## --------------" + nextUrl + "------------------------");

        return nextUrl;
    }
}
