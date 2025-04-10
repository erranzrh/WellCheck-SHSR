// package com.SmartHealthRemoteSystem.SHSR.User.Pharmacist;

// import com.SmartHealthRemoteSystem.SHSR.User.User;

// public class Pharmacist extends User {
    
//     private String pharmacistHospital;
//     private String pharmacistPosition;
//     // image
//     private byte[] profilePicture;

//     public Pharmacist(){
//     }

//     public Pharmacist(String userId, String name, String password, String contact, String role, String email, String pharmacistHospital, String pharmacistPosition) {
//         super(userId, name, password, contact, role, email);
//         this.pharmacistHospital = pharmacistHospital;
//         this.pharmacistPosition = pharmacistPosition;
//     }

//     public Pharmacist(String name, String password, String contact, String role, String email, String pharmacistHospital, String pharmacistPosition) {
//         super(name, password, contact, role, email);
//         this.pharmacistHospital = pharmacistHospital;
//         this.pharmacistPosition = pharmacistPosition;
//     }

//     public String getHospital() {
//         return pharmacistHospital;
//     }

//     public void setHospital(String pharmacistHospital) {
//         this.pharmacistHospital = pharmacistHospital;
//     }

//     public String getPosition() {
//         return pharmacistPosition;
//     }

//     public void setPosition(String pharmacistPosition) {
//         this.pharmacistPosition = pharmacistPosition;
//     }
    
//     //image
//     public byte[] getProfilePicture() {
//         return profilePicture;
//     }
    
//     public void setProfilePicture(byte[] profilePicture) {
//         this.profilePicture = profilePicture;
//     }
// }

package com.SmartHealthRemoteSystem.SHSR.User.Pharmacist;

import org.springframework.data.mongodb.core.mapping.Document;

import com.SmartHealthRemoteSystem.SHSR.User.User;

@Document(collection = "Pharmacist")
public class Pharmacist extends User {

    private String hospital;
    private String position;
    private byte[] profilePicture; 

    public Pharmacist() {}

    public Pharmacist(String userId, String name, String password, String contact, String role, String email,
                      String hospital, String position) {
        super(userId, name, password, contact, role, email);
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    
    
}

