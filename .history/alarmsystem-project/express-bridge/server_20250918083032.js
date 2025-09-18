const express = require('express');
const cors = require('cors');
const fs = require('fs');
const { spawn } = require('child_process');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Mock alarm data (since we can't directly call CORBA from Node.js easily)
const mockAlarmData = {
    id: "ALARM-001",
    action: "Trigger cooling system", 
    priority: 5,
    status: "ACTIVE",
    helpURL: "http://docs.example.com/alarm-help"
};

// Alternative: Call Java CORBA client as a subprocess
function getAlarmDataFromCorba() {
    return new Promise((resolve, reject) => {
        // For now, return mock data
        // In a real implementation, you'd call a Java process or use JNI
        resolve(mockAlarmData);
    });
}

// REST endpoint
app.get('/alarm', async (req, res) => {
    try {
        const alarmData = await getAlarmDataFromCorba();
        res.json(alarmData);
    } catch (error) {
        console.error('Error getting alarm data:', error);
        res.status(500).json({ error: 'Failed to get alarm data' });
    }
});

// Health check endpoint
app.get('/health', (req, res) => {
    res.json({ status: 'OK', service: 'Alarm Express Bridge' });
});

app.listen(PORT, () => {
    console.log(`Express Alarm Bridge running on port ${PORT}`);
    console.log(`Alarm endpoint: http://localhost:${PORT}/alarm`);
});