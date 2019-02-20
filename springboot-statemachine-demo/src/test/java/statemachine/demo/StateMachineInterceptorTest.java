package statemachine.demo;

import java.util.Arrays;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.transition.Transition;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class StateMachineInterceptorTest {

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

        machine = builder.build();
        machine.getStateMachineAccessor().withRegion()
            .addStateMachineInterceptor(new StateMachineInterceptor<String, String>() {

                @Override
                public Message<String> preEvent(Message<String> message, StateMachine<String, String> stateMachine) {
                    logger.info("## preEvent. message : {} / state machine : {}"
                        , message, stateMachine.getState().getId());
                    return message;
                }

                @Override
                public StateContext<String, String> preTransition(StateContext<String, String> stateContext) {
                    logger.info("## preTransition. stateContext : {}", stateContext);
                    return stateContext;
                }

                @Override
                public void preStateChange(State<String, String> state, Message<String> message,
                    Transition<String, String> transition, StateMachine<String, String> stateMachine) {

                    logger.info("## preStateChange. stateContext : {}", stateMachine);
                }

                @Override
                public StateContext<String, String> postTransition(StateContext<String, String> stateContext) {
                    logger.info("## postTransition : {}", stateContext);
                    if (true) {
                        throw new RuntimeException("Force exception");
                    }

                    return stateContext;
                }

                @Override
                public void postStateChange(State<String, String> state, Message<String> message,
                    Transition<String, String> transition, StateMachine<String, String> stateMachine) {
                }

                @Override
                public Exception stateMachineError(StateMachine<String, String> stateMachine,
                    Exception exception) {
                    logger.warn("stateMachineError :: " + exception.getMessage());
                    return exception;
                }
            });

        machine.start();
    }

    @Test
    public void eventOccurAndFail() {
        logger.info("## Init state :: " + machine.getState().getId());
        machine.sendEvent("E1");
        logger.info("## After state :: " + machine.getState().getId());

        System.out.println(machine.getState().getId());
    }

}
