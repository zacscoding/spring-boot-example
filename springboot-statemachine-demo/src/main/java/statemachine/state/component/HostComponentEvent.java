package statemachine.state.component;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum HostComponentEvent {

    IDLE, ACTIVATE, DELETE, UNKNOWN;

    private static Set<HostComponentEvent> ENUMS = EnumSet.allOf(HostComponentEvent.class);

    public static HostComponentEvent getType(String type) {
        if (StringUtils.hasText(type)) {
            for (HostComponentEvent state : ENUMS) {
                if (type.equalsIgnoreCase(state.name())) {
                    return state;
                }
            }
        }

        return UNKNOWN;
    }

}
