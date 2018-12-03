package org.boot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class ScheduledTasks {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd [HH:mm:ss]");

    // @Scheduled(fixedRate = 3000)
    @Scheduled(cron = "0 0 0/1 * * *")
    public void reportCurrentTime() {
        log.info("## ScheduledTasks::reportCurrentTime : " + sdf.format(new Date()));
    }
}
