const express = require("express");
const cors = require("cors");
const fs = require("fs");
const { spawn } = require("child_process");

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
  helpURL: "http://docs.example.com/alarm-help",
};

// Alternative: Call Java CORBA client as a subprocess
function getAlarmDataFromCorba() {
  return new Promise((resolve, reject) => {
    const path = require("path");
    const javaClasspath = path.join(
      __dirname,
      "..",
      "corba-server",
      "target",
      "classes"
    );
    const workingDir = path.join(__dirname, "..", "corba-server");

    const javaProcess = spawn(
      "java",
      ["-cp", javaClasspath, "alarmsystem.CorbaClient"],
      {
        cwd: workingDir,
        stdio: ["pipe", "pipe", "pipe"],
      }
    );

    let output = "";
    let errorOutput = "";

    javaProcess.stdout.on("data", (data) => {
      output += data.toString();
    });

    javaProcess.stderr.on("data", (data) => {
      errorOutput += data.toString();
    });

    javaProcess.on("close", (code) => {
      if (code === 0) {
        // CORBA returns a string, so format it as JSON
        const alarmString = output.trim();
        const alarmData = {
          id: "ALM001",
          description: alarmString,
          action: "Check system status",
          priority: 5,
          status: "ACTIVE",
          timestamp: new Date().toISOString(),
          source: "CORBA-Server",
        };
        resolve(alarmData);
      } else {
        console.error("Java process failed:", errorOutput);
        reject(new Error(`CORBA connection failed: ${errorOutput}`));
      }
    });
  });
}

// Function to call specific CORBA methods
function callCorbaMethod(method, param = null) {
  return new Promise((resolve, reject) => {
    const path = require("path");
    const javaClasspath = path.join(
      __dirname,
      "..",
      "corba-server",
      "target",
      "classes"
    );
    const workingDir = path.join(__dirname, "..", "corba-server");

    const args = ["-cp", javaClasspath, "alarmsystem.CorbaClient", method];
    if (param) args.push(param);

    const javaProcess = spawn("java", args, {
      cwd: workingDir,
      stdio: ["pipe", "pipe", "pipe"],
    });

    let output = "";
    let errorOutput = "";

    javaProcess.stdout.on("data", (data) => {
      output += data.toString();
    });

    javaProcess.stderr.on("data", (data) => {
      errorOutput += data.toString();
    });

    javaProcess.on("close", (code) => {
      if (code === 0) {
        resolve(output.trim());
      } else {
        console.error("Java process failed:", errorOutput);
        reject(new Error(`CORBA connection failed: ${errorOutput}`));
      }
    });
  });
}

// REST endpoints
app.get("/alarm", async (req, res) => {
  try {
    const alarmData = await getAlarmDataFromCorba();
    res.json(alarmData);
  } catch (error) {
    console.error("Error getting alarm data:", error);
    res.status(500).json({ error: "Failed to get alarm data" });
  }
});

// Get alarm by specific ID
app.get("/alarm/:id", async (req, res) => {
  try {
    const alarmId = req.params.id;
    const result = await callCorbaMethod("getAlarmById", alarmId);
    res.json({
      id: alarmId,
      description: result,
      source: "CORBA-Server",
      method: "getAlarmById",
      timestamp: new Date().toISOString(),
    });
  } catch (error) {
    console.error("Error getting alarm by ID:", error);
    res.status(500).json({ error: "Failed to get alarm by ID" });
  }
});

// Get alarm action
app.get("/alarm/action", async (req, res) => {
  try {
    const action = await callCorbaMethod("getAction");
    res.json({
      action: action,
      source: "CORBA-Server",
      method: "getAction",
      timestamp: new Date().toISOString(),
    });
  } catch (error) {
    console.error("Error getting alarm action:", error);
    res.status(500).json({ error: "Failed to get alarm action" });
  }
});

// Get alarm priority
app.get("/alarm/priority", async (req, res) => {
  try {
    const priority = await callCorbaMethod("getPriority");
    res.json({
      priority: parseInt(priority),
      source: "CORBA-Server",
      method: "getPriority",
      timestamp: new Date().toISOString(),
    });
  } catch (error) {
    console.error("Error getting alarm priority:", error);
    res.status(500).json({ error: "Failed to get alarm priority" });
  }
});

// Health check endpoint
app.get("/health", (req, res) => {
  res.json({ status: "OK", service: "Alarm Express Bridge" });
});

app.listen(PORT, () => {
  console.log(`Express Alarm Bridge running on port ${PORT}`);
  console.log(`Alarm endpoint: http://localhost:${PORT}/alarm`);
});
