package alarmsystem;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Server {
    public static void main(String[] args) throws Exception {
        ORB orb = ORB.init(args, null);

        POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootPOA.the_POAManager().activate();

        AlarmImpl alarmImpl = new AlarmImpl();
        org.omg.CORBA.Object ref = rootPOA.servant_to_reference(alarmImpl);

        // Write reference to file
        PrintWriter out = new PrintWriter(new FileOutputStream("Alarm.ref"));
        out.println(orb.object_to_string(ref));
        out.close();

        System.out.println("Alarm CORBA server is running...");
        orb.run();
    }
}
