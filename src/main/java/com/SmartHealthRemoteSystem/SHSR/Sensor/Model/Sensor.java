// package com.SmartHealthRemoteSystem.SHSR.Sensor.Model;

// import java.time.Instant;
// import org.bson.Document;

// public class Sensor {
//     private String sensorDataId;
//     private double Heart_Rate;
//     private double bodyTemperature;
//     private double ecgReading;
//     private double oxygenReading;
//     private String timestamp;

//     // Default constructor
//     public Sensor() {
//         this.timestamp = Instant.now().toString();
//     }

//     // Full constructor
//     public Sensor(String sensorDataId, double Heart_Rate, double bodyTemperature, 
//                  double ecgReading, double oxygenReading) {
//         this.sensorDataId = sensorDataId;
//         this.Heart_Rate = Heart_Rate;
//         this.bodyTemperature = bodyTemperature;
//         this.ecgReading = ecgReading;
//         this.oxygenReading = oxygenReading;
//         this.timestamp = Instant.now().toString();
//     }

//     // Convenience constructor from MongoDB Document
//     public static Sensor fromDocument(Document doc) {
//         Sensor sensor = new Sensor();
//         sensor.setSensorDataId(doc.getString("sensorDataId"));
//         sensor.setHeart_Rate(doc.getDouble("Heart_Rate"));
//         sensor.setBodyTemperature(doc.getDouble("bodyTemperature"));
//         sensor.setEcgReading(doc.getDouble("ecgReading"));
//         sensor.setOxygenReading(doc.getDouble("oxygenReading"));
//         sensor.setTimestamp(doc.getString("timestamp"));
//         return sensor;
//     }

//     // Convert to MongoDB Document
//     public Document toDocument() {
//         return new Document()
//             .append("sensorDataId", sensorDataId)
//             .append("Heart_Rate", Heart_Rate)
//             .append("bodyTemperature", bodyTemperature)
//             .append("ecgReading", ecgReading)
//             .append("oxygenReading", oxygenReading)
//             .append("timestamp", timestamp);
//     }

//     // Getters and Setters
//     public String getSensorDataId() {
//         return sensorDataId;
//     }

//     public void setSensorDataId(String sensorDataId) {
//         this.sensorDataId = sensorDataId;
//     }

//     public double getHeart_Rate() {
//         return Heart_Rate;
//     }

//     public void setHeart_Rate(double Heart_Rate) {
//         this.Heart_Rate = Heart_Rate;
//     }

//     public double getBodyTemperature() {
//         return bodyTemperature;
//     }

//     public void setBodyTemperature(double bodyTemperature) {
//         this.bodyTemperature = bodyTemperature;
//     }

//     public double getEcgReading() {
//         return ecgReading;
//     }

//     public void setEcgReading(double ecgReading) {
//         this.ecgReading = ecgReading;
//     }

//     public double getOxygenReading() {
//         return oxygenReading;
//     }

//     public void setOxygenReading(double oxygenReading) {
//         this.oxygenReading = oxygenReading;
//     }

//     public String getTimestamp() {
//         return timestamp;
//     }

//     public void setTimestamp(String timestamp) {
//         this.timestamp = timestamp;
//     }
// }

//MongoDB//

package com.SmartHealthRemoteSystem.SHSR.Sensor.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.HistorySensorData;

import java.time.Instant;
import java.util.List;

@Document(collection = "SensorData")
public class Sensor {

    private String sensorDataId;
    private String uniqueKey;
    private double heart_Rate;
    private double bodyTemperature;
    private double ecgReading;
    private double oxygenReading;
    private Instant timestamp;

    public Sensor() {
        this.timestamp = Instant.now();
    }

    public Sensor(String sensorDataId, String uniqueKey, double heart_Rate,
                  double bodyTemperature, double ecgReading, double oxygenReading) {
        this.sensorDataId = sensorDataId;
        this.uniqueKey = uniqueKey;
        this.heart_Rate = heart_Rate;
        this.bodyTemperature = bodyTemperature;
        this.ecgReading = ecgReading;
        this.oxygenReading = oxygenReading;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public String getSensorDataId() {
        return sensorDataId;
    }

    public void setSensorDataId(String sensorDataId) {
        this.sensorDataId = sensorDataId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public double getHeart_Rate() {
        return heart_Rate;
    }

    public void setHeart_Rate(double heart_Rate) {
        this.heart_Rate = heart_Rate;
    }

    public double getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(double bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public double getEcgReading() {
        return ecgReading;
    }

    public void setEcgReading(double ecgReading) {
        this.ecgReading = ecgReading;
    }

    public double getOxygenReading() {
        return oxygenReading;
    }

    public void setOxygenReading(double oxygenReading) {
        this.oxygenReading = oxygenReading;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<HistorySensorData> getHistory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHistory'");
    }
}
