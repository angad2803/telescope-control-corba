-- Create database and user (PostgreSQL)
-- Run these commands as database admin:

CREATE DATABASE alarm_system;
CREATE USER alarm_user WITH PASSWORD 'alarm_password';
GRANT ALL PRIVILEGES ON DATABASE alarm_system TO alarm_user;

-- Connect to alarm_system database and create tables:
\c alarm_system;

-- Alarms table
CREATE TABLE alarms (
    id VARCHAR(50) PRIMARY KEY,
    description TEXT NOT NULL,
    action TEXT NOT NULL,
    priority INTEGER NOT NULL DEFAULT 5,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    help_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data
INSERT INTO alarms (id, description, action, priority, status, help_url) VALUES
('ALM001', 'Temperature sensor failure in cooling system', 'Check cooling system immediately', 1, 'CRITICAL', 'https://docs.example.com/cooling-system'),
('ALM002', 'Network connectivity issue detected', 'Restart network interface', 3, 'ACTIVE', 'https://docs.example.com/network'),
('ALM003', 'Disk space running low on server', 'Clean up temporary files', 4, 'WARNING', 'https://docs.example.com/disk-space'),
('CRITICAL001', 'Power supply voltage out of range', 'Emergency power system activation', 1, 'CRITICAL', 'https://docs.example.com/emergency-power'),
('TEST123', 'Test alarm for system validation', 'Run diagnostic tests', 5, 'ACTIVE', 'https://docs.example.com/testing');

-- Grant permissions to user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO alarm_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO alarm_user;