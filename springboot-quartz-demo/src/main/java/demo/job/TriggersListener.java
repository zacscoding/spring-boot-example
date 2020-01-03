package demo.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * https://advenoh.tistory.com/52
 */
@Component
@Slf4j
public class TriggersListener implements TriggerListener {

    @Override
    public String getName() {
        return "globalTrigger";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        JobKey jobKey = trigger.getJobKey();
        logger.info("triggerFired at {} :: jobKey : {}", trigger.getStartTime(), jobKey);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        logger.info("triggerMisfired at {} :: jobKey : {}", trigger.getStartTime(), jobKey);
    }

    @Override
    public void triggerComplete(Trigger trigger,
                                JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {

        JobKey jobKey = trigger.getJobKey();
        logger.info("triggerComplete at {} :: jobKey : {}", trigger.getStartTime(), jobKey);
    }
}