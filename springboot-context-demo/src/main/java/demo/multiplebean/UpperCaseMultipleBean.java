package demo.multiplebean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("multiple-bean")
@Component
@ConditionalOnBean(MultipleBeanConfiguration.class)
public class UpperCaseMultipleBean implements IMultipleBean {

    @Override
    public void onString(String message) {
        logger.info("[UppercaseListener] message : {}", message == null ? "NULL" : message.toUpperCase());
    }
}
