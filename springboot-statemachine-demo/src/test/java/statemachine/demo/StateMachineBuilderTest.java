package statemachine.demo;

import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import statemachine.demo.simple.SimpleEvents;
import statemachine.demo.simple.SimpleStates;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class StateMachineBuilderTest {

    @Test
    public void buildMachine() throws Exception {

        Builder<SimpleStates, SimpleEvents> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
            .initial(SimpleStates.STATE1)
            .states(EnumSet.allOf(SimpleStates.class));

        builder.configureTransitions()
            .withExternal()
            .source(SimpleStates.STATE1).target(SimpleStates.STATE2)
            .event(SimpleEvents.EVENT1)
            .and()
            .withExternal()
            .source(SimpleStates.STATE2).target(SimpleStates.STATE1)
            .event(SimpleEvents.EVENT2);
        StateMachine<SimpleStates, SimpleEvents> machine = builder.build();
        machine.addStateListener(createListener());

        logger.info("## Initialized machine :: {}", machine.getState());
        machine.start();
        logger.info("## After started :: {}", machine.getState().getId());

        machine.sendEvent(SimpleEvents.EVENT1);
        logger.info("## After event1 transition :: {}", machine.getState().getId());
    }

    @Test
    public void buildMachineWithActions() throws Exception {
        Builder<SimpleStates, SimpleEvents> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
            .initial(SimpleStates.STATE1)
            .states(EnumSet.allOf(SimpleStates.class));

        builder.configureTransitions()
            .withExternal()
            .source(SimpleStates.STATE1).target(SimpleStates.STATE2)
            .event(SimpleEvents.EVENT1).action(createAction())
            .and()
            .withExternal()
            .source(SimpleStates.STATE2).target(SimpleStates.STATE1)
            .event(SimpleEvents.EVENT2).action(createAction());

        StateMachine<SimpleStates, SimpleEvents> machine = builder.build();
        machine.addStateListener(createListener());

        machine.start();
        machine.sendEvent(SimpleEvents.EVENT1);
        System.out.println("Finally :: " + machine.getState().getId());
    }

    private Action<SimpleStates, SimpleEvents> createAction() {
        return context -> {
            State<SimpleStates, SimpleEvents> source = context.getSource();
            State<SimpleStates, SimpleEvents> target = context.getTarget();

            logger.info("action`s execute is called.. will changed {} > {}", source.getId(), target.getId());
        };
    }

    private StateMachineListenerAdapter<SimpleStates, SimpleEvents> createListener() {
        return new StateMachineListenerAdapter<SimpleStates, SimpleEvents>() {
            @Override
            public void stateChanged(State<SimpleStates, SimpleEvents> from, State<SimpleStates, SimpleEvents> to) {
                SimpleStates fromStates = (from == null) ? null : from.getId();
                SimpleStates toStates = (to == null) ? null : to.getId();
                logger.info("## Listener :: {} > {}", fromStates, toStates);
                throw new RuntimeException("");
            }
        };
    }
}
