package statemachine.config;

import java.util.Arrays;
import java.util.HashSet;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

/**
 * @GitHub : https://github.com/zacscoding
 */
@TestConfiguration
@EnableStateMachineFactory
public class StateMachineFactoryTestConfig extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
            .initial("S1")
            .states(new HashSet<>(Arrays.asList("S1", "S2")));
    }
}
