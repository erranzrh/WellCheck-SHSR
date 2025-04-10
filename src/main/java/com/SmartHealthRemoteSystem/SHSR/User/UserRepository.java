// package com.SmartHealthRemoteSystem.SHSR.User;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ExecutionException;

// import org.springframework.stereotype.Repository;

// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.Timestamp;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.WriteResult;
// import com.google.firebase.cloud.FirestoreClient;

// @Repository
// public class UserRepository implements SHSRDAO<User> {
//     public final String COL_NAME = "User";

//     @Override
//     public User get(String id) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(id);
//         ApiFuture<DocumentSnapshot> future = documentReference.get();
//         DocumentSnapshot document = future.get();
//         User user;
//         user = document.toObject(User.class);
//         return user;
//     }

//     @Override
//     public List<User> getAll() throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
//         Iterator<DocumentReference> iterator = documentReference.iterator();

//         List<User> userList = new ArrayList<>();
//         User user;
//         while (iterator.hasNext()) {
//             DocumentReference documentReference1=iterator.next();
//             ApiFuture<DocumentSnapshot> future = documentReference1.get();
//             DocumentSnapshot document = future.get();
//             user = document.toObject(User.class);
//             userList.add(user);
//         }
//         return userList;
//     }

//     @Override
//     public String save(User user) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).set(user);
//         return collectionsApiFuture.get().getUpdateTime().toString();
//     }

//     @Override
//     public String update(User user) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         ApiFuture<WriteResult> collectionsApiFuture = null;
//         //if-else condition to check which field does the user update.
//         //statement if-else condition add change value into a map key value
//         //map key value pass to firestore database to be update
//         if(!(user.getName().isEmpty())){
//             collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).update("name", user.getName());
//         }
        
//         if(!(user.getContact().isEmpty())){
//             collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).update("contact", user.getContact());
//         }

//         if(!(user.getEmail().isEmpty())){
//             collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).update("email", user.getEmail());
//         }

//         if (collectionsApiFuture != null) {
//             return collectionsApiFuture.get().getUpdateTime().toString();
//         } else{
//             return Timestamp.now().toString();
//         }
//     }

//     public String delete(String id) throws ExecutionException, InterruptedException {
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         Map<String, ApiFuture<WriteResult>> deleteFutures = new HashMap<>();
    
//         // Delete from User collection
//         DocumentReference userDocumentReference = dbFirestore.collection(COL_NAME).document(id);
//         deleteFutures.put("User", userDocumentReference.delete());
    
//         Map<String, String> roleCollections = new HashMap<>();
//         roleCollections.put("DOCTOR", "Doctor");
//         roleCollections.put("PATIENT", "Patient");
//         roleCollections.put("PHARMACIST", "Pharmacist");
    
//         // Delete role from collection
//         String userRole = getUserRole(id); //method to get the user role
//         if (roleCollections.containsKey(userRole)) {
//             String roleCollectionName = roleCollections.get(userRole);
//             DocumentReference roleDocumentReference = dbFirestore.collection(roleCollectionName).document(id);
//             deleteFutures.put(userRole, roleDocumentReference.delete());
//         }
    
//         //deletion process
//         for (Map.Entry<String, ApiFuture<WriteResult>> entry : deleteFutures.entrySet()) {
//             String collectionName = entry.getKey();
//             ApiFuture<WriteResult> deleteFuture = entry.getValue();
    
//             try {
//                 deleteFuture.get();
//                 System.out.println("Deleted from collection: " + collectionName);
                
//             } catch (Exception e) {
//                 System.err.println("Error deleting from collection " + collectionName + ": " + e.getMessage());
//                 e.printStackTrace();
//             }
//         }
    
//         return Timestamp.now().toString();
//     }
    
//     private String getUserRole(String userId) throws ExecutionException, InterruptedException {
//         //logic to get the user role from user id
//         // Return the user role as a string
//         Firestore dbFirestore = FirestoreClient.getFirestore();
//         DocumentReference userDocumentReference = dbFirestore.collection(COL_NAME).document(userId);
//         ApiFuture<DocumentSnapshot> future = userDocumentReference.get();
//         DocumentSnapshot document = future.get();
//         if (document.exists()) {
//             User user = document.toObject(User.class);
//             return user.getRole();
//         } else {
//             return null;
//         }
//     }
//     public boolean existsByEmail(String email) {
//         return false;
//     }
// }

package com.SmartHealthRemoteSystem.SHSR.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;

@Repository
public class UserRepository implements SHSRDAO<User> {

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Override
    public User get(String id) {
        System.out.println("Getting user by ID: " + id);
        return mongoUserRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAll() {
        System.out.println("Getting all users");
        return mongoUserRepository.findAll();
    }

    @Override
    public String save(User user) {
        System.out.println("Saving new user: " + user.getUserId());
        User savedUser = mongoUserRepository.save(user);
        return savedUser.getUserId();
    }

    @Override
    public String update(User user) {
        System.out.println("Updating user: " + user.getUserId());

        Optional<User> optionalUser = mongoUserRepository.findById(user.getUserId());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (user.getName() != null && !user.getName().isEmpty()) {
                existingUser.setName(user.getName());
            }

            if (user.getContact() != null && !user.getContact().isEmpty()) {
                existingUser.setContact(user.getContact());
            }

            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }

            mongoUserRepository.save(existingUser);
            return existingUser.getUserId();
        }

        return null;
    }

    @Override
    public String delete(String id) {
        System.out.println("Deleting user: " + id);
        mongoUserRepository.deleteById(id);
        return id;
    }

    public boolean existsByEmail(String email) {
        return mongoUserRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return mongoUserRepository.findByEmail(email);
    }
}
