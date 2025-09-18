package alarmsystem;

public class AlarmImpl extends AlarmPOA {
    
    @Override
    public String getAction() {
        return "Check telescope mount motors and restart if necessary";
    }

    @Override
    public String getAlarmId() {
        return "MOUNT_001";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getStatus() {
        return "ACTIVE";
    }

    @Override
    public String getHelpURL() {
        return "https://alma.nrao.edu/docs/telescope/mount-procedures";
    }
    
    @Override
    public String getAlarmById(String id) {
        switch (id) {
            case "MOUNT_001":
                return createAlarmData("MOUNT", "MOUNT_1", 1, 
                    "Check telescope mount motors and restart if necessary",
                    "ACTIVE", 1, "Motor controller communication failure");
            case "PS_001":
                return createAlarmData("PS", "PS_1", 100, 
                    "Check power supply voltage and reset circuit breaker",
                    "ACTIVE", 0, "Power supply over current detected");
            case "ANTENNA_001":
                return createAlarmData("ANTENNA", "ANTENNA_7", 200,
                    "Verify antenna positioning system and recalibrate",
                    "ACTIVE", 2, "Antenna positioning error detected");
            default:
                return createAlarmData("UNKNOWN", id, 999,
                    "Contact system administrator for assistance",
                    "ACTIVE", 3, "Unknown alarm identifier");
        }
    }
    
    private String createAlarmData(String faultFamily, String faultMember, int faultCode,
                                 String action, String status, int priority, String description) {
        return String.format(
            "{\"faultFamily\":\"%s\",\"faultMember\":\"%s\",\"faultCode\":%d," +
            "\"action\":\"%s\",\"status\":\"%s\",\"priority\":%d,\"description\":\"%s\"," +
            "\"helpURL\":\"https://alma.nrao.edu/docs/alarms/%s\",\"timestamp\":\"%d\"}",
            faultFamily, faultMember, faultCode, action, status, priority, description,
            faultFamily.toLowerCase(), System.currentTimeMillis()
        );
    }
}