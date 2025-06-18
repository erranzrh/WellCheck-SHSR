package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

import com.SmartHealthRemoteSystem.SHSR.updateStatusAppointment.Service.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SensorDataRepository {

    private static final String COLLECTION_NAME = "SensorData";

    // ✅ Get sensor data by ID
    public SensorData get(String sensorDataId) {
    MongoDatabase db = MongoDBConnection.connect();
    try {
        Document doc = db.getCollection(COLLECTION_NAME)
                .find(new Document("sensorDataId", sensorDataId))
                .first();

        if (doc != null) {
            System.out.println("✅ Sensor data document found: " + doc.toJson());
            return SensorData.fromDocument(doc);
        } else {
            System.out.println("❌ No document found for sensorDataId: " + sensorDataId);
            return null;
        }
    } finally {
        MongoDBConnection.close();
    }
}


    // ✅ Get all sensors (for admin/debug)
    public List<SensorData> getAll() {
        MongoDatabase db = MongoDBConnection.connect();
        List<SensorData> list = new ArrayList<>();
        try {
            for (Document doc : db.getCollection(COLLECTION_NAME).find()) {
                list.add(SensorData.fromDocument(doc));
            }
        } finally {
            MongoDBConnection.close();
        }
        return list;
    }

    // ✅ Save new sensor data (initial creation)
    public String save(SensorData sensorData) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            sensorData.setTimestamp(Instant.now());
            db.getCollection(COLLECTION_NAME).insertOne(sensorData.toDocument());
            return sensorData.getSensorDataId();
        } finally {
            MongoDBConnection.close();
        }
    }

    // ✅ Update real-time sensor data (overwrite latest)
    public String update(SensorData sensorData) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            Document updateDoc = new Document();
            updateDoc.append("heart_Rate", sensorData.getHeart_Rate());
            updateDoc.append("bodyTemperature", sensorData.getBodyTemperature());
            updateDoc.append("ecgReading", sensorData.getEcgReading());
            updateDoc.append("oxygenReading", sensorData.getOxygenReading());
            updateDoc.append("timestamp", Instant.now());

            db.getCollection(COLLECTION_NAME)
              .updateOne(new Document("sensorDataId", sensorData.getSensorDataId()),
                         new Document("$set", updateDoc));

            return "Updated successfully.";
        } finally {
            MongoDBConnection.close();
        }
    }

    // ✅ Add nested history reading into history array
    public boolean addToHistory(String sensorDataId, HistorySensorData newEntry) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            Document historyDoc = newEntry.toDocument();
            db.getCollection(COLLECTION_NAME).updateOne(
                Filters.eq("sensorDataId", sensorDataId),
                Updates.push("history", historyDoc)
            );
            return true;
        } finally {
            MongoDBConnection.close();
        }
    }

    // ✅ Delete sensor (optional)
    public String delete(String sensorDataId) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            db.getCollection(COLLECTION_NAME)
              .deleteOne(new Document("sensorDataId", sensorDataId));
            return "Deleted sensorDataId: " + sensorDataId;
        } finally {
            MongoDBConnection.close();
        }
    }
}
