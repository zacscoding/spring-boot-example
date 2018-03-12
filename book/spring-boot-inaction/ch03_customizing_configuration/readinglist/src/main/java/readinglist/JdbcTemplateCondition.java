package readinglist;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author zacconding
 * @Date 2018-02-26
 * @GitHub : https://github.com/zacscoding
 */
public class JdbcTemplateCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            context.getClassLoader().loadClass("org.springframework.jdbc.core.JdbcTemplate");
            System.out.println("exist JdbcTemplate");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
