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
        SerialPort port = SerialPort.getCommPort("COM4"); // Change to your actual COM port
        port.setBaudRate(9600);

        if (port.openPort()) {
            System.out.println("Serial port opened.");
            try (InputStream in = port.getInputStream(); Scanner scanner = new Scanner(in)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    processLine(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                port.closePort();
                System.out.println("Serial port closed.");
            }
        } else {
            System.out.println("Failed to open serial port.");
        }
    }

    private void processLine(String line) {
        try {
            SensorReading reading = objectMapper.readValue(line, SensorReading.class);
            System.out.println("Received: HR=" + reading.heartRate + " ECG=" + reading.ecgValue);

            SensorData sensorData = new SensorData();
            sensorData.setSensorDataId("SENSOR001"); // You can link with registered sensor
            sensorData.setHeart_Rate(reading.heartRate);
            sensorData.setEcgReading(reading.ecgValue);
            sensorData.setTimestamp(Instant.now());
            sensorData.setBodyTemperature(0); // optional
            sensorData.setOxygenReading(0); // optional

            sensorDataRepository.save(sensorData);

        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
        }
    }

    // Inner class to map JSON
    public static class SensorReading {
        public int heartRate;
        public double ecgValue;
    }
}
