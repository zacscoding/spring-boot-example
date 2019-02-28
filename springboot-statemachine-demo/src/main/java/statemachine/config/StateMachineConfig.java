package statemachine.config;

import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import statemachine.state.component.HostComponentEntity;
import statemachine.state.component.HostComponentEntityRepository;
import statemachine.state.component.HostComponentEvent;
import statemachine.state.component.HostComponentState;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
public class StateMachineConfig {

    @PostConstruct
    public void setUp() {
        System.out.println("StateMachineConfig`s config");
    }

    @Configuration
    // @EnableStateMachineFactory(name = "hostComponentStateMachine")
    @EnableStateMachine(name = "hostComponentStateMachine")
    public static class HostComponentStateMachineConfig extends
        StateMachineConfigurerAdapter<String, String> {

        @Override
        public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
            states.withStates()
                .initial(HostComponentState.IDLE.name())
                .states(new HashSet<String>(Arrays.asList(
                    HostComponentState.IDLE.name(),
                    HostComponentState.ACTIVE.name(),
                    HostComponentState.DELETED.name()
                )));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {

            transitions.withExternal()
                .source(HostComponentState.IDLE.name())
                .target(HostComponentState.ACTIVE.name())
                .event(HostComponentEvent.ACTIVATE.name())
                .and().withExternal()
                .source(HostComponentState.ACTIVE.name())
                .target(HostComponentState.IDLE.name())
                .event(HostComponentEvent.IDLE.name())
                .and().withExternal()
                .source(HostComponentState.IDLE.name())
                .target(HostComponentState.DELETED.name())
                .event(HostComponentEvent.DELETE.name())
                .and().withExternal()
                .source(HostComponentState.ACTIVE.name())
                .target(HostComponentState.DELETED.name())
                .event(HostComponentEvent.DELETE.name());
        }
    }

    @Configuration
    public static class HostComponentPersistConfiguration {

        private StateMachine<String, String> hostStateMachine;
        private HostComponentEntityRepository hostComponentEntityRepository;

        @Autowired
        public HostComponentPersistConfiguration(
            @Qualifier("hostComponentStateMachine") StateMachine<String, String> hostStateMachine,
            HostComponentEntityRepository hostComponentEntityRepository) {

            this.hostStateMachine = hostStateMachine;
            this.hostComponentEntityRepository = hostComponentEntityRepository;
        }

        @Bean
        public PersistStateMachineHandler hostComponentPersistStateMachineHandler() {
            PersistStateMachineHandler handler = new PersistStateMachineHandler(hostStateMachine);

            handler.addPersistStateChangeListener(new PersistStateChangeListener() {
                @Override
                public void onPersist(State<String, String> state, Message<String> message,
                    Transition<String, String> transition, StateMachine<String, String> stateMachine) {
                    logger.info("## On persist state : " + state);
                    logger.info("## On message : " + message);
                    logger.info("## On transition : " + transition);
                    logger.info("## On stateMachine : " + stateMachine);

                    if (message == null
                        || !message.getHeaders().containsKey(HostComponentEntity.class.getSimpleName())) {
                        return;
                    }

                    HostComponentEntity hostComponentEntity = (HostComponentEntity) message.getHeaders()
                        .get(HostComponentEntity.class.getSimpleName());

                    hostComponentEntity.setState(HostComponentState.getType(state.getId()));

                    hostComponentEntityRepository.save(hostComponentEntity);
                }
            });

            return handler;
        }
    }
}
