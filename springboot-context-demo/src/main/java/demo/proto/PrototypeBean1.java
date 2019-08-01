package demo.proto;

import demo.util.ProfileNames;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Profile(ProfileNames.PROTOTYPE)
@Slf4j(topic = "PrototypeBean1")
@Component
@Scope("prototype")
public class PrototypeBean1 implements DisposableBean {

    @Getter
    private Long id;
    private final PrototypeIdSupplier idSupplier;

    @Autowired
    public PrototypeBean1(PrototypeIdSupplier idSupplier) {
        this.idSupplier = idSupplier;
    }

    @PostConstruct
    private void setUp() {
        id = idSupplier.getId();
        logger.info("PrototypeBean1 is constructed. {}", this);
    }

    @PreDestroy
    private void tearDown() {
        System.out.println("tearDown() is called id : " + id);
    }

    @Override
    public String toString() {
        return "PrototypeBean1{" +
            "id=" + id +
            ", addr=" + super.toString() +
            '}';
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() is called id : " + id);
    }
}
