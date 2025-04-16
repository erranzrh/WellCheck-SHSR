// // package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

// // import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// // import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// // import com.SmartHealthRemoteSystem.SHSR.User.User;
// // import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
// // import com.google.api.core.ApiFuture;
// // import com.google.cloud.firestore.DocumentReference;
// // import com.google.cloud.firestore.DocumentSnapshot;
// // import com.google.cloud.firestore.Firestore;
// // import com.google.cloud.firestore.WriteResult;
// // import com.google.firebase.cloud.FirestoreClient;
// // import org.springframework.stereotype.Repository;

// // import javax.print.Doc;
// // import java.util.*;
// // import java.util.concurrent.ExecutionException;
// // @Repository
// // public class DoctorRepository implements SHSRDAO<Doctor> {
    
// //     public static final String COL_NAME = "Doctor";
// //     public final UserRepository userRepository;

// //     public DoctorRepository(UserRepository userRepository) {
// //         this.userRepository = userRepository;
// //     }

// //     @Override
// //     public Doctor get(String doctorId) throws ExecutionException, InterruptedException {
// //         Firestore dbFirestore = FirestoreClient.getFirestore();
// //         DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(doctorId);
// //         ApiFuture<DocumentSnapshot> future = documentReference.get();
// //         DocumentSnapshot document = future.get();
// //         Doctor tempDoctor;

// //         if (document.exists()) {
// //             tempDoctor  = document.toObject(Doctor.class);
// //             User user = userRepository.get(doctorId);
// //             assert tempDoctor != null;
// //             tempDoctor.setUserId(user.getUserId());
// //             tempDoctor.setName(user.getName());
// //             tempDoctor.setPassword(user.getPassword());
// //             tempDoctor.setContact(user.getContact());
// //             tempDoctor.setRole(user.getRole());
// //             tempDoctor.setEmail(user.getEmail());
// //             return tempDoctor;
// //         } else {
// //             return null;
// //         }
// //     }

// //     @Override
// //     public List<Doctor> getAll() throws ExecutionException, InterruptedException {
// //         Firestore dbFirestore = FirestoreClient.getFirestore();
// //         Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
// //         Iterator<DocumentReference> iterator = documentReference.iterator();

// //         List<Doctor> doctorList = new ArrayList<>();
// //         Doctor doctor;
// //         while (iterator.hasNext()) {
// //             DocumentReference documentReference1=iterator.next();
// //             ApiFuture<DocumentSnapshot> future = documentReference1.get();
// //             DocumentSnapshot document = future.get();
// //             doctor = document.toObject(Doctor.class);
// //             User user = userRepository.get(document.getId());
// //             assert doctor != null;
// //             doctor.setUserId(user.getUserId());
// //             doctor.setPassword(user.getPassword());
// //             doctor.setName(user.getName());
// //             doctor.setContact(user.getContact());
// //             doctor.setRole(user.getRole());
// //             doctor.setEmail(user.getEmail());
// //             doctorList.add(doctor);
// //         }

// //         return doctorList;
// //     }

// //     @Override
// //     public String save(Doctor doctor) throws ExecutionException, InterruptedException {
// //         Firestore dbFirestore = FirestoreClient.getFirestore();
// //         Map<String, String> tempDoctor = new HashMap<>();
// //         tempDoctor.put("hospital", doctor.getHospital());
// //         tempDoctor.put("position", doctor.getPosition());
// //         tempDoctor.put("name", doctor.getName());
// //         tempDoctor.put("contact", doctor.getContact());
// //         //image
// //         tempDoctor.put("profilePicture", doctor.getProfilePicture());

// //         //Create User object and save it to database
// //         User user = new User(doctor.getUserId(), doctor.getName(), doctor.getPassword(), doctor.getContact(), doctor.getRole(), doctor.getEmail());
// //         userRepository.save(user);

// //         ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(doctor.getUserId()).set(tempDoctor);
// //         return collectionsApiFuture.get().getUpdateTime().toString();
// //     }

// //     @Override
// //     public String update(Doctor doctor) throws ExecutionException, InterruptedException {
// //         Firestore dbFirestore = FirestoreClient.getFirestore();
// //     if(!(doctor.getHospital().isEmpty())){
// //         dbFirestore.collection(COL_NAME).document(doctor.getUserId()).update("hospital", doctor.getHospital());
// //     }
// //     if(!(doctor.getPosition().isEmpty())){
// //         dbFirestore.collection(COL_NAME).document(doctor.getUserId()).update("position", doctor.getPosition());
// //     }
// //     if(!(doctor.getName().isEmpty())){
// //         dbFirestore.collection(COL_NAME).document(doctor.getUserId()).update("name", doctor.getName());
// //     }
// //     if(!(doctor.getContact().isEmpty())){
// //         dbFirestore.collection(COL_NAME).document(doctor.getUserId()).update("contact", doctor.getContact());
// //     }
// //     if(!(doctor.getProfilePicture().isEmpty())){
// //         dbFirestore.collection(COL_NAME).document(doctor.getUserId()).update("profilePicture", doctor.getProfilePicture());
// //     }
// //         User user = new User(doctor.getUserId(), doctor.getName(), doctor.getPassword(), doctor.getContact(), doctor.getRole(), doctor.getEmail());
// //         return userRepository.update(user);
// //     }

// //     @Override
// //     public String delete(String doctorId) throws ExecutionException, InterruptedException {
// //         Firestore dbFirestore = FirestoreClient.getFirestore();
// //         String message;
// //         String timeDeleteUser;
// //         Doctor doctor = get(doctorId);
      
// //             //delete patient
// //             ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(doctorId).delete();
// //             timeDeleteUser = userRepository.delete(doctorId);
    
// //         return "Document with Doctor Id " + doctorId + " has been deleted. " + timeDeleteUser;
// //     }
// // }

// package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.stereotype.Repository;

// import java.util.Optional;

// @Repository
// public interface MongoDoctorRepository extends MongoRepository<Doctor, String> {
//     Optional<Doctor> findByEmail(String email);
// }

package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Repository
public class DoctorRepository implements SHSRDAO<Doctor> {

    @Autowired
    private MongoDoctorRepository mongoDoctorRepository;

    @Autowired
    private SHSRDAO<User> userRepository;

    @Override
    public Doctor get(String id) throws ExecutionException, InterruptedException {
        Optional<Doctor> doctorOptional = mongoDoctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            User user = userRepository.get(id);
            if (user != null) {
                doctor.setEmail(user.getEmail());
                doctor.setName(user.getName());
                doctor.setContact(user.getContact());
                doctor.setPassword(user.getPassword());
                doctor.setRole(user.getRole());
            }
            return doctor;
        }
        return null;
    }

    @Override
    public List<Doctor> getAll() throws ExecutionException, InterruptedException {
        List<Doctor> doctorList = new ArrayList<>();
        List<Doctor> allDoctors = mongoDoctorRepository.findAll();
        for (Doctor doctor : allDoctors) {
            User user = userRepository.get(doctor.getUserId());
            if (user != null) {
                doctor.setEmail(user.getEmail());
                doctor.setName(user.getName());
                doctor.setContact(user.getContact());
                doctor.setPassword(user.getPassword());
                doctor.setRole(user.getRole());
            }
            doctorList.add(doctor);
        }
        return doctorList;
    }

    @Override
    public String save(Doctor doctor) throws ExecutionException, InterruptedException {
        if (mongoDoctorRepository.existsById(doctor.getUserId())) {
            return "Doctor already exists.";
        }

        User user = new User(
                doctor.getUserId(),
                doctor.getName(),
                doctor.getPassword(),
                doctor.getContact(),
                doctor.getRole(),
                doctor.getEmail()
        );

        userRepository.save(user);
        mongoDoctorRepository.save(doctor);
        return "Doctor saved successfully.";
    }

    @Override
    public String update(Doctor doctor) throws ExecutionException, InterruptedException {
        Optional<Doctor> existingDoctor = mongoDoctorRepository.findById(doctor.getUserId());
        if (existingDoctor.isPresent()) {
            mongoDoctorRepository.save(doctor);
            return userRepository.update(new User(
                    doctor.getUserId(),
                    doctor.getName(),
                    doctor.getPassword(),
                    doctor.getContact(),
                    doctor.getRole(),
                    doctor.getEmail()
            ));
        }
        return "Doctor not found.";
    } 

    @Override
    public String delete(String id) throws ExecutionException, InterruptedException {
        mongoDoctorRepository.deleteById(id);
        userRepository.delete(id);
        return "Doctor deleted.";
    }
}


