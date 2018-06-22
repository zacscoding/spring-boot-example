package org.bootdemo.validator;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
public class PageRequestRule extends ParameterRule {

    private static final Logger logger = LoggerFactory.getLogger("rules");

    @Override
    public ValidationResult validate(Object param) {
        ValidationResult result = null;
        if (param == null) {
            logger.info("param is null => " + new ValidationResult(false, "??"));
            return new ValidationResult(false, "??");
        } else if (param instanceof Map) {
            result = preFilterMap((Map<String, Object>) param);
        } else if (true) { // TEMP
            throw new UnsupportedOperationException("Not support page request rule. class : " + param.getClass());
        }

        logger.info("PageRequestRule : " + result);
        return result;
    }

    private ValidationResult preFilterMap(Map<String, Object> param) {
        // page no request
        ValidationResult result = null;

        if ((result = checkIntegerFormat(param.get("PAGE_NO"), "Invalid page number value")) != null // check page no
            || (result = checkIntegerFormat(param.get("PAGE_SIZE"), "Invalid page size value")) != null) // check page size
        {
            return result;
        }

        return Success;
    }

    private ValidationResult checkIntegerFormat(Object val, String error) {
        if (val instanceof Number) {
            return null;
        } else if (val instanceof String) {
            try {
                Integer.parseInt((String) val);
                return null;
            } catch (NumberFormatException e) {
                return new ValidationResult(false, error);
            }
        }

        return new ValidationResult(false, error);
    }
}