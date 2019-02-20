package statemachine.exception;

import lombok.Getter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public class InvalidStateTransitionException extends Exception {

    private Enum<?> currentState;
    private Enum<?> event;

    public InvalidStateTransitionException(Enum<?> currentState, Enum<?> event) {
        super("Invalid event : " + event + "at " + currentState);
        this.currentState = currentState;
        this.event = event;
    }
}
