package server.rest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-12-21
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "index")
@RestController
public class IndexController {

    private Thread printRandomMessageTask;

    @GetMapping("/{level}/{message}")
    public void printInfoLog(@PathVariable("level") String level, @PathVariable("message") String message) {
        printMessage(level, message);
    }

    @GetMapping("/print/{action}")
    public String printTaskAction(@PathVariable("action") String action) {
        if (!StringUtils.hasText(action)) {
            return "INVALID ACTION : " + action;
        }

        if ("start".equalsIgnoreCase(action)) {
            return startTask();
        }

        if ("stop".equalsIgnoreCase(action)) {
            return stopTask();
        }

        return "INVALID ACTION : " + action;
    }

    private String startTask() {
        if (printRandomMessageTask != null && !printRandomMessageTask.isInterrupted()) {
            return "ALREADY STARTED";
        }

        printRandomMessageTask = new Thread(() -> {
            try {
                String[] levels = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR"};
                Random random = new Random();
                while (!Thread.currentThread().isInterrupted()) {
                    String level = levels[random.nextInt(levels.length)];
                    String message = UUID.randomUUID().toString();

                    printMessage(level, message);

                    TimeUnit.SECONDS.sleep(2L);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
        printRandomMessageTask.setDaemon(true);
        printRandomMessageTask.start();

        return "SUCCESS TO START TASK : " + printRandomMessageTask.getName() + "-" + printRandomMessageTask.getId();
    }

    private String stopTask() {
        if (printRandomMessageTask == null || printRandomMessageTask.isInterrupted()) {
            return "ALREADY STOPPED";
        }

        printRandomMessageTask.interrupt();

        return "SUCCESS TO STOP TASK";
    }

    private void printMessage(String level, String message) {
        if (!StringUtils.hasText(level)) {
            log.error("Invalid log level");
            return;
        }

        switch (level.toUpperCase()) {
            case "TRACE":
                log.trace(message);
                break;
            case "DEBUG":
                log.debug(message);
                break;
            case "INFO":
                log.info(message);
                break;
            case "WARN":
                log.warn(message);
                break;
            case "ERROR":
                log.error(message);
                break;
            default:
                log.error("Invalid log level : " + level);
        }
    }
}
