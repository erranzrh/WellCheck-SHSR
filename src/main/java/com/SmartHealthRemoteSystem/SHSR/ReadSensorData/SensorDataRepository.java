// package com.SmartHealthRemoteSystem.SHSR.ReadSensorData;

// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
// import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
// import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;
// import com.SmartHealthRemoteSystem.SHSR.User.User;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.Timestamp;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.WriteResult;
// import com.google.firebase.cloud.FirestoreClient;
// import com.google.firebase.database.*;
// import lombok.SneakyThrows;
// import org.springframework.stereotype.Repository;

// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.List;
// import java.util.concurrent.ExecutionException;

// @Repository
// public class SensorDataRepository implements SHSRDAO<SensorData> {
//     private final String COL_NAME = "SensorData";

//     @Override
//     public SensorData get(String sensorDataId) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(sensorDataId);
//         ApiFuture<DocumentSnapshot> future = documentReference.get();
//         DocumentSnapshot document = future.get();
//         SensorData tempSensorData;
//         if (document.exists()) {
//             tempSensorData = document.toObject(SensorData.class);
//             return tempSensorData;
//         } else {
//             return null;
//         }
//     }

//     @Override
//     public List<SensorData> getAll() throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).document("").collection("SensorDataHistory").listDocuments();
//         Iterator<DocumentReference> iterator = documentReference.iterator();

//         List<SensorData> sensorDataList = new ArrayList<>();
//         SensorData sensorData;
//         while (iterator.hasNext()) {
//             DocumentReference documentReference1=iterator.next();
//             ApiFuture<DocumentSnapshot> future = documentReference1.get();
//             DocumentSnapshot document = future.get();
//             sensorData = document.toObject(SensorData.class);
//             sensorDataList.add(sensorData);
//         }

//         return sensorDataList;
//     }

//     @Override
//     public String save(SensorData sensorData) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         //auto create data ID by firebase
//         DocumentReference addedDocRef = dbFirestore.collection(COL_NAME).document();
//         sensorData.setSensorDataId(addedDocRef.getId());
//         ApiFuture<WriteResult> collectionsApiFuture =
//                 addedDocRef.set(sensorData);
//         ApiFuture<WriteResult> writeResult = addedDocRef.update("timestamp", collectionsApiFuture.get().getUpdateTime());

//         return addedDocRef.getId();
//     }

//     @Override
//     public String update(SensorData sensorData) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference addedDocRef = dbFirestore.collection(COL_NAME).document(sensorData.getSensorDataId());
//         ApiFuture<WriteResult> collectionsApiFuture = null;
//         if(!(sensorData.getEcgReading() != 0)){
//             collectionsApiFuture =addedDocRef.update("ecgReading", sensorData.getEcgReading());
//         }
//         if (!(sensorData.getOxygenReading() != 0)){
//             collectionsApiFuture =  addedDocRef.update("oxygenReading", sensorData.getOxygenReading());
//         }
//         if (sensorData.getBodyTemperature() != null){
//             collectionsApiFuture = addedDocRef.update("bodyTemperature", sensorData.getBodyTemperature());
//         }
//         if (sensorData.getHeart_Rate() != 0){
//             collectionsApiFuture = addedDocRef.update("Heart_Rate", sensorData.getHeart_Rate());
//         }//mg, ijat, keng, faruq, din
//         if (collectionsApiFuture != null) {
//             ApiFuture<WriteResult> writeResult = addedDocRef.update("timestamp", collectionsApiFuture.get().getUpdateTime());
//             return writeResult.get().getUpdateTime().toString();
//         }
//         return Timestamp.now().toString();
//     }

//     @Override
//     public String delete(String sensorDataId) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         if(get(sensorDataId) == null){
//             ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(sensorDataId).delete();
//             return "The sensorData with Id " + sensorDataId + " is not exist.";
            
//         }
//         ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(sensorDataId).delete();
//         return "Document with Sensor Data Id " + sensorDataId + " has been deleted";
//     }
// }


//MongoDB//
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

    public SensorData get(String sensorDataId) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            Document doc = db.getCollection(COLLECTION_NAME)
                    .find(new Document("sensorDataId", sensorDataId))
                    .first();
            return doc != null ? SensorData.fromDocument(doc) : null;
        } finally {
            MongoDBConnection.close();
        }
    }

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

    public String appendHistory(String sensorDataId, SensorData historyEntry) {
        MongoDatabase db = MongoDBConnection.connect();
        try {
            Document historyDoc = new Document()
                    .append("heart_Rate", historyEntry.getHeart_Rate())
                    .append("bodyTemperature", historyEntry.getBodyTemperature())
                    .append("ecgReading", historyEntry.getEcgReading())
                    .append("oxygenReading", historyEntry.getOxygenReading())
                    .append("timestamp", Instant.now());

            db.getCollection(COLLECTION_NAME)
              .updateOne(new Document("sensorDataId", sensorDataId),
                         new Document("$push", new Document("history", historyDoc)));

            return "History entry added.";
        } finally {
            MongoDBConnection.close();
        }
    }

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

}
