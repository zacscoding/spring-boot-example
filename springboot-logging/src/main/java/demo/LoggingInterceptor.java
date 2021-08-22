package demo;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader("X-Request-ID");
        if (!StringUtils.hasLength(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put("x-request-id", requestId);
        System.out.println("Put requestId: " + requestId);
//
//        logger.info();
//
//        logger.info("LoggingInterceptor::preHandler is called");
//        final String requestId = UUID.randomUUID().toString();
//        MDC.put("x-request-id", requestId);
//        MDC.put("requestCount", String.valueOf(counter.getAndIncrement()));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        MDC.clear();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
