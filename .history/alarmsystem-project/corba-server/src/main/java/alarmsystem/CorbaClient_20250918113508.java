package alarmsystem;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

import java.io.BufferedReader;
import java.io.FileReader;

public class CorbaClient {
    public static void main(String[] args) throws Exception {
        try {
            ORB orb = ORB.init(args, null);
            
            BufferedReader in = new BufferedReader(new FileReader("Alarm.ref"));
            String ior = in.readLine();
            in.close();

            Object obj = orb.string_to_object(ior);
            Alarm alarm = AlarmHelper.narrow(obj);

            if (args.length > 0 && args[0].equals("getAlarmById")) {
                String alarmId = args.length > 1 ? args[1] : "ALM001";
                String result = alarm.getAlarmById(alarmId);
                System.out.println(result);
            } else if (args.length > 0 && args[0].equals("getAction")) {
                String action = alarm.getAction();
                System.out.println(action);
            } else if (args.length > 0 && args[0].equals("getPriority")) {
                int priority = alarm.getPriority();
                System.out.println(priority);
            } else {
                // Default test - call getAlarmById with default ID
                String result = alarm.getAlarmById("ALM001");
                System.out.println(result);
            }
            
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}