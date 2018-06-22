package org.bootdemo.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
public class TempRule extends ParameterRule {

    private static final Logger logger = LoggerFactory.getLogger("rules");

    @Override
    public ValidationResult validate(Object param) {
        logger.info("Temp rule result : " + Success);
        return Success;
    }
}
