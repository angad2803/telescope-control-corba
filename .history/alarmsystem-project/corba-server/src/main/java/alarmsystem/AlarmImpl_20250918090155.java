package alarmsystem;package alarmsystem;



public class AlarmImpl extends CERNAlarmServicePOA {public class AlarmImpl extends CERNAlarmServicePOA {

        

    // ALMA-compliant getAlarmById implementation    // ALMA-compliant getAlarmById implementation

    @Override    @Override

    public Alarm getAlarmById(String id) {    public Alarm getAlarmById(String id) {

        // Create ALMA-compliant timestamp        // Create ALMA-compliant timestamp

        Timestamp timestamp = new Timestamp();        Timestamp timestamp = new Timestamp();

        timestamp.miliseconds = System.currentTimeMillis();        timestamp.miliseconds = System.currentTimeMillis();

        timestamp.nanos = 0;        timestamp.nanos = 0;

                

        // Create ALMA-compliant status        // Create ALMA-compliant status

        Status status = new Status();        Status status = new Status();

        status.active = true;        status.active = true;

        status.masked = false;        status.masked = false;

        status.reduced = false;        status.reduced = false;

        status.sourceTimestamp = timestamp;        status.sourceTimestamp = timestamp;

        status.sourceHostname = "corba-server-001";        status.sourceHostname = "corba-server-001";

                

        // Create ALMA-compliant alarm        // Create ALMA-compliant alarm

        Alarm alarm = new Alarm();        Alarm alarm = new Alarm();

        alarm.alarmId = "CORBA-REAL-" + id;        alarm.alarmId = "CORBA-REAL-" + id;

        alarm.problemDescription = "REAL CORBA: Emergency shutdown sequence";        alarm.problemDescription = "REAL CORBA: Emergency shutdown sequence";

        alarm.priority = 1;        alarm.priority = 1;

        alarm.cause = "System temperature exceeded threshold";        alarm.cause = "System temperature exceeded threshold";

        alarm.action = "REAL CORBA: Emergency shutdown sequence";        alarm.action = "REAL CORBA: Emergency shutdown sequence";

        alarm.consequence = "System protection activated";        alarm.consequence = "System protection activated";

        alarm.helpURL = "https://corba.real.server/emergency";        alarm.helpURL = "https://corba.real.server/emergency";

        alarm.alarmStatus = status;        alarm.alarmStatus = status;

                

        return alarm;        return alarm;

    }    }

        

    // Legacy simple interface methods for backward compatibility    // Legacy simple interface methods for backward compatibility

    @Override    @Override

    public String getAction() {    public String getAction() {

        return "REAL CORBA: Emergency shutdown sequence";        return "REAL CORBA: Emergency shutdown sequence";

    }    }



    @Override    @Override

    public String getAlarmId() {    public String getAlarmId() {

        return "CORBA-REAL-999";        return "CORBA-REAL-999";

    }    }



    @Override    @Override

    public int getPriority() {    public int getPriority() {

        return 1;        return 1;

    }    }



    @Override    @Override

    public String getStatus() {    public String getStatus() {

        return "CRITICAL - FROM CORBA SERVER";        return "CRITICAL - FROM CORBA SERVER";

    }    }



    @Override    @Override

    public String getHelpURL() {    public String getHelpURL() {

        return "https://corba.real.server/emergency";        return "https://corba.real.server/emergency";

    }    }

}}
