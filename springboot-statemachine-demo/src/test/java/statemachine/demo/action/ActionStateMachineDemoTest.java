package statemachine.demo.action;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import statemachine.state.action.ActionEvent;
import statemachine.state.action.ActionState;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class ActionStateMachineDemoTest {

    StateMachine<ActionState, ActionEvent> actionStateMachine;

    @Before
    public void setUp() throws Exception {
        Builder<ActionState, ActionEvent> builder = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
            .initial(ActionState.INIT)
            .states(EnumSet.allOf(ActionState.class));

        builder.configureTransitions()
            .withExternal()
            .source(ActionState.INIT).target(ActionState.IN_PROGRESS).event(ActionEvent.ACTION_IN_PROGRESS)
        .and()
            .withExternal()
            .source(ActionState.INIT).target(ActionState.COMPLETED).event(ActionEvent.ACTION_COMPLETE)
        .and()
            .withExternal()
            .source(ActionState.INIT).target(ActionState.FAILED).event(ActionEvent.ACTION_FAILED)

        .and()
            .withExternal()
            .source(ActionState.IN_PROGRESS).target(ActionState.IN_PROGRESS).event(ActionEvent.ACTION_IN_PROGRESS)
        .and()
            .withExternal()
            .source(ActionState.IN_PROGRESS).target(ActionState.COMPLETED).event(ActionEvent.ACTION_COMPLETE)
        .and()
            .withExternal()
            .source(ActionState.IN_PROGRESS).target(ActionState.FAILED).event(ActionEvent.ACTION_FAILED)

        .and()
            .withExternal()
            .source(ActionState.COMPLETED).target(ActionState.INIT).event(ActionEvent.ACTION_INIT)

        .and()
            .withExternal()
            .source(ActionState.FAILED).target(ActionState.INIT).event(ActionEvent.ACTION_INIT);

        actionStateMachine = builder.build();
        actionStateMachine.start();
    }

    @Test
    public void testTransitions() {
        assertThat(actionStateMachine.getState().getId()).isEqualTo(ActionState.INIT);

        // INIT state -> (transition) ACTION_COMPLETE -> COMPLETED state
        actionStateMachine.sendEvent(ActionEvent.ACTION_COMPLETE);
        assertThat(actionStateMachine.getState().getId()).isEqualTo(ActionState.COMPLETED);

        // COMPLETED state-> (transition) ACTION_INIT -> INIT state
        actionStateMachine.sendEvent(ActionEvent.ACTION_INIT);
        assertThat(actionStateMachine.getState().getId()).isEqualTo(ActionState.INIT);


        // INIT state -> (transition) ACTION_IN_PROGRESS -> IN_PROGRESS state
        actionStateMachine.sendEvent(ActionEvent.ACTION_IN_PROGRESS);
        assertThat(actionStateMachine.getState().getId()).isEqualTo(ActionState.IN_PROGRESS);

        // IN_PROGRESS state -> (transition) ACTION_INIT -> IN_PROGRESS state
        actionStateMachine.sendEvent(ActionEvent.ACTION_INIT);
        assertThat(actionStateMachine.getState().getId()).isEqualTo(ActionState.IN_PROGRESS);
    }
}
