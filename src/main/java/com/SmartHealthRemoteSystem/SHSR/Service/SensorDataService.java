// package com.SmartHealthRemoteSystem.SHSR.Service;

// import com.SmartHealthRemoteSystem.SHSR.Prediction.Prediction;
// import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
// import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorDataRepository;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.Comparator;
// import java.util.List;
// import java.util.Optional;
// import java.util.concurrent.ExecutionException;

// @Service
// public class SensorDataService {
//     private final SHSRDAO<SensorData> sensorDataRepository;

//     public SensorDataService() {
//         sensorDataRepository=new SensorDataRepository();
//     }

//     @Autowired
//     public SensorDataService(SHSRDAO<SensorData> sensorDataRepository) {
//         this.sensorDataRepository = sensorDataRepository;
//     }

//     public String createSensorData() throws ExecutionException, InterruptedException {
//         SensorData sensorData = new SensorData();
//         return sensorDataRepository.save(sensorData);
//     }

//     public String deleteSensorData(String sensorId) throws ExecutionException, InterruptedException {
//         return sensorDataRepository.delete(sensorId);
//     }

//     public SensorData getSensorData(String sensorId) throws ExecutionException, InterruptedException {
//         return sensorDataRepository.get(sensorId);
//     }

//     public String updateSensorData(SensorData sensorData) throws ExecutionException, InterruptedException {
//         return sensorDataRepository.update(sensorData);
//     }

//     public String stringSensorData(String sensorId) throws ExecutionException, InterruptedException {
//         SensorData sensorData=sensorDataRepository.get(sensorId);
//         return sensorData.toString();
//     }

//     public Optional<SensorData> getRecentSensorData(String sensorId) throws ExecutionException, InterruptedException{
//     List<SensorData> SensorDataList = sensorDataRepository.getAll();
//     return SensorDataList.stream().max(Comparator.comparing(SensorData::getTimestamp));
//   }
// }

//MongoDB//

package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "SensorData";

    public String saveSensorData(SensorData sensorData) {
        sensorData.setTimestamp(Instant.now());
        mongoTemplate.save(sensorData, COLLECTION_NAME);
        return sensorData.getSensorDataId();
    }

    public List<SensorData> getAllSensorData() {
        return mongoTemplate.findAll(SensorData.class, COLLECTION_NAME);
    }

    public SensorData getSensorById(String sensorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sensorDataId").is(sensorId));
        return mongoTemplate.findOne(query, SensorData.class, COLLECTION_NAME);
    }

    public boolean updateSensorData(SensorData updatedSensorData) {
        SensorData existing = getSensorById(updatedSensorData.getSensorDataId());
        if (existing != null) {
            updatedSensorData.setTimestamp(Instant.now());
            mongoTemplate.save(updatedSensorData, COLLECTION_NAME);
            return true;
        }
        return false;
    }

    public boolean deleteSensorData(String sensorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sensorDataId").is(sensorId));
        return mongoTemplate.remove(query, SensorData.class, COLLECTION_NAME).getDeletedCount() > 0;
    }

    public Optional<SensorData> getMostRecentSensor() {
        List<SensorData> sensorDataList = getAllSensorData();
        return sensorDataList.stream()
                .max(Comparator.comparing(SensorData::getTimestamp));
    }
}
