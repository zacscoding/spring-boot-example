package statemachine.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;

/**
 * https://docs.spring.io/spring-statemachine/docs/2.0.3.RELEASE/reference/htmlsingle/#with-enablestatemachinefactory
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@DataJpaTest
public class StateMachinePersisterTest {

    StateMachine<String, String> machine;

    @Before
    public void setUp() throws Exception {
        Builder<String, String> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
            .initial("S1")
            .states(new HashSet<>(Arrays.asList("S1", "S2")));

        builder.configureTransitions()
            .withExternal()
            .source("S1").target("S2").event("E1");

        StateMachineFactory<String, String> factory;

        machine = builder.build();

        StateMachineRuntimePersister<String, String, PersistTestEntity> persister = new StateMachineRuntimePersister<String, String, PersistTestEntity>() {

            Map<PersistTestEntity, StateMachineContext<String, String>> inmemory = new HashMap();

            @Override
            public StateMachineInterceptor<String, String> getInterceptor() {
                return null;
            }

            @Override
            public void write(StateMachineContext<String, String> context, PersistTestEntity contextObj)
                throws Exception {
                logger.info("Write from persister context : {} / contextObj : {}", context, contextObj);
                inmemory.put(contextObj, context);
            }

            @Override
            public StateMachineContext<String, String> read(PersistTestEntity contextObj) throws Exception {
                logger.info("read from persister : {}", contextObj);
                return inmemory.get(contextObj);
            }
        };


    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = "name")
    @ToString
    public static class PersistTestEntity {

        private String state;
        private String name;
    }


}
