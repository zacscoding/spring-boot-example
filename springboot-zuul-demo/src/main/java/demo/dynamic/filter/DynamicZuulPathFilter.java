package demo.dynamic.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import demo.dynamic.route.DynamicRouteLocator;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DynamicZuulPathFilter extends ZuulFilter {

    private final String ROUTE_HOST = "routeHost";
    private final DynamicRouteLocator dynamicRouteLocator;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("DynamicZuulPathFilter::run() is called");
        RequestContext context = RequestContext.getCurrentContext();
        displayRequestContext(context);

        if (isDynamicRequest(context)) {
            context.put(ROUTE_HOST, dynamicRouteLocator.getCurrentUrl());
        }

        return null;
    }

    private void displayRequestContext(RequestContext context) {
        logger.info("Display request context");

        for (Entry<String, Object> entry : context.entrySet()) {
            logger.info("key : {} / value : {}", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Check whether proxy with named "dynamic" or not
     */
    private boolean isDynamicRequest(RequestContext requestContext) {
        return "dynamic".equals(requestContext.get(FilterConstants.PROXY_KEY));
    }
}