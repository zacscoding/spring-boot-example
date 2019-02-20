package statemachine.state.component;

import statemachine.orm.ComponentEntity;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface Component {

    ComponentEntity getComponentEntity();

    String getComponentName();

    void handleEvent(ComponentEvent event);
}
