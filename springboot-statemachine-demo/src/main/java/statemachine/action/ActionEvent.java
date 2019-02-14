package statemachine.action;

import lombok.Getter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
public abstract class ActionEvent {

    private final long actionId;
    private final ActionEventType actionEventType;
    private long timestamp;

    public ActionEvent(long actionId, ActionEventType actionEventType) {
        this(actionId, actionEventType, -1L);
    }

    public ActionEvent(long actionId, ActionEventType actionEventType, long timestamp) {
        this.actionId = actionId;
        this.actionEventType = actionEventType;
        this.timestamp = timestamp;
    }
}
