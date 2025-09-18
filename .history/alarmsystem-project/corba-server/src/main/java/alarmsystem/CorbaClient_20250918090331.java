package alarmsystem;package alarmsystem;package alarmsystem;



import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;import java.io.IOException;import java.io.IOException;



public class CorbaClient {import java.nio.file.Files;import java.nio.file.Files;

    public static void main(String[] args) {

        try {import java.nio.file.Paths;import java.nio.file.Paths;

            // Initialize ORB

            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

            

            // Read the IOR from filepublic class CorbaClient {public class CorbaClient {

            String ior = new String(Files.readAllBytes(Paths.get("Alarm.ref"))).trim();

                public static void main(String[] args) {    public static void main(String[] args) {

            // Convert IOR to object reference

            org.omg.CORBA.Object obj = orb.string_to_object(ior);        try {        try {

            

            // Narrow to CERNAlarmService (ALMA-style)            // Initialize ORB            // Initialize ORB

            CERNAlarmService alarmService = CERNAlarmServiceHelper.narrow(obj);

                        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

            // Call methods and output JSON

            System.out.println("{");                        

            System.out.println("  \"id\": \"" + alarmService.getAlarmById("999") + "\",");

            System.out.println("  \"action\": \"" + alarmService.getAction() + "\",");            // Read the IOR from file            // Read the IOR from file

            System.out.println("  \"priority\": " + alarmService.getPriority() + ",");

            System.out.println("  \"status\": \"" + alarmService.getStatus() + "\",");            String ior = new String(Files.readAllBytes(Paths.get("Alarm.ref"))).trim();            String ior = new String(Files.readAllBytes(Paths.get("Alarm.ref"))).trim();

            System.out.println("  \"helpURL\": \"" + alarmService.getHelpURL() + "\"");

            System.out.println("}");                        

            

        } catch (Exception e) {            // Convert IOR to object reference            // Convert IOR to object reference

            System.err.println("Error: " + e.getMessage());

            System.exit(1);            org.omg.CORBA.Object obj = orb.string_to_object(ior);            org.omg.CORBA.Object obj = orb.string_to_object(ior);

        }

    }                        

}
            // Narrow to ALMA-compliant CERNAlarmService interface            // Narrow to ALMA-compliant CERNAlarmService interface

            CERNAlarmService alarmService = CERNAlarmServiceHelper.narrow(obj);            CERNAlarmService alarmService = CERNAlarmServiceHelper.narrow(obj);

                        

            // Use ALMA-compliant getAlarmById method            // Use ALMA-compliant getAlarmById method

            Alarm alarm = alarmService.getAlarmById("999");            Alarm alarm = alarmService.getAlarmById("999");

                        

            // Output JSON using ALMA-compliant structure            // Output JSON using ALMA-compliant structure

            System.out.println("{");            System.out.println("{");

            System.out.println("  \"id\": \"" + alarm.alarmId + "\",");            System.out.println("  \"id\": \"" + alarm.alarmId + "\",");

            System.out.println("  \"action\": \"" + alarm.action + "\",");            System.out.println("  \"action\": \"" + alarm.action + "\",");

            System.out.println("  \"priority\": " + alarm.priority + ",");            System.out.println("  \"priority\": " + alarm.priority + ",");

            System.out.println("  \"status\": \"" + (alarm.alarmStatus.active ? "ACTIVE" : "INACTIVE") + "\",");            System.out.println("  \"status\": \"" + (alarm.alarmStatus.active ? "ACTIVE" : "INACTIVE") + "\",");

            System.out.println("  \"helpURL\": \"" + alarm.helpURL + "\",");            System.out.println("  \"helpURL\": \"" + alarm.helpURL + "\",");

            System.out.println("  \"problemDescription\": \"" + alarm.problemDescription + "\",");            System.out.println("  \"problemDescription\": \"" + alarm.problemDescription + "\",");

            System.out.println("  \"sourceHostname\": \"" + alarm.alarmStatus.sourceHostname + "\"");            System.out.println("  \"sourceHostname\": \"" + alarm.alarmStatus.sourceHostname + "\"");

            System.out.println("}");            System.out.println("}");

                        

        } catch (Exception e) {        } catch (Exception e) {

            System.err.println("Error: " + e.getMessage());            System.err.println("Error: " + e.getMessage());

            System.exit(1);            System.exit(1);

        }        }

    }    }

}}