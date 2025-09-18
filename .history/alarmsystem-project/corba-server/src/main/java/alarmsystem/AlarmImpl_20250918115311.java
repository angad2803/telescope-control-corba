package alarmsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlarmImpl extends AlarmPOA {
    
    @Override
    public String getAction() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT action FROM alarms WHERE status = 'CRITICAL' ORDER BY priority ASC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "DB-ACTION: " + rs.getString("action");
            }
            return "DB-ACTION: No critical alarms found";
            
        } catch (SQLException e) {
            System.err.println("Database error in getAction: " + e.getMessage());
            return "DB-ERROR: Unable to retrieve action - " + e.getMessage();
        }
    }

    @Override
    public String getAlarmId() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT id FROM alarms ORDER BY priority ASC, created_at DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "DB-ID: " + rs.getString("id");
            }
            return "DB-ID: No alarms found";
            
        } catch (SQLException e) {
            System.err.println("Database error in getAlarmId: " + e.getMessage());
            return "DB-ERROR: " + e.getMessage();
        }
    }

    @Override
    public int getPriority() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT AVG(priority)::INTEGER as avg_priority FROM alarms WHERE status IN ('CRITICAL', 'ACTIVE')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("avg_priority");
            }
            return 5; // Default priority
            
        } catch (SQLException e) {
            System.err.println("Database error in getPriority: " + e.getMessage());
            return -1; // Error indicator
        }
    }

    @Override
    public String getStatus() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT COUNT(*) as critical_count FROM alarms WHERE status = 'CRITICAL'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int criticalCount = rs.getInt("critical_count");
                if (criticalCount > 0) {
                    return "DB-STATUS: " + criticalCount + " CRITICAL alarms active";
                } else {
                    return "DB-STATUS: System operational - no critical alarms";
                }
            }
            return "DB-STATUS: Unable to determine status";
            
        } catch (SQLException e) {
            System.err.println("Database error in getStatus: " + e.getMessage());
            return "DB-ERROR: " + e.getMessage();
        }
    }

    @Override
    public String getHelpURL() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT help_url FROM alarms WHERE status = 'CRITICAL' AND help_url IS NOT NULL ORDER BY priority ASC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("help_url");
            }
            return "https://docs.example.com/general-help";
            
        } catch (SQLException e) {
            System.err.println("Database error in getHelpURL: " + e.getMessage());
            return "https://docs.example.com/error-help";
        }
    }
    
    // ALMA-style getAlarmById method
    @Override
    public String getAlarmById(String id) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT id, description, action, priority, status, help_url FROM alarms WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Return JSON-like string with alarm data
                return String.format(
                    "DB-ALARM: {id:'%s', desc:'%s', action:'%s', priority:%d, status:'%s', help:'%s'}",
                    rs.getString("id"),
                    rs.getString("description"),
                    rs.getString("action"),
                    rs.getInt("priority"),
                    rs.getString("status"),
                    rs.getString("help_url")
                );
            }
            return "DB-ALARM: Alarm ID '" + id + "' not found in database";
            
        } catch (SQLException e) {
            System.err.println("Database error in getAlarmById: " + e.getMessage());
            return "DB-ERROR: Failed to retrieve alarm '" + id + "' - " + e.getMessage();
        }
    }
}