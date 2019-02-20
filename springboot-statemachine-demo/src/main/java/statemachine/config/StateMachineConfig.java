package statemachine.config;

import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import statemachine.orm.ComponentEntity;
import statemachine.orm.ComponentEntityRepository;
import statemachine.state.component.ComponentEvent;
import statemachine.state.component.ComponentState;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class StateMachineConfig {

    @PostConstruct
    public void setUp() {
        System.out.println("StateMachineConfig`s config");
    }

    @Configuration
    @EnableStateMachine(name = "componentStateMachine")
    public static class ComponentStateMachineConfig extends
        StateMachineConfigurerAdapter<ComponentState, ComponentEvent> {

        @PostConstruct
        public void setUp() {
            System.out.println("ComponentStateMachineConfig`s config");
        }

        @Override
        public void configure(StateMachineStateConfigurer<ComponentState, ComponentEvent> states) throws Exception {
            states.withStates()
                .initial(ComponentState.IDLE)
                .states(ComponentState.ENUMS);
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<ComponentState, ComponentEvent> transitions)
            throws Exception {

            transitions.withExternal()
                .source(ComponentState.IDLE).target(ComponentState.ACTIVE).event(ComponentEvent.ACTIVATE)
                .and().withExternal()
                .source(ComponentState.ACTIVE).target(ComponentState.IDLE).event(ComponentEvent.IDLE);
        }
    }

    @Slf4j
    public static class ComponentStateMachinePersist implements
        StateMachinePersist<ComponentState, ComponentEvent, ComponentEntity> {

        private ComponentEntityRepository componentEntityRepository;
        private StateMachine<ComponentState, ComponentEvent> machine;

        public ComponentStateMachinePersist(ComponentEntityRepository componentEntityRepository,
            StateMachine<ComponentState, ComponentEvent> machine) {
            Objects.requireNonNull(componentEntityRepository, "componentEntityRepository must be not null");
            Objects.requireNonNull(machine, "machine must be not null");

            this.componentEntityRepository = componentEntityRepository;
            this.machine = machine;
        }

        @Override
        public void write(StateMachineContext<ComponentState, ComponentEvent> context, ComponentEntity contextObj)
            throws Exception {
            logger.info("persist component entity");

        }

        @Override
        public StateMachineContext<ComponentState, ComponentEvent> read(ComponentEntity contextObj) throws Exception {
            return null;
        }
    }
}
