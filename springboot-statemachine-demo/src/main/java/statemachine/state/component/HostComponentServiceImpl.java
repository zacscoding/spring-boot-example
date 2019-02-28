package statemachine.state.component;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.stereotype.Service;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class HostComponentServiceImpl implements HostComponentService {

    private HostComponentEntityRepository hostComponentEntityRepository;
    private PersistStateMachineHandler persistStateMachineHandler;

    @Autowired
    public HostComponentServiceImpl(
        HostComponentEntityRepository hostComponentEntityRepository,
        PersistStateMachineHandler persistStateMachineHandler) {

        this.hostComponentEntityRepository = hostComponentEntityRepository;
        this.persistStateMachineHandler = persistStateMachineHandler;
    }

    @Override
    public HostComponentEntity saveHostComponent(HostComponentEntity entity) {
        if (entity.getState() == null) {
            entity.setState(HostComponentState.IDLE);
        }

        return hostComponentEntityRepository.save(entity);
    }

    @Override
    public List<HostComponentEntity> getHostComponents() {
        return hostComponentEntityRepository.findAll();
    }

    @Override
    public boolean handleEvent(Long id, HostComponentEvent event) {
        Optional<HostComponentEntity> hostComponentEntityOptional = hostComponentEntityRepository.findById(id);

        if (!hostComponentEntityOptional.isPresent()) {
            logger.warn("Failed to find host component entity. id : {}", id);
            return false;
        }

        logger.info("find host component : " + hostComponentEntityOptional.get());

        return persistStateMachineHandler.handleEventWithState(
            MessageBuilder.withPayload(event.name())
                .setHeader(HostComponentEntity.class.getSimpleName(), hostComponentEntityOptional.get()).build(),
            hostComponentEntityOptional.get().getState().name()
        );
    }
}
