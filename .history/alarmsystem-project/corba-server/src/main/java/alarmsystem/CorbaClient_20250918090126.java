package alarmsystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CorbaClient {
    public static void main(String[] args) {
        try {
            // Initialize ORB
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);
            
            // Read the IOR from file
            String ior = new String(Files.readAllBytes(Paths.get("Alarm.ref"))).trim();
            
            // Convert IOR to object reference
            org.omg.CORBA.Object obj = orb.string_to_object(ior);
            
            // Narrow to ALMA-compliant CERNAlarmService interface
            CERNAlarmService alarmService = CERNAlarmServiceHelper.narrow(obj);
            
            // Use ALMA-compliant getAlarmById method
            Alarm alarm = alarmService.getAlarmById("999");
            
            // Output JSON using ALMA-compliant structure
            System.out.println("{");
            System.out.println("  \"id\": \"" + alarm.alarmId + "\",");
            System.out.println("  \"action\": \"" + alarm.action + "\",");
            System.out.println("  \"priority\": " + alarm.priority + ",");
            System.out.println("  \"status\": \"" + (alarm.alarmStatus.active ? "ACTIVE" : "INACTIVE") + "\",");
            System.out.println("  \"helpURL\": \"" + alarm.helpURL + "\",");
            System.out.println("  \"problemDescription\": \"" + alarm.problemDescription + "\",");
            System.out.println("  \"sourceHostname\": \"" + alarm.alarmStatus.sourceHostname + "\"");
            System.out.println("}");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}