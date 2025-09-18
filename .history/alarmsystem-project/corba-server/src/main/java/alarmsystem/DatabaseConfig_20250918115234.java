package alarmsystem;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;
    
    static {
        try {
            HikariConfig config = new HikariConfig();
            
            // For MySQL (uncomment if using MySQL)
            // config.setJdbcUrl("jdbc:mysql://localhost:3306/alarm_system");
            // config.setUsername("alarm_user");
            // config.setPassword("alarm_password");
            // config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            
            // For PostgreSQL (uncomment if using PostgreSQL)
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/alarm_system");
            config.setUsername("alarm_user");
            config.setPassword("alarm_password");
            config.setDriverClassName("org.postgresql.Driver");
            
            // For SQLite (lightweight option)
            // config.setJdbcUrl("jdbc:sqlite:alarm_system.db");
            // config.setDriverClassName("org.sqlite.JDBC");
            
            // Connection pool settings
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            dataSource = new HikariDataSource(config);
            
            System.out.println("Database connection pool initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }
        return dataSource.getConnection();
    }
    
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }
}