package demo.multiplebean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@ConditionalOnBean(MultipleBeanConfiguration.class)
public class LowerCaseMultipleBean implements IMultipleBean {

    @Override
    public void onString(String message) {
        log.info("[LowerCaseListener] message : {}", message == null ? "NULL" : message.toLowerCase());
    }
}
