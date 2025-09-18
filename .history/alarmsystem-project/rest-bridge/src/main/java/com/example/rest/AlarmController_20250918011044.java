package com.example.rest;

import alarmsystem.Alarm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
public class AlarmController {

    private final Alarm alarm = CorbaClientConfig.getAlarmService();

    @GetMapping("/alarm")
    public Map<String, Object> getAlarm() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", alarm.getAlarmId());
        result.put("action", alarm.getAction());
        result.put("priority", alarm.getPriority());
        result.put("status", alarm.getStatus());
        result.put("helpURL", alarm.getHelpURL());
        return result;
    }
}
