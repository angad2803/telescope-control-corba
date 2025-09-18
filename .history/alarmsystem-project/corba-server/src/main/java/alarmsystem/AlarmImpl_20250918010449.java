package alarmsystem;

public class AlarmImpl extends AlarmPOA {
    @Override
    public String getAction() {
        return "Trigger cooling system";
    }

    @Override
    public String getAlarmId() {
        return "ALARM-001";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String getStatus() {
        return "ACTIVE";
    }

    @Override
    public String getHelpURL() {
        return "http://docs.example.com/alarm-help";
    }
}
