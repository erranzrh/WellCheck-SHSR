// package com.SmartHealthRemoteSystem.SHSR.User.Pharmacist;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ExecutionException;

// import org.springframework.stereotype.Repository;

// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.User.User;
// import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.WriteResult;
// import com.google.firebase.cloud.FirestoreClient;

// @Repository
// public class PharmacistRepository implements SHSRDAO<Pharmacist> {
//     public static final String COL_NAME = "Pharmacist";
//     public final UserRepository userRepository;

//     public PharmacistRepository(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     @Override
//     public Pharmacist get(String pharmacistId) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(pharmacistId);
//         ApiFuture<DocumentSnapshot> future = documentReference.get();
//         DocumentSnapshot document = future.get();
//         Pharmacist tempPharmacist;

//         if (document.exists()) {
//             tempPharmacist = document.toObject(Pharmacist.class);
//             User user = userRepository.get(pharmacistId);
//             assert tempPharmacist != null;
//             tempPharmacist.setUserId(user.getUserId());
//             tempPharmacist.setName(user.getName());
//             tempPharmacist.setPassword(user.getPassword());
//             tempPharmacist.setContact(user.getContact());
//             tempPharmacist.setRole(user.getRole());
//             tempPharmacist.setEmail(user.getEmail());
//             return tempPharmacist;
//         } else {
//             return null;
//         }
//     }

//     @Override
//     public List<Pharmacist> getAll() throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
//         Iterator<DocumentReference> iterator = documentReference.iterator();

//         List<Pharmacist> pharmacistList = new ArrayList<>();
//         Pharmacist pharmacist;
//         while (iterator.hasNext()) {
//             DocumentReference documentReference1 = iterator.next();
//             ApiFuture<DocumentSnapshot> future = documentReference1.get();
//             DocumentSnapshot document = future.get();
//             pharmacist = document.toObject(Pharmacist.class);
//             User user = userRepository.get(document.getId());
//             assert pharmacist != null;
//             pharmacist.setUserId(user.getUserId());
//             pharmacist.setPassword(user.getPassword());
//             pharmacist.setName(user.getName());
//             pharmacist.setContact(user.getContact());
//             pharmacist.setRole(user.getRole());
//             pharmacist.setEmail(user.getEmail());
//             pharmacistList.add(pharmacist);
//         }

//         return pharmacistList;
//     }

//     @Override
//     public String save(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Map<String, String> tempPharmacist = new HashMap<>();
//         tempPharmacist.put("hospital", pharmacist.getHospital());
//         tempPharmacist.put("position", pharmacist.getPosition());
//         tempPharmacist.put("name", pharmacist.getName());
//         tempPharmacist.put("contact", pharmacist.getContact());
//         // image
//         //tempPharmacist.put("profilePicture", pharmacist.getProfilePicture());

//         // Create User object and save it to the database
//         User user = new User(pharmacist.getUserId(), pharmacist.getName(), pharmacist.getPassword(), pharmacist.getContact(), pharmacist.getRole(), pharmacist.getEmail());
//         userRepository.save(user);

//         ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).set(tempPharmacist);
//         return collectionsApiFuture.get().getUpdateTime().toString();
//     }

//     @Override
//     public String update(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         if (!(pharmacist.getHospital().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).update("hospital", pharmacist.getHospital());
//         }
//         if (!(pharmacist.getPosition().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).update("position", pharmacist.getPosition());
//         }
//         if (!(pharmacist.getName().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).update("name", pharmacist.getName());
//         }
//         if (!(pharmacist.getContact().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).update("contact", pharmacist.getContact());
//         }
//         /*if (!(pharmacist.getProfilePicture().isEmpty())) {
//             dbFirestore.collection(COL_NAME).document(pharmacist.getUserId()).update("profilePicture", pharmacist.getProfilePicture());
//         }*/
//         User user = new User(pharmacist.getUserId(), pharmacist.getName(), pharmacist.getPassword(), pharmacist.getContact(), pharmacist.getRole(), pharmacist.getEmail());
//         return userRepository.update(user);
//     }

//     @Override
//     public String delete(String pharmacistId) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         String message;
//         String timeDeleteUser;
//         Pharmacist pharmacist = get(pharmacistId);

//         if (pharmacist != null) {
//             // Delete pharmacist
//             ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(pharmacistId).delete();
//             timeDeleteUser = userRepository.delete(pharmacistId);

//             return "Document with Pharmacist Id " + pharmacistId + " has been deleted. " + timeDeleteUser;
//         } else {
//             return "Pharmacist with Id " + pharmacistId + " not found.";
//         }
//     }
// }


package com.SmartHealthRemoteSystem.SHSR.User.Pharmacist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.MongoUserRepository;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Repository
public class PharmacistRepository implements SHSRDAO<Pharmacist> {

    @Autowired
    private MongoPharmacistRepository mongoPharmacistRepository;

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Override
    public Pharmacist get(String pharmacistId) throws ExecutionException, InterruptedException {
        Optional<Pharmacist> pharmacistOpt = mongoPharmacistRepository.findById(pharmacistId);
        if (pharmacistOpt.isPresent()) {
            Pharmacist pharmacist = pharmacistOpt.get();
            mongoUserRepository.findById(pharmacistId).ifPresent(user -> {
                pharmacist.setName(user.getName());
                pharmacist.setPassword(user.getPassword());
                pharmacist.setContact(user.getContact());
                pharmacist.setRole(user.getRole());
                pharmacist.setEmail(user.getEmail());
            });
            return pharmacist;
        }
        return null;
    }

    @Override
    public List<Pharmacist> getAll() throws ExecutionException, InterruptedException {
        List<Pharmacist> list = new ArrayList<>();
        for (Pharmacist pharmacist : mongoPharmacistRepository.findAll()) {
            mongoUserRepository.findById(pharmacist.getUserId()).ifPresent(user -> {
                pharmacist.setName(user.getName());
                pharmacist.setPassword(user.getPassword());
                pharmacist.setContact(user.getContact());
                pharmacist.setRole(user.getRole());
                pharmacist.setEmail(user.getEmail());
            });
            list.add(pharmacist);
        }
        return list;
    }

    @Override
    public String save(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
        if (mongoPharmacistRepository.existsById(pharmacist.getUserId())) {
            return "Error: Pharmacist with ID already exists.";
        }

        User user = new User(pharmacist.getUserId(), pharmacist.getName(), pharmacist.getPassword(),
                pharmacist.getContact(), pharmacist.getRole(), pharmacist.getEmail());
        mongoUserRepository.save(user);

        mongoPharmacistRepository.save(pharmacist);
        return "Pharmacist saved successfully.";
    }

    @Override
    public String update(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
        mongoPharmacistRepository.save(pharmacist);
        mongoUserRepository.save(new User(
                pharmacist.getUserId(),
                pharmacist.getName(),
                pharmacist.getPassword(),
                pharmacist.getContact(),
                pharmacist.getRole(),
                pharmacist.getEmail()
        ));
        return "Pharmacist updated successfully.";
    }

    @Override
    public String delete(String pharmacistId) throws ExecutionException, InterruptedException {
        mongoPharmacistRepository.deleteById(pharmacistId);
        mongoUserRepository.deleteById(pharmacistId);
        return "Pharmacist with ID " + pharmacistId + " deleted successfully.";
    }
}
