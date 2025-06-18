package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;

public class SensorData {

    private String sensorDataId;
    private double heart_Rate;
    private double bodyTemperature;
    private double ecgReading;
    private double oxygenReading;
    private Instant timestamp;

    // ✅ History list for all past readings
    private List<HistorySensorData> history = new ArrayList<>();

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

    // ✅ Getters & Setters
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

    // ✅ Convert to MongoDB Document (optional for repository layer)
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

    // ✅ Convert from MongoDB Document (optional for repository layer)
   public static SensorData fromDocument(Document doc) {
    SensorData sensorData = new SensorData();
    sensorData.setSensorDataId(doc.getString("sensorDataId"));
    sensorData.setHeart_Rate(doc.getDouble("heart_Rate"));
    sensorData.setBodyTemperature(doc.getDouble("bodyTemperature"));
    sensorData.setEcgReading(doc.getDouble("ecgReading"));
    sensorData.setOxygenReading(doc.getDouble("oxygenReading"));

    // ✅ FIXED: Convert Date -> Instant
    sensorData.setTimestamp(((Date) doc.get("timestamp")).toInstant());

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
