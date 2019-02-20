package statemachine.state.component;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum ComponentEvent {

    IDLE, ACTIVATE, DELETE, UNKNOWN;

    private static Set<ComponentEvent> ENUMS = EnumSet.allOf(ComponentEvent.class);

    public static ComponentEvent getType(String type) {
        if (StringUtils.hasText(type)) {
            for (ComponentEvent state : ENUMS) {
                if (type.equalsIgnoreCase(state.name())) {
                    return state;
                }
            }
        }

        return UNKNOWN;
    }

}
