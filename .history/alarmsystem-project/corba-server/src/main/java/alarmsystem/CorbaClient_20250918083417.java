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
            
            // Narrow to Alarm interface
            Alarm alarm = AlarmHelper.narrow(obj);
            
            // Call methods and output JSON
            System.out.println("{");
            System.out.println("  \"id\": \"" + alarm.getAlarmId() + "\",");
            System.out.println("  \"action\": \"" + alarm.getAction() + "\",");
            System.out.println("  \"priority\": " + alarm.getPriority() + ",");
            System.out.println("  \"status\": \"" + alarm.getStatus() + "\",");
            System.out.println("  \"helpURL\": \"" + alarm.getHelpURL() + "\"");
            System.out.println("}");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}