package demo.proto;

import demo.util.ProfileNames;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Profile(ProfileNames.PROTOTYPE)
@Slf4j
@RestController
public class PrototypeBeanController {

    private final ApplicationContext ctx;
    private ConcurrentHashMap<Long, PrototypeBean1> beans = new ConcurrentHashMap<>();

    @Autowired
    public PrototypeBeanController(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @GetMapping("/prototypes")
    public ResponseEntity getBeans() {
        logger.info("getBeans is called");
        StringBuilder result = new StringBuilder("Getting PrototypeBean1 from map\n");

        for (PrototypeBean1 value : beans.values()) {
            result.append(value)
                .append("\n");
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/prototypes/ctx")
    public ResponseEntity getBeansFromCtx() {
        // 매번 새로운 PrototypeBean1 이 생성되어 Map::size() == 1 인 맵을 반환
        StringBuilder result = new StringBuilder("Getting PrototypeBeans1 from ctx\n");

        Map<String, PrototypeBean1> beansOfType = ctx.getBeansOfType(PrototypeBean1.class);

        result.append("> bean size : ").append(beansOfType.size()).append("\n");

        for (PrototypeBean1 value : beansOfType.values()) {
            result.append(">> ").append(value.toString());
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/prototype/{id}")
    public ResponseEntity getBean(@PathVariable("id") Long id) {
        logger.info("Try to get bean id : {}", id);

        PrototypeBean1 bean = beans.get(id);

        if (bean == null) {
            logger.info("> not exist");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bean.toString());
    }

    @PostMapping("/prototype")
    public ResponseEntity createBean() {
        logger.info("Try to create bean");

        PrototypeIdSupplier idSupplier = ctx.getBean(PrototypeIdSupplier.class);
        PrototypeBean1 bean = ctx.getBean(PrototypeBean1.class, idSupplier);

        logger.info("Success to create bean. id : {}", bean.getId());

        beans.putIfAbsent(bean.getId(), bean);

        return ResponseEntity.ok(bean.toString());
    }

    @DeleteMapping("/prototype/{id}")
    public ResponseEntity destroyBean(@PathVariable("id") Long id) {
        logger.info("Try to destroy bean. id : {}", id);

        PrototypeBean1 bean = beans.remove(id);

        if (bean == null) {
            return ResponseEntity.notFound().build();
        }

        String result = null;

        try {
            result = bean.toString();

            if (bean instanceof DisposableBean) {
                logger.info("Can cast DisposableBean");
                DisposableBean disposable = (DisposableBean) bean;
                disposable.destroy();
            }
        } catch (Exception e) {
            result = e.getMessage();
        }

        return ResponseEntity.ok(result);
    }
}
