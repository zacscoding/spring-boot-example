package demo.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        // Logging current thread's stacktrace for debugging actuate code :)
        final StringBuilder sb = new StringBuilder();
        for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
            // skip from servlet
            if ("javax.servlet.http.HttpServlet".equals(se.getClassName())) {
                break;
            }
            sb.append(se).append('\n');
        }
        logger.info("## doHealthCheck is called()\n{}", sb.toString());

        builder.up()
               .withDetail("type", "custom indicator")
               .build();
    }
    // Stack trace
    //demo.health.CustomHealthIndicator.doHealthCheck(CustomHealthIndicator.java:16)
    //org.springframework.boot.actuate.health.AbstractHealthIndicator.health(AbstractHealthIndicator.java:82)
    //org.springframework.boot.actuate.health.HealthIndicator.getHealth(HealthIndicator.java:37)
    //org.springframework.boot.actuate.health.HealthEndpointWebExtension.getHealth(HealthEndpointWebExtension.java:85)
    //org.springframework.boot.actuate.health.HealthEndpointWebExtension.getHealth(HealthEndpointWebExtension.java:44)
    //org.springframework.boot.actuate.health.HealthEndpointSupport.getContribution(HealthEndpointSupport.java:99)
    //org.springframework.boot.actuate.health.HealthEndpointSupport.getAggregateHealth(HealthEndpointSupport.java:110)
    //org.springframework.boot.actuate.health.HealthEndpointSupport.getContribution(HealthEndpointSupport.java:96)
    //org.springframework.boot.actuate.health.HealthEndpointSupport.getHealth(HealthEndpointSupport.java:74)
    //org.springframework.boot.actuate.health.HealthEndpointSupport.getHealth(HealthEndpointSupport.java:61)
    //org.springframework.boot.actuate.health.HealthEndpointWebExtension.health(HealthEndpointWebExtension.java:71)
    //org.springframework.boot.actuate.health.HealthEndpointWebExtension.health(HealthEndpointWebExtension.java:60)
    //sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    //sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    //sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    //java.lang.reflect.Method.invoke(Method.java:498)
    //org.springframework.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:282)
    //org.springframework.boot.actuate.endpoint.invoke.reflect.ReflectiveOperationInvoker.invoke(ReflectiveOperationInvoker.java:77)
    //org.springframework.boot.actuate.endpoint.annotation.AbstractDiscoveredOperation.invoke(AbstractDiscoveredOperation.java:60)
    //org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$ServletWebOperationAdapter.handle(AbstractWebMvcEndpointHandlerMapping.java:305)
    //org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping$OperationHandler.handle(AbstractWebMvcEndpointHandlerMapping.java:388)
}
