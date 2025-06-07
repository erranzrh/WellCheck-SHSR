//FireStore//
// package com.SmartHealthRemoteSystem.SHSR.User.Patient;

// import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.SendDailyHealth.HealthStatus;
// import com.SmartHealthRemoteSystem.SHSR.User.User;
// import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.WriteResult;
// import com.google.firebase.cloud.FirestoreClient;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Repository;

// import java.util.*;
// import java.util.concurrent.ExecutionException;

// @Repository
// public class PatientRepository implements SHSRDAO<Patient> {
//     public static final String COL_NAME = "Patient";

//     private final SHSRDAO<User> userRepository;
//     private final SubCollectionSHSRDAO<HealthStatus> healthStatusRepository;
//     private final SHSRDAO<SensorData> sensorDataRepository;
//     private final SubCollectionSHSRDAO<Prescription> prescriptionRepository;

//     @Autowired
//     public PatientRepository(SHSRDAO<User> userRepository, SubCollectionSHSRDAO<HealthStatus> healthStatusRepository,
//                              SHSRDAO<SensorData> sensorDataRepository, SubCollectionSHSRDAO<Prescription> prescriptionRepository) {
//         this.userRepository = userRepository;
//         this.healthStatusRepository = healthStatusRepository;
//         this.sensorDataRepository = sensorDataRepository;
//         this.prescriptionRepository = prescriptionRepository;
//     }

//     @Override
//     public Patient get(String patientId) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(patientId);
//         ApiFuture<DocumentSnapshot> future = documentReference.get();
//         DocumentSnapshot document = future.get();
//         Patient tempPatient;
//         if (document.exists()) {
//             tempPatient = document.toObject(Patient.class);
//             User user = userRepository.get(patientId);
//             assert tempPatient != null;
//             tempPatient.setUserId(user.getUserId());
//             tempPatient.setName(user.getName());
//             tempPatient.setPassword(user.getPassword());
//             tempPatient.setContact(user.getContact());
//             tempPatient.setRole(user.getRole());
//             tempPatient.setEmail(user.getEmail());
//             return tempPatient;
//         } else {
//             return null;
//         }
//     }

//     @Override
//     public List<Patient> getAll() throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
//         Iterator<DocumentReference> iterator = documentReference.iterator();

//         List<Patient> patientList = new ArrayList<>();
//         Patient patient;
        
//         while (iterator.hasNext()) {
//             DocumentReference documentReference1 = iterator.next();
//             ApiFuture<DocumentSnapshot> future = documentReference1.get();
//             DocumentSnapshot document = future.get();
//             patient = document.toObject(Patient.class);

//             // Check if user is null before further processing
//             if (patient != null) {
//                 User user = userRepository.get(document.getId());
//                 if (user != null) {
//                     patient.setUserId(user.getUserId());
//                     patient.setPassword(user.getPassword());
//                     patient.setName(user.getName());
//                     patient.setContact(user.getContact());
//                     patient.setRole(user.getRole());
//                     patient.setEmail(user.getEmail());
//                     patientList.add(patient);
//                 }
//             }
//         }

//         return patientList;
//     }

//     @Override
//     public String save(Patient patient) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Map<String, String> tempPatient = new HashMap<>();
//         tempPatient.put("address", patient.getAddress());
//         tempPatient.put("emergencyContact", patient.getEmergencyContact());
//         tempPatient.put("sensorDataId", patient.getSensorDataId());
//         tempPatient.put("assigned_doctor", patient.getAssigned_doctor());
//         tempPatient.put("status",patient.getStatus());


//         //Create a temporary User
//         User user = new User(patient.getUserId(), patient.getName(), patient.getPassword(), patient.getContact(), patient.getRole(), patient.getEmail());
//         userRepository.save(user);

//         ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId()).set(tempPatient);
//         return collectionsApiFuture.get().getUpdateTime().toString();
//     }

//    /*  @Override
//     public String update(Patient patient) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         if (!(patient.getAddress().isEmpty())) {
//             ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId())
//                     .update("address", patient.getAddress());
//         }
//         if (!(patient.getAssigned_doctor() == null)) {
//             ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId())
//                     .update("assigned_doctor", patient.getAssigned_doctor());
//         }
//         if (!(patient.getEmergencyContact().isEmpty())) {
//             ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId())
//                     .update("emergencyContact", patient.getEmergencyContact());
//         }
//         if (!(patient.getSensorDataId() == null)) {
//             ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId())
//                     .update("sensorDataId", patient.getSensorDataId());
//         }
//         if (!(patient.getStatus() == null)) {
//             ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getUserId())
//                     .update("status", patient.getStatus());
//         }

//         return userRepository.update(new User(patient.getUserId(), patient.getName(), patient.getPassword(), patient.getContact(), patient.getRole()));
//     } */

//     @Override
//     public String update(Patient patient) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         // DocumentReference docRef = dbFirestore.collection(COL_NAME).document(patient.getUserId());

//         // Map<String, Object> updates = new HashMap<>();
//         if (!(patient.getAddress().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(patient.getUserId()).update("address", patient.getAddress());
//         }
//         if (!(patient.getEmergencyContact().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(patient.getUserId()).update("emergencyContact", patient.getEmergencyContact());
//         }
//         if (!(patient.getAssigned_doctor() == null)){
//             dbFirestore.collection(COL_NAME).document(patient.getUserId()).update("assigned_doctor", patient.getAssigned_doctor());
//         }
//         if (!(patient.getName().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(patient.getUserId()).update("name", patient.getName());
//         }
//         if (!(patient.getContact().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(patient.getUserId()).update("contact", patient.getContact());
//         }

//         User user = new User(patient.getUserId(), patient.getName(), patient.getPassword(), patient.getContact(), patient.getRole(), patient.getEmail());
//         return userRepository.update(user);
//     } 
 
//     @Override
//     public String delete(String patientId) throws ExecutionException, InterruptedException {
//         String healthStatusMessage = "", prescriptionMessage = "", timeDeleteUser, messageSensor = "";

//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Patient patient = get(patientId);
     
//         //delete sensor from sensor table
//         //since patient and sensor is 1-to-1 composition relation,
//         //delete the patient will also delete the sensor
//         if (patient.getSensorDataId().isEmpty()) {
//             ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(patientId).delete();
//              timeDeleteUser = userRepository.delete(patientId);
//              return "Document with Patient Id " + patientId + " has been deleted. " + "\n";// + timeDeleteUser;
//             // messageSensor = "sensor is not included";
            
//             //since patient doesn't have sensorId, we are not deleting the sensor from sensor database table
//         } else {
//             messageSensor = sensorDataRepository.delete(patient.getSensorDataId());
//         }

//         //Delete all health status patient in the database
//         List<HealthStatus> healthStatusList = healthStatusRepository.getAll(patientId);
//         for (HealthStatus healthStatus : healthStatusList) {
//             healthStatusMessage += healthStatusRepository.delete(patientId, healthStatus.getHealthStatusId()) + "\n";
//         }

//         //Delete all prescription patient in the database
//         List<Prescription> prescriptionList = prescriptionRepository.getAll(patientId);
//         for (Prescription prescription : prescriptionList) {
//             prescriptionMessage += prescriptionRepository.delete(patientId, prescription.getPrescriptionId()) + "\n";
//         }

//         //delete patient
//         ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(patientId).delete();
//         timeDeleteUser = userRepository.delete(patientId);


//         return "Document with Patient Id " + patientId + " has been deleted. " + "\n" ;
//         // messageSensor + " \n+" +
//         // healthStatusMessage +
//         // prescriptionMessage + "\n" +
//         // timeDeleteUser;
//     }
// }


//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.User.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.MongoUserRepository;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Repository
public class PatientRepository implements SHSRDAO<Patient> {
    private static final Logger LOGGER = Logger.getLogger(PatientRepository.class.getName());

    @Autowired
    private MongoPatientRepository mongoPatientRepository;

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Override
    public Patient get(String patientId) throws ExecutionException, InterruptedException {
        Optional<Patient> patientOptional = mongoPatientRepository.findById(patientId);

        if (patientOptional.isPresent()) {
            Patient tempPatient = patientOptional.get();
            try {
                Optional<User> userOpt = mongoUserRepository.findById(patientId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    tempPatient.setUserId(user.getUserId());
                    tempPatient.setName(user.getName());
                    tempPatient.setPassword(user.getPassword());
                    tempPatient.setContact(user.getContact());
                    tempPatient.setRole(user.getRole());
                    tempPatient.setEmail(user.getEmail());
                }
            } catch (Exception e) {
                LOGGER.warning("User details could not be retrieved for patient ID: " + patientId);
            }
            return tempPatient;
        }
        return null;
    }

    @Override
public List<Patient> getAll() throws ExecutionException, InterruptedException {
    System.out.println("✅ PatientRepository: getAll() called");
    List<Patient> patientList = new ArrayList<>();
    List<Patient> allPatients = mongoPatientRepository.findAll();

    for (Patient patient : allPatients) {
        System.out.println("🔁 Checking patient: " + patient.getUserId());

        Optional<User> userOpt = mongoUserRepository.findById(patient.getUserId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("✅ User found: " + user.getUserId() + " - " + user.getName());

            patient.setName(user.getName());
            patient.setPassword(user.getPassword());
            patient.setContact(user.getContact());
            patient.setRole(user.getRole());
            patient.setEmail(user.getEmail());
        } else {
            System.out.println("❌ No user found for patient: " + patient.getUserId());
        }

        patientList.add(patient);
    }

    return patientList;
}

    


@Override
public String save(Patient patient) throws ExecutionException, InterruptedException {
    System.out.println("📝 Saving patient with ID: " + patient.getUserId());

    // if (mongoPatientRepository.existsById(patient.getUserId())) {
    //     return "Error: Patient with ID " + patient.getUserId() + " already exists.";
    // }


    mongoPatientRepository.save(patient);

    return "Patient saved successfully.";
}


    @Override
public String update(Patient patient) throws ExecutionException, InterruptedException {
    Optional<Patient> existingPatientOpt = mongoPatientRepository.findById(patient.getUserId());

    if (existingPatientOpt.isPresent()) {
        Patient existingPatient = existingPatientOpt.get();

        // ✅ Update fields
        if (patient.getAddress() != null && !patient.getAddress().isEmpty()) {
            existingPatient.setAddress(patient.getAddress());
        }
        if (patient.getEmergencyContact() != null && !patient.getEmergencyContact().isEmpty()) {
            existingPatient.setEmergencyContact(patient.getEmergencyContact());
        }
        if (patient.getAssigned_doctor() != null) {
            existingPatient.setAssigned_doctor(patient.getAssigned_doctor());
        }
        if (patient.getSensorDataId() != null) {
            existingPatient.setSensorDataId(patient.getSensorDataId());
        }
        if (patient.getStatus() != null) {
            existingPatient.setStatus(patient.getStatus());
        }

        // ✅ Handle profile picture update (ADD THIS PART)
        if (patient.getProfilePicture() != null) {
            existingPatient.setProfilePicture(patient.getProfilePicture());
        }
        if (patient.getProfilePictureType() != null) {
            existingPatient.setProfilePictureType(patient.getProfilePictureType());
        }

        // ✅ Manual diagnosis flag
        existingPatient.setNeedsManualDiagnosis(patient.isNeedsManualDiagnosis());

        // ✅ Save updated patient
        mongoPatientRepository.save(existingPatient);

        // ✅ Also update User collection (for authentication fields)
        User user = new User(
                patient.getUserId(),
                patient.getName(),
                patient.getPassword(),
                patient.getContact(),
                patient.getRole(),
                patient.getEmail()
        );
        mongoUserRepository.save(user);

        return "Patient updated successfully.";
    }

    return "Error: Patient not found.";
}


    @Override
    public String delete(String id) throws ExecutionException, InterruptedException {
        if (mongoPatientRepository.existsById(id)) {
            mongoPatientRepository.deleteById(id);
            mongoUserRepository.deleteById(id);
            return "Patient with ID " + id + " deleted successfully.";
        }
        return "Error: Patient not found.";
    }

    public Optional<Patient> findByEmail(String email) {
        return mongoPatientRepository.findById(email);
    }
}
