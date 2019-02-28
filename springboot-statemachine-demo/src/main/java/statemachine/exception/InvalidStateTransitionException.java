package statemachine.exception;

import lombok.Getter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public class InvalidStateTransitionException extends RuntimeException {

    private String currentState;
    private String event;

    public InvalidStateTransitionException(String currentState, String event) {
        super("Invalid event : " + event + "at " + currentState);
        this.currentState = currentState;
        this.event = event;
    }
    /*private Enum<?> currentState;
    private Enum<?> event;

    public InvalidStateTransitionException(Enum<?> currentState, Enum<?> event) {
        super("Invalid event : " + event + "at " + currentState);
        this.currentState = currentState;
        this.event = event;
    }*/
}
