package demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ServletHelper {

    // =================================
    // HttpServletRequest
    // =================================
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static String getParameter(String param) {
        return getParameter(getHttpServletRequest(), param);
    }

    public static String getParameter(HttpServletRequest request, String param) {
        if (request != null) {
            return request.getParameter(param);
        }

        return null;
    }

    public static String getHeader(String name) {
        return getHeader(getHttpServletRequest(), name);
    }

    public static String getHeader(HttpServletRequest request, String name) {
        if (request != null) {
            return request.getHeader(name);
        }

        return null;
    }

    public static String getIpAddress() {
        return getIpAddress(getHttpServletRequest());
    }

    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        return extractIpAddress(request);
    }

    // =================================
    // HttpServletResponse
    // =================================
    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }

    // =================================
    // HttpSession
    // =================================
    public static HttpSession getSession() {
        HttpServletRequest req = getHttpServletRequest();

        if (req != null) {
            return req.getSession();
        }

        return null;
    }

    public static Object getSessionAttribute(String name) {
        HttpSession session = getSession();
        if (session != null) {
            return session.getAttribute(name);
        }

        return null;
    }

    private static String extractIpAddress(HttpServletRequest request) {
        // headers
        final String[] headerNames = new String[] {
                "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        String ip = null;

        for (String headerName : headerNames) {
            ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() != 0 && !(ip.length() == 7 && "unknown".equalsIgnoreCase(ip));
    }

    private ServletHelper() {
    }
}
