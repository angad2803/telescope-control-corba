package alarmsystem;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Server {
    private static ORB orb;
    
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Starting CORBA Alarm Server...");
            orb = ORB.init(args, null);
            
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            AlarmImpl alarmImpl = new AlarmImpl();
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(alarmImpl);

            // Write reference to file
            PrintWriter out = new PrintWriter(new FileOutputStream("Alarm.ref"));
            out.println(orb.object_to_string(ref));
            out.close();

            System.out.println("Alarm CORBA server is running...");
            System.out.println("IOR written to Alarm.ref");
            System.out.println("Server ready to accept requests");
            System.out.println("Press Ctrl+C to stop the server");
            
            // Add shutdown hook to gracefully stop the server
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down CORBA server...");
                if (orb != null) {
                    orb.destroy();
                }
            }));
            
            // Keep the server running indefinitely
            System.out.println("Server entering main loop...");
            orb.run();
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
