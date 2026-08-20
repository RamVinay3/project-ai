package com.example.aidemo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTools {
    @Tool(description = "to get the current time ")
    public String getCurrentDateTime(){
        System.out.println("hi i called you");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "set alarm for a given time.provided in ISO-8601 format")
    public void setAlarm(String time){
        System.out.println("setAlarm was called to set alarm");
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("Alarm set for " + alarmTime);
    }
}
