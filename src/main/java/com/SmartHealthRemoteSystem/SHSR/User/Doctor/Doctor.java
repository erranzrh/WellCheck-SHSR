// package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

// import com.SmartHealthRemoteSystem.SHSR.User.User;

// public class Doctor extends User {
//     private String hospital;
//     private String position;
//     // image
//     private String profilePicture;

//     public Doctor() {
//     }

//     public Doctor(String userId, String name, String password, String contact, String role, String email,  String hospital, String position) {
//         super(userId, name, password, contact, role, email);
//         this.hospital = hospital;
//         this.position = position;
//     }

//     public Doctor(String name, String password, String contact, String role, String hospital, String position, String email) {
//         super(name, password, contact, role, email);
//         this.hospital = hospital;
//         this.position = position;
        
//     }

//     public String getHospital() {
//         return hospital;
//     }

//     public void setHospital(String hospital) {
//         this.hospital = hospital;
//     }

//     public String getPosition() {
//         return position;
//     }

//     public void setPosition(String position) {
//         this.position = position;
//     }
    
//     //image
//     public String getProfilePicture() {
//         return profilePicture;
//     }

//     public void setProfilePicture(String profilePicture) {
//         this.profilePicture = profilePicture;
//     }
// }

package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

import org.springframework.data.mongodb.core.mapping.Document;

import com.SmartHealthRemoteSystem.SHSR.User.User;

@Document(collection = "Doctor")  // Maps this class to the "Doctor" collection in MongoDB
public class Doctor extends User {

    private String hospital;
    private String position;
    private String profilePicture;

    public Doctor() {}

    public Doctor(String userId, String name, String password, String contact, String role, String email,
                  String hospital, String position) {
        super(userId, name, password, contact, role, email);
        this.hospital = hospital;
        this.position = position;
    }

    public Doctor(String name, String password, String contact, String role,
                  String hospital, String position, String email) {
        super(name, password, contact, role, email, email);
        this.hospital = hospital;
        this.position = position;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
