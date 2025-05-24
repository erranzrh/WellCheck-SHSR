// package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

// import com.google.cloud.Timestamp;

// public class HistorySensorData {
//     private Double ecgReading;
//     private Double bodyTemperature;
//     private Timestamp timestamp;
//     private String sensorDataId;
//     private Double oxygenReading;
//     private int Heart_Rate;


//     public HistorySensorData() {
//     }

//     public HistorySensorData( int Heart_Rate, Double bodyTemperature,Double ecgReading,Double OxygenReading, String sensorDataId, Timestamp timestamp) {//din
//         this.ecgReading = ecgReading;
//         this.bodyTemperature = bodyTemperature;
//         this.timestamp = timestamp;
//         this.sensorDataId = sensorDataId;
//         this.oxygenReading= OxygenReading;
//         this.Heart_Rate = Heart_Rate;

//     }

//     public void SetHistorySensorData( int Heart_Rate, Double bodyTemperature,Double ecgReading,Double OxygenReading, String sensorDataId, Timestamp timestamp) {//din
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
// }


//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

import java.time.Instant;
import org.bson.Document;

public class HistorySensorData {

    private double heart_Rate;
    private double bodyTemperature;
    private double ecgReading;
    private double oxygenReading;
    private Instant timestamp;

    public HistorySensorData() {
        this.timestamp = Instant.now();
    }

    public HistorySensorData(double heart_Rate, double bodyTemperature, double ecgReading,
                             double oxygenReading) {
        this.heart_Rate = heart_Rate;
        this.bodyTemperature = bodyTemperature;
        this.ecgReading = ecgReading;
        this.oxygenReading = oxygenReading;
        this.timestamp = Instant.now();
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

    public Document toDocument() {
        return new Document("heart_Rate", heart_Rate)
                .append("bodyTemperature", bodyTemperature)
                .append("ecgReading", ecgReading)
                .append("oxygenReading", oxygenReading)
                .append("timestamp", timestamp.toString());
    }

    public static HistorySensorData fromDocument(Document doc) {
        HistorySensorData h = new HistorySensorData();
        h.setHeart_Rate(doc.getDouble("heart_Rate"));
        h.setBodyTemperature(doc.getDouble("bodyTemperature"));
        h.setEcgReading(doc.getDouble("ecgReading"));
        h.setOxygenReading(doc.getDouble("oxygenReading"));
        h.setTimestamp(Instant.parse(doc.getString("timestamp")));
        return h;
    }
}
