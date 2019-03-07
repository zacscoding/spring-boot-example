//package statemachine.state.action.fsm;
//
//import java.util.EnumSet;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.StateMachineBuilder;
//import org.springframework.statemachine.config.StateMachineBuilder.Builder;
//import statemachine.state.action.ActionEvent;
//import statemachine.state.action.ActionState;
//
///**
// * TODO :: will change to config
// *
// * @GitHub : https://github.com/zacscoding
// */
//@Slf4j
//public class ActionStateMachine {
//
//    private static final ActionStateMachine INSTANCE = new ActionStateMachine();
//    private StateMachine<ActionState, ActionEvent> machine;
//
//    private ActionStateMachine() {
//        initialize();
//    }
//
//    public StateMachine<ActionState, ActionEvent> getStateMachine() {
//        return this.machine;
//    }
//
//    private void initialize() {
//        try {
//            Builder<ActionState, ActionEvent> builder = StateMachineBuilder.builder();
//            builder.configureStates()
//                .withStates()
//                .initial(ActionState.INIT)
//                .states(EnumSet.allOf(ActionState.class));
//
//            builder.configureTransitions()
//                .withExternal()
//                .source(ActionState.INIT).target(ActionState.IN_PROGRESS).event(null)
//                .source(null).target(null).event(null)
//                .and()
//                .withExternal()
//            ;
//
//            machine = builder.build();
//            machine.start();
//        } catch (Exception e) {
//            logger.warn("Failed to initialize state machine", e);
//        }
//    }
//}
