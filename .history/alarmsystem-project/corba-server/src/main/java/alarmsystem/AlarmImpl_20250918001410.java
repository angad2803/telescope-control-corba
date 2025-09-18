package alarmsystem;

// Implementation of the Alarm interface
public class AlarmImpl extends AlarmPOA {
    @Override
    public void triggerAlarm(String message) {
        System.out.println("Alarm triggered: " + message);
    }
}
