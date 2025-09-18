package alarmsystem;

public class AlarmImpl extends AlarmPOA {
    @Override
    public String getAction() {
        return "REAL CORBA: Emergency shutdown sequence";
    }

    @Override
    public String getAlarmId() {
        return "CORBA-REAL-999";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getStatus() {
        return "CRITICAL - FROM CORBA SERVER";
    }

    @Override
    public String getHelpURL() {
        return "https://corba.real.server/emergency";
    }
}
