package alarmsystem;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ALMA ACS-Compliant Database Configuration
 * Based on ALMA Common Software (ACS) 8.2 Architecture
 * Reference: https://www.eso.org/projects/alma/develop/acs/Releases/ACS_8_2/Distribution/index.html
 */
public class DatabaseConfig {
    private static HikariDataSource dataSource;
    
    static {
        try {
            HikariConfig config = new HikariConfig();
            
            // ALMA ACS Database Configuration
            // For PostgreSQL (ALMA standard database)
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/alma_alarm_system");
            config.setUsername("alma_alarm_user");
            config.setPassword("alma_acs_password");
            config.setDriverClassName("org.postgresql.Driver");
            
            // ALMA ACS Connection Pool Settings (enterprise-grade)
            config.setMaximumPoolSize(50);          // ALMA: Support multiple containers
            config.setMinimumIdle(10);              // ALMA: Keep connections ready
            config.setConnectionTimeout(30000);     // 30 seconds
            config.setIdleTimeout(300000);          // 5 minutes
            config.setMaxLifetime(1800000);         // 30 minutes
            config.setLeakDetectionThreshold(60000); // 1 minute - ALMA monitoring
            
            // ALMA ACS Pool Name for monitoring
            config.setPoolName("ALMA-ACS-AlarmService-Pool");
            
            // ALMA Database Validation (health checks)
            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(5000);
            
            dataSource = new HikariDataSource(config);
            
            System.out.println("ALMA ACS Database connection pool initialized successfully");
            System.out.println("Pool Name: " + config.getPoolName());
            System.out.println("Max Pool Size: " + config.getMaximumPoolSize());
            
        } catch (Exception e) {
            System.err.println("Failed to initialize ALMA ACS database connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get database connection for ALMA ACS AlarmService
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("ALMA ACS DataSource is not initialized");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close ALMA ACS database connection pool
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("ALMA ACS Database connection pool closed");
        }
    }
    
    /**
     * Get pool statistics for ALMA ACS monitoring
     * @return Pool status information
     */
    public static String getPoolStatus() {
        if (dataSource != null) {
            return String.format(
                "ALMA ACS Pool Status - Active: %d, Idle: %d, Waiting: %d, Total: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection(),
                dataSource.getHikariPoolMXBean().getTotalConnections()
            );
        }
        return "ALMA ACS Pool: Not initialized";
    }
}