package alarmsystem;

public class AlarmImpl extends AlarmPOA {
    
    @Override
    public String getAction() {
        return "CORBA-ACTION: Emergency shutdown sequence initiated";
    }

    @Override
    public String getAlarmId() {
        return "CORBA-ID-999";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getStatus() {
        return "CORBA-STATUS: CRITICAL - System requires immediate attention";
    }

    @Override
    public String getHelpURL() {
        return "https://corba.enterprise.system/emergency-procedures";
    }
    
    // ALMA-style getAlarmById method
    @Override
    public String getAlarmById(String id) {
        return "CORBA-ALARM-" + id + "-ENTERPRISE-READY";
    }
}