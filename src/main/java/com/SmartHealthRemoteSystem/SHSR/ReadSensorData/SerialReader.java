package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

import com.fazecast.jSerialComm.SerialPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.time.Instant;
import java.util.Scanner;

@Component
public class SerialReader {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        new Thread(this::readSerial).start();
    }

    private void readSerial() {
        SerialPort port = SerialPort.getCommPort("COM4"); // ✅ Update with your actual COM port
        port.setBaudRate(9600);

        if (port.openPort()) {
            System.out.println("✅ Serial port opened successfully.");
            System.out.println("📡 Listening on port COM4...");
            try (InputStream in = port.getInputStream(); Scanner scanner = new Scanner(in)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("📩 RAW LINE: " + line); // ✅ Print raw input
                    processLine(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                port.closePort();
                System.out.println("⚠ Serial port closed.");
            }
        } else {
            System.out.println("❌ Failed to open serial port.");
        }
    }

    private void processLine(String line) {
        try {
            SensorReading reading = objectMapper.readValue(line, SensorReading.class);
            System.out.println("✅ Parsed sensor reading: Temp=" + reading.temperature + ", HR=" + reading.heartRate + ", ECG=" + reading.ecgValue);

            String sensorDataId = "demoSensor01";  // ✅ For demo patient Aina12

            SensorData existingSensorData = sensorDataRepository.get(sensorDataId);

            if (existingSensorData == null) {
                SensorData newSensorData = new SensorData();
                newSensorData.setSensorDataId(sensorDataId);
                newSensorData.setHeart_Rate(reading.heartRate);
                newSensorData.setEcgReading(reading.ecgValue);
                newSensorData.setBodyTemperature(reading.temperature);
                newSensorData.setOxygenReading(0);
                newSensorData.setTimestamp(Instant.now());

                sensorDataRepository.save(newSensorData);
                System.out.println("✅ New SensorData created in MongoDB.");
            } else {
                existingSensorData.setHeart_Rate(reading.heartRate);
                existingSensorData.setEcgReading(reading.ecgValue);
                existingSensorData.setBodyTemperature(reading.temperature);
                existingSensorData.setTimestamp(Instant.now());

                sensorDataRepository.update(existingSensorData);
                System.out.println("✅ SensorData updated in MongoDB.");

                HistorySensorData historyEntry = new HistorySensorData(
                        reading.heartRate,
                        reading.temperature,
                        reading.ecgValue,
                        0
                );
                sensorDataRepository.addToHistory(sensorDataId, historyEntry);
                System.out.println("✅ History updated in MongoDB.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error parsing incoming serial data: " + line);
            e.printStackTrace();
        }
    }

    // ✅ Inner class to receive JSON-formatted serial data
    public static class SensorReading {
        public int heartRate;
        public double ecgValue;
        public double temperature;
    }
}
