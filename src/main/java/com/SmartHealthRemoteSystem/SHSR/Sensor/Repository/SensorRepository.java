package com.SmartHealthRemoteSystem.SHSR.Sensor.Repository;

import com.SmartHealthRemoteSystem.SHSR.Sensor.Model.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {
    Sensor findByUniqueKey(String uniqueKey);
}

