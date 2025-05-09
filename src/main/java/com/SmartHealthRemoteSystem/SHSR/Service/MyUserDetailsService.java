// package com.SmartHealthRemoteSystem.SHSR.Service;

// import com.SmartHealthRemoteSystem.SHSR.User.User;
// import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
// import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

// import lombok.SneakyThrows;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// public class MyUserDetailsService implements UserDetailsService {
    
//     private UserRepository userRepository;

//     @Autowired
//     public MyUserDetailsService(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     @SneakyThrows
//     @Override
//     public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//         // user from firestore database
//         Optional<User> user = Optional.ofNullable(userRepository.get(userName));
//         if (user.isPresent()) {
//             System.out.println("User found in the database: " + userName);
//             return user.map(MyUserDetails::new).get();
//         } else {
//             System.out.println("User not found in the database: " + userName);
//             throw new UsernameNotFoundException("Not found: " + userName);
//         }
//     }
// }


package com.SmartHealthRemoteSystem.SHSR.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SmartHealthRemoteSystem.SHSR.User.User;
import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(" Attempting login with userId: " + userId);

        User user = userRepository.get(userId); // Get by userId (not email)
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        return new MyUserDetails(user); // Use your custom class
    }
}
