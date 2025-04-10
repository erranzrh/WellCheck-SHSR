package com.SmartHealthRemoteSystem.SHSR.User.Patient;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoPatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByEmail(String email);
}
