package statemachine.state.component;

import java.util.List;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface HostComponentService {

    HostComponentEntity saveHostComponent(HostComponentEntity entity);

    List<HostComponentEntity> getHostComponents();

    boolean handleEvent(Long id, HostComponentEvent event);
}
