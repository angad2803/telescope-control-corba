-- ALMA ACS-Compliant Alarm System Database Schema
-- Based on ALMA Common Software (ACS) 8.2 Architecture
-- Reference: https://www.eso.org/projects/alma/develop/acs/Releases/ACS_8_2/Distribution/index.html

-- Create database and user (PostgreSQL)
-- Run these commands as database admin:

CREATE DATABASE alma_alarm_system;
CREATE USER alma_alarm_user WITH PASSWORD 'alma_acs_password';
GRANT ALL PRIVILEGES ON DATABASE alma_alarm_system TO alma_alarm_user;

-- Connect to alma_alarm_system database and create tables:
\c alma_alarm_system;

-- ALMA ACS Component Registry (for CORBA component discovery)
CREATE TABLE acs_components (
    component_name VARCHAR(100) PRIMARY KEY,
    component_type VARCHAR(50) NOT NULL,
    corba_ior TEXT NOT NULL,
    container_name VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ALMA-style Alarm System Table (following ACS AlarmService patterns)
CREATE TABLE alma_alarms (
    fault_family VARCHAR(50) NOT NULL,           -- ALMA: Fault Family (e.g., 'TEMPERATURE', 'POWER')
    fault_member VARCHAR(50) NOT NULL,           -- ALMA: Fault Member (e.g., 'ANTENNA01', 'COOLING_UNIT')
    fault_code INTEGER NOT NULL,                 -- ALMA: Fault Code (numerical identifier)
    alarm_id VARCHAR(100) GENERATED ALWAYS AS (fault_family || ':' || fault_member || ':' || fault_code) STORED,
    
    -- Alarm Properties (ALMA ACS standard fields)
    priority INTEGER NOT NULL DEFAULT 3,        -- 0=INFO, 1=CRITICAL, 2=MAJOR, 3=MINOR
    description TEXT NOT NULL,
    action TEXT NOT NULL,
    cause TEXT,
    consequence TEXT,
    source_hostname VARCHAR(100),
    source_object VARCHAR(100),
    
    -- ALMA Timestamps (UTC)
    user_timestamp TIMESTAMP,                   -- When fault occurred
    system_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- When logged to system
    
    -- ALMA Properties
    location VARCHAR(100),                      -- Physical location (e.g., 'ANTENNA_PAD_A001')
    status VARCHAR(20) DEFAULT 'ACTIVE',        -- ACTIVE, MASKED, ACKNOWLEDGED, CLEARED
    is_node_parent BOOLEAN DEFAULT FALSE,       -- ALMA: Is this a parent node alarm?
    is_node_child BOOLEAN DEFAULT FALSE,        -- ALMA: Is this a child node alarm?
    
    -- Additional ALMA fields
    help_url VARCHAR(255),
    contact VARCHAR(100),                       -- Responsible person/team
    email VARCHAR(100),                         -- Contact email
    sms VARCHAR(50),                           -- SMS contact
    
    PRIMARY KEY (fault_family, fault_member, fault_code)
);

-- ALMA ACS Configuration Properties Table
CREATE TABLE acs_alarm_config (
    fault_family VARCHAR(50) NOT NULL,
    fault_member VARCHAR(50) NOT NULL,
    fault_code INTEGER NOT NULL,
    property_name VARCHAR(100) NOT NULL,
    property_value TEXT,
    PRIMARY KEY (fault_family, fault_member, fault_code, property_name),
    FOREIGN KEY (fault_family, fault_member, fault_code) 
        REFERENCES alma_alarms(fault_family, fault_member, fault_code)
);

-- ALMA ACS Component Registration
INSERT INTO acs_components (component_name, component_type, corba_ior, container_name) VALUES
('ALARMSYSTEM_CORBA_SERVER', 'AlarmService', 'IOR:PLACEHOLDER', 'DefaultContainer'),
('ALMA_ALARM_DATABASE', 'DatabaseService', 'IOR:PLACEHOLDER', 'DatabaseContainer');

-- ALMA-style Sample Alarm Data (following ALMA Fault Family structure)
INSERT INTO alma_alarms (
    fault_family, fault_member, fault_code, 
    priority, description, action, cause, consequence,
    source_hostname, source_object, location, status,
    help_url, contact, email
) VALUES
-- Antenna Temperature Alarms
('TEMPERATURE', 'ANTENNA_01', 001, 1, 'Receiver temperature above critical threshold', 
 'Activate emergency cooling system', 'Cooling system failure', 'Receiver damage risk',
 'antenna01.alma.cl', 'TemperatureSensor', 'ANTENNA_PAD_A001', 'CRITICAL',
 'https://alma.eso.org/docs/temperature-control', 'Antenna Team', 'antenna-ops@alma.cl'),

-- Power System Alarms  
('POWER', 'UPS_CENTRAL', 002, 1, 'UPS battery backup activated - main power lost',
 'Check main power feed immediately', 'Electrical grid failure', 'System shutdown in 30min',
 'control.alma.cl', 'PowerMonitor', 'CENTRAL_BUILDING', 'CRITICAL',
 'https://alma.eso.org/docs/power-systems', 'Infrastructure Team', 'power@alma.cl'),

-- Network Communication Alarms
('NETWORK', 'CORRELATOR_01', 003, 2, 'High packet loss detected on correlator network',
 'Check network infrastructure', 'Network congestion or hardware failure', 'Data loss possible',
 'correlator01.alma.cl', 'NetworkMonitor', 'CORRELATOR_BUILDING', 'MAJOR',
 'https://alma.eso.org/docs/network', 'IT Team', 'network@alma.cl'),

-- Weather Station Alarms
('WEATHER', 'METEO_STATION', 004, 3, 'Wind speed approaching operational limits',
 'Monitor conditions - prepare for shutdown', 'High altitude weather conditions', 'Observation quality degradation',
 'weather.alma.cl', 'WeatherStation', 'PLATEAU_5000M', 'MINOR',
 'https://alma.eso.org/docs/weather', 'Weather Team', 'weather@alma.cl'),

-- Test Alarm for Development
('TEST', 'DEVELOPMENT', 999, 3, 'Test alarm for CORBA system validation',
 'Run diagnostic procedures', 'System testing', 'No operational impact',
 'dev.alma.cl', 'TestSystem', 'DEVELOPMENT_LAB', 'ACTIVE',
 'https://alma.eso.org/docs/testing', 'Dev Team', 'dev@alma.cl');

-- ALMA ACS Configuration Properties
INSERT INTO acs_alarm_config (fault_family, fault_member, fault_code, property_name, property_value) VALUES
('TEMPERATURE', 'ANTENNA_01', 001, 'THRESHOLD_CRITICAL', '85.0'),
('TEMPERATURE', 'ANTENNA_01', 001, 'THRESHOLD_WARNING', '75.0'),
('POWER', 'UPS_CENTRAL', 002, 'BATTERY_RUNTIME_MINUTES', '30'),
('NETWORK', 'CORRELATOR_01', 003, 'PACKET_LOSS_THRESHOLD', '5.0'),
('WEATHER', 'METEO_STATION', 004, 'WIND_SPEED_LIMIT', '15.0');

-- Grant permissions to ALMA user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO alma_alarm_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO alma_alarm_user;