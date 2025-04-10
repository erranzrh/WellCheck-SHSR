// package com.SmartHealthRemoteSystem.SHSR.Service;

// import java.util.List;
// import java.util.concurrent.ExecutionException;
// import java.util.stream.Collectors;

// import org.springframework.stereotype.Service;

// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.User.User;

// @Service
// public class UserService {
    
//     private final SHSRDAO<User> userRepository;

//     public UserService(SHSRDAO<User> userRepository) {
//         this.userRepository = userRepository;
//     }

//     public String updateUser(User user) throws ExecutionException, InterruptedException {
//         return userRepository.update(user);
//     }

//     public String createUser(User user) throws ExecutionException, InterruptedException {
//         //to check whether userId is taken or not
//         List<User> userList = userRepository.getAll();
//         for(User user1:userList){
//             if(user.getUserId().equals(user1.getUserId()) || (user.getEmail() != null && user.getEmail().equals(user1.getEmail()))){
//                 return "Failed to create user with id " + user.getUserId() + ",please choose another Id";
//             }
//         }
//         return userRepository.save(user);
//     }

//     public User getUser(String userId) throws ExecutionException, InterruptedException {
//         return userRepository.get(userId);
//     }

//     public List<User> getUserList() throws ExecutionException, InterruptedException {
//         return userRepository.getAll();
//     }

//     public String deleteUser(String userId) throws ExecutionException, InterruptedException {
//         return userRepository.delete(userId);
//     }

//     public List<User> getAdminList() throws ExecutionException, InterruptedException {
//         List<User> userList = userRepository.getAll();
//         for(int i = userList.size()-1; i >= 0; i--){
//             if(!userList.get(i).getRole().equals("ADMIN")){
//                 userList.remove(i);
//             }
//         }
//         return userList;
//     }
//     public List<User> searchUsers(String keyword) throws ExecutionException, InterruptedException {
//         List<User> allUsers = userRepository.getAll();

//         return allUsers.stream()
//                 .filter(user -> user.getUserId().contains(keyword) || user.getName().contains(keyword)) // adjust fields as needed
//                 .collect(Collectors.toList());
//     }
// }
package com.SmartHealthRemoteSystem.SHSR.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Service
public class UserService {
    private final SHSRDAO<User> userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(SHSRDAO<User> userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String updateUser(User user) throws ExecutionException, InterruptedException {
        return userRepository.update(user);
    }

    public String createUser(User user) throws ExecutionException, InterruptedException {
        List<User> userList = userRepository.getAll();
        
        boolean isTaken = userList.stream()
                .anyMatch(existingUser -> 
                    existingUser.getUserId().equals(user.getUserId()) || 
                    (user.getEmail() != null && user.getEmail().equals(existingUser.getEmail()))
                );
        
        if (isTaken) {
            return "Failed to create user. ID or Email already exists.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return userRepository.save(user);
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        return userRepository.get(userId);
    }

    public List<User> getUserList() throws ExecutionException, InterruptedException {
        return userRepository.getAll();
    }

    public String deleteUser(String userId) throws ExecutionException, InterruptedException {
        return userRepository.delete(userId);
    }

    public List<User> getAdminList() throws ExecutionException, InterruptedException {
        return userRepository.getAll()
                .stream()
                .filter(user -> "ADMIN".equals(user.getRole()))
                .collect(Collectors.toList());
    }

    public List<User> searchUsers(String keyword) throws ExecutionException, InterruptedException {
        return userRepository.getAll()
                .stream()
                .filter(user -> user.getUserId().contains(keyword) || user.getName().contains(keyword))
                .collect(Collectors.toList());
    }
}
