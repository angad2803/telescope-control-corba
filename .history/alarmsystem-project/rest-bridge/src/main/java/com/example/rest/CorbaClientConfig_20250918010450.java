package com.example.rest;

import alarmsystem.Alarm;
import org.omg.CORBA.ORB;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CorbaClientConfig {
    private static Alarm alarm;

    public static Alarm getAlarmService() {
        if (alarm == null) {
            try {
                ORB orb = ORB.init(new String[]{}, null);
                String ior = new String(Files.readAllBytes(Paths.get("Alarm.ref")));
                org.omg.CORBA.Object obj = orb.string_to_object(ior);
                alarm = alarmsystem.AlarmHelper.narrow(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alarm;
    }
}
