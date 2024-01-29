package net.quizz.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    public static String displayDuration(long seconds) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");

            LocalTime time = LocalTime.MIN.plusSeconds(seconds);
            return formatter.format(time);
            
    }
	
}
