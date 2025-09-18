package com.example.rest;

import alarmsystem.Alarm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AlarmController {

    private final Alarm alarm = CorbaClientConfig.getAlarmService();

    @GetMapping("/alarm")
    public Map<String, Object> getAlarm() {
        return Map.of(
                "id", alarm.getAlarmId(),
                "action", alarm.getAction(),
                "priority", alarm.getPriority(),
                "status", alarm.getStatus(),
                "helpURL", alarm.getHelpURL()
        );
    }
}
