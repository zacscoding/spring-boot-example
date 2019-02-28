package statemachine.state.component;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * Component state
 *
 * @GitHub : https://github.com/zacscoding
 */
public enum HostComponentState {

    IDLE, ACTIVE, DELETED, UNKNOWN;

    public static Set<HostComponentState> ENUMS = EnumSet.allOf(HostComponentState.class);

    public static HostComponentState getType(String type) {
        if (StringUtils.hasText(type)) {
            for (HostComponentState state : ENUMS) {
                if (type.equalsIgnoreCase(state.name())) {
                    return state;
                }
            }
        }

        return UNKNOWN;
    }
}
