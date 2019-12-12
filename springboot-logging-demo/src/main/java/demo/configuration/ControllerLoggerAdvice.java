package demo.configuration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.util.ServletHelper;

@Component
@Aspect
public class ControllerLoggerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggerAdvice.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* demo.rest.*Controller.*(..))")
    public Object logProcess(ProceedingJoinPoint pjp) throws Throwable {
        String txId = "" + System.currentTimeMillis();
        MDC.put("txId", txId);
        final String id = pjp.getTarget().getClass().getSimpleName()
                          + " :: "
                          + pjp.getSignature().getName();
        final HttpServletRequest request = ServletHelper.getHttpServletRequest();

        final StringBuilder builder = new StringBuilder(
                "## ======================Before Catch =================================== ##\n");

        builder.append("TXID : ").append(txId).append('\n')
               .append("ID : ").append(id).append('\n')
               .append("URI : ").append(request.getRequestURI()).append('\n')
               .append("IP : ").append(ServletHelper.getIpAddress(request)).append('\n')
               .append("Param : ").append(extractParams(pjp.getArgs())).append('\n')
               .append("## ===================================================================== ##\n");

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            builder.append("## ======================After Catch =================================== ##\n");
            builder.append("TXID : ").append(txId).append('\n')
                   .append("Exception : ").append(e.getMessage()).append('n')
                   .append("## ===================================================================== ##\n");
            MDC.clear();
            throw e;
        }

        builder.append("## ======================After Catch =================================== ##\n");
        builder.append("TXID : ").append(txId).append('\n')
               .append("Result : ").append(result == null ? "null" : result.toString()).append('\n')
               .append("## ===================================================================== ##\n");
        logger.info("\n" + builder);

        MDC.clear();

        return result;
    }

    private String extractParams(Object[] params) {
        if (params == null) {
            return "null";
        }

        if (params.length == 0) {
            return "empty";
        }

        try {
            return objectMapper.writeValueAsString(params);
        } catch (Exception e) {
            return "Parse error " + e.getMessage();
        }
    }
}
