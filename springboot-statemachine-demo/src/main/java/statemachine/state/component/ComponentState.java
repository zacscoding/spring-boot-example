package statemachine.state.component;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * Component state
 *
 * @GitHub : https://github.com/zacscoding
 */
public enum ComponentState {

    IDLE, ACTIVE, DELETED, UNKNOWN;

    public static Set<ComponentState> ENUMS = EnumSet.allOf(ComponentState.class);

    public static ComponentState getType(String type) {
        if (StringUtils.hasText(type)) {
            for (ComponentState state : ENUMS) {
                if (type.equalsIgnoreCase(state.name())) {
                    return state;
                }
            }
        }

        return UNKNOWN;
    }
}
