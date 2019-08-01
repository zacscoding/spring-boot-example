package demo.proto;

import demo.util.ProfileNames;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 *
 */
@Profile(ProfileNames.PROTOTYPE)
@Slf4j(topic = "PrototypeIdSupplier")
@Component
public class PrototypeIdSupplier {

    private AtomicLong idGenerator = new AtomicLong(0L);

    public Long getId() {
        return idGenerator.incrementAndGet();
    }
}
