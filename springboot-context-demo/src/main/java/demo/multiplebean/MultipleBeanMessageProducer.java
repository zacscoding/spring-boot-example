package demo.multiplebean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@ConditionalOnBean(MultipleBeanConfiguration.class)
public class MultipleBeanMessageProducer {

    List<IMultipleBean> listeners = new ArrayList<>();
    ConfigurableApplicationContext ctx;

    @Autowired
    public MultipleBeanMessageProducer(ConfigurableApplicationContext ctx) {
        this.ctx = ctx;
    }

    @PostConstruct
    private void setUp() {
        Map<String, IMultipleBean> beans = ctx.getBeansOfType(IMultipleBean.class);
        System.out.println("beans size :: " + beans.size());

        Iterator<String> itr = beans.keySet().iterator();
        while (itr.hasNext()) {
            String beanName = itr.next();
            log.info("IMultipleBean : {}", beanName);
            listeners.add(beans.get(beanName));
        }

        IntStream.range(1, 10).forEach(i -> {
            String message = UUID.randomUUID().toString();
            listeners.forEach(listener -> listener.onString(message));
        });
    }
}
