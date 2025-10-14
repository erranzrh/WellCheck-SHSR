package com.SmartHealthRemoteSystem.SHSR.User.Patient;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoPatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findAll(); // this will now only return documents with _class = Patient
}

