package statemachine.demo.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;
import statemachine.state.component.HostComponentEvent;
import statemachine.state.component.HostComponentState;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentStatePersisTest {

    @Autowired
    private StateMachine<HostComponentState, HostComponentEvent> machine;

    @Test
    public void temp() {
        System.out.println(machine);
    }
}
