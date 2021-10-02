package io.spring.batch.handleerror;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

public class EmptyInputStepFailer {

    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        if (execution.getReadCount() > 0) {
            return execution.getExitStatus();
        }
        return ExitStatus.FAILED;
    }
}
