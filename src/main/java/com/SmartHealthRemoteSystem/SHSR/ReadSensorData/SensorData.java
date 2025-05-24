// package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

// import com.google.cloud.Timestamp;

// import java.sql.Time;

// public class SensorData {
//     private Double ecgReading;
//     private Double bodyTemperature;
//     private Timestamp timestamp;
//     private String sensorDataId;
//     private Double oxygenReading;
//     private int Heart_Rate;//mg, ijat, keng, faruq, din


//     public SensorData() {
//     }

//     public SensorData(Double ecgReading, Double bodyTemperature, Double oxygenReading, int Heart_Rate) {//din
//         this.ecgReading = ecgReading;
//         this.bodyTemperature = bodyTemperature;
//         this.oxygenReading = oxygenReading;
//         this.Heart_Rate = Heart_Rate;//mg, ijat, keng, faruq, din

//     }

//     public SensorData(Double ecgReading, Double bodyTemperature, Timestamp timestamp, String sensorDataId, Double OxygenReading, int Heart_Rate) {//din
//         this.ecgReading = ecgReading;
//         this.bodyTemperature = bodyTemperature;
//         this.timestamp = timestamp;
//         this.sensorDataId = sensorDataId;
//         this.oxygenReading= OxygenReading;
//         this.Heart_Rate = Heart_Rate;//mg, ijat, keng, faruq, din

//     }

//     public Double getEcgReading() {
//         return ecgReading;
//     }

//     public void setEcgReading(Double ecgReading) {
//         this.ecgReading = ecgReading;
//     }

//     public Double getBodyTemperature() {
//         return bodyTemperature;
//     }

//     public void setBodyTemperature(Double bodyTemperature) {
//         this.bodyTemperature = bodyTemperature;
//     }

//     public void setOxygenReading(Double OxygenReading){this.oxygenReading=OxygenReading;}

//     public Double getOxygenReading(){return oxygenReading;}

//     public void setHeart_Rate(int Heart_Rate){this.Heart_Rate=Heart_Rate;}//mg, ijat, keng, faruq, din

//     public int getHeart_Rate(){return Heart_Rate;}//mg, ijat, keng, faruq, din

//     public Timestamp getTimestamp() {
//         return timestamp;
//     }

//     public void setTimestamp(Timestamp timestamp) {
//         this.timestamp = timestamp;
//     }

//     public String getSensorDataId() {
//         return sensorDataId;
//     }

//     public void setSensorDataId(String sensorDataId) {
//         this.sensorDataId = sensorDataId;
//     }

//     @Override
//     public String toString() {
//         return "SensorData{" +
//                 "ecgReading='" + ecgReading + '\'' +
//                 "\n timestamp=" + timestamp +
//                 "\n sensorDataId='" + sensorDataId + '\'' +
//                 '}';
//     }
// }


//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class SensorData {

    private String sensorDataId;
    private double heart_Rate;
    private double bodyTemperature;
    private double ecgReading;
    private double oxygenReading;
    private Instant timestamp;

    // NEW: History field (nested)
    private List<HistorySensorData> history = new ArrayList<>();

    // Constructors
    public SensorData() {
        this.timestamp = Instant.now();
    }

    public SensorData(String sensorDataId, double heart_Rate, double bodyTemperature,
                      double ecgReading, double oxygenReading) {
        this.sensorDataId = sensorDataId;
        this.heart_Rate = heart_Rate;
        this.bodyTemperature = bodyTemperature;
        this.ecgReading = ecgReading;
        this.oxygenReading = oxygenReading;
        this.timestamp = Instant.now();
    }

    // Getters & Setters
    public String getSensorDataId() {
        return sensorDataId;
    }

    public void setSensorDataId(String sensorDataId) {
        this.sensorDataId = sensorDataId;
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
        return history;
    }

    public void setHistory(List<HistorySensorData> history) {
        this.history = history;
    }

    public void addToHistory(HistorySensorData data) {
        this.history.add(data);
    }

    // Convert to MongoDB Document
    public Document toDocument() {
        Document doc = new Document();
        doc.append("sensorDataId", sensorDataId)
           .append("heart_Rate", heart_Rate)
           .append("bodyTemperature", bodyTemperature)
           .append("ecgReading", ecgReading)
           .append("oxygenReading", oxygenReading)
           .append("timestamp", timestamp.toString());

        List<Document> historyDocs = new ArrayList<>();
        for (HistorySensorData h : history) {
            historyDocs.add(h.toDocument());
        }
        doc.append("history", historyDocs);

        return doc;
    }

    // Convert from MongoDB Document
    public static SensorData fromDocument(Document doc) {
        SensorData sensorData = new SensorData();
        sensorData.setSensorDataId(doc.getString("sensorDataId"));
        sensorData.setHeart_Rate(doc.getDouble("heart_Rate"));
        sensorData.setBodyTemperature(doc.getDouble("bodyTemperature"));
        sensorData.setEcgReading(doc.getDouble("ecgReading"));
        sensorData.setOxygenReading(doc.getDouble("oxygenReading"));
        sensorData.setTimestamp(Instant.parse(doc.getString("timestamp")));

        List<Document> historyDocs = (List<Document>) doc.get("history");
        if (historyDocs != null) {
            List<HistorySensorData> history = new ArrayList<>();
            for (Document hDoc : historyDocs) {
                history.add(HistorySensorData.fromDocument(hDoc));
            }
            sensorData.setHistory(history);
        }

        return sensorData;
    }
}
