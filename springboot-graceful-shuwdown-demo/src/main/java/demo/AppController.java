package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AppController {

    private final TaskExecutor taskExecutor;
    private final ThreadPoolTaskExecutor notBeanTaskExecutor;

    @Autowired
    public AppController(@Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        notBeanTaskExecutor = new ThreadPoolTaskExecutor();
        notBeanTaskExecutor.setCorePoolSize(2);
        notBeanTaskExecutor.setMaxPoolSize(2);
        notBeanTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        notBeanTaskExecutor.setAwaitTerminationSeconds(10);
        notBeanTaskExecutor.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(notBeanTaskExecutor::shutdown));
    }

    @GetMapping("/long-process")
    public String pause() throws InterruptedException {
        pauseInternal();
        return "Process finished";
    }

    @GetMapping("/tasks")
    public String startTasks() {
        for (int i = 0; i < 2; i++) {
            taskExecutor.execute(() -> {
                try {
                    pauseInternal();
                } catch (Exception e) {
                    logger.warn("Exception occur while doing pause", e);
                }
            });
        }

        return "Started task";
    }

    @GetMapping("/tasks-not-bean")
    public String startNotBeanTasks() {
        for (int i = 0; i < 2; i++) {
            notBeanTaskExecutor.execute(() -> {
                try {
                    pauseInternal();
                } catch (Exception e) {
                    logger.warn("Exception occur while doing pause", e);
                }
            });
        }

        return "Started task";
    }

    private void pauseInternal() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            logger.info("working ... {}-{}", Thread.currentThread().getName(), i);
            Thread.sleep(1000);
        }
        logger.info("Complete task.");
    }
}
