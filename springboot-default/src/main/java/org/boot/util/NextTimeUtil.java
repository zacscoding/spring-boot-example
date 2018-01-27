package org.boot.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
public class NextTimeUtil {
    public long nextTime() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek > 0 && dayOfWeek < 7) {
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if(hour < 18) {
                return TimeUnit.MINUTES.toMillis(30);
            }
            else {
                return TimeUnit.HOURS.toMillis(1);
            }
        }
        else {
            return TimeUnit.HOURS.toMillis(2);
        }
    }
}