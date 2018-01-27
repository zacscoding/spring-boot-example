package org.boot.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd [HH:mm:ss]");

    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        logger.info("## ScheduledTasks::reportCurrentTime : " + sdf.format(new Date()));
    }
}
