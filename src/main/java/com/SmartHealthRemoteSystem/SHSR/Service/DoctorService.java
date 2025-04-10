// package com.SmartHealthRemoteSystem.SHSR.Service;

// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// import com.SmartHealthRemoteSystem.SHSR.User.User;
// import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.concurrent.ExecutionException;

// @Service
// public class DoctorService {
//     private final SHSRDAO<Doctor> doctorRepository;
//     private final SHSRDAO<Patient> patientRepository;
//     private final SHSRDAO<User> userRepository;

//     @Autowired
//     public DoctorService(SHSRDAO<Doctor> doctorRepository, SHSRDAO<Patient> patientRepository, SHSRDAO<User> userRepository) {
//         this.doctorRepository = doctorRepository;
//         this.patientRepository = patientRepository;
//         this.userRepository = userRepository;
//     }

//     public String createDoctor(Doctor doctor) throws ExecutionException, InterruptedException {
//         boolean checkUserExist = false;
//         //create a temporary user
//         User user = new User(doctor.getUserId(), doctor.getName(), doctor.getPassword(), doctor.getContact(), doctor.getRole(), doctor.getEmail());

//         //get list of all user
//         List<User> userList = userRepository.getAll();
//         for (User u : userList) {
//             //if the Id already exist
//             if (u.getUserId().equals(doctor.getUserId())) {
//                 checkUserExist = true;
//                 break;
//             }
//         }
//         //return error message
//         if (checkUserExist) {
//             return "Error create doctor with id " + doctor.getUserId() + ". Please use another Id.";
//         }

//         userRepository.save(user);
//         return doctorRepository.save(doctor);
//     }

//     public String updateDoctor(Doctor doctor) throws ExecutionException, InterruptedException {
//         return doctorRepository.update(doctor);
//     }
  
//     public Doctor getDoctor(String doctorId) throws ExecutionException, InterruptedException {
        
//         if(doctorId.isEmpty()){
//             return new Doctor();
//         }
//         Doctor doctor = doctorRepository.get(doctorId);
//         if (doctor == null) {
//             return null;
//         } else {
//             User user = userRepository.get(doctorId);
//             doctor.setName(user.getName());
//             doctor.setPassword(user.getPassword());
//             doctor.setContact(user.getContact());
//             doctor.setRole(user.getRole());
//             doctor.setUserId(user.getUserId());
//             doctor.setEmail(user.getEmail());
//             return doctor;
//         }
//     }

//     public List<Doctor> getListDoctor() throws ExecutionException, InterruptedException {
//         return doctorRepository.getAll();
//     }


//     public String deleteDoctor(String doctorId) throws ExecutionException, InterruptedException {
//         return doctorRepository.delete(doctorId);
//     }

//     public List<Patient> findAllPatientAssignToDoctor(String doctorId) throws ExecutionException, InterruptedException {
//         List<Patient> patientList = new ArrayList<>();
//         //Logic on finding all the patient that has been assign to doctor...
//         List<Patient> allPatientList = patientRepository.getAll();

//         for (Patient patient : allPatientList) {
//             if (patient.getAssigned_doctor().equals(doctorId)&& patient.getStatus().equals("Under Surveillance")) {
//                 patientList.add(patient);
//             }
//         }
//         return patientList;
//     }
    
//     public List<Patient> getListPatient() throws ExecutionException, InterruptedException {
//         //function to return list of unassigned patient
//         List<Patient> patients=patientRepository.getAll();
//         for(int i=patients.size()-1;i>=0;i--)
//         {
//             if (!(patients.get(i).getAssigned_doctor().isEmpty())){
//                 patients.remove(i);
//             }
//         }
//         return patients;
//     }

//     public Patient getPatient(String patientId) throws ExecutionException, InterruptedException {
//         return patientRepository.get(patientId);
//     }

// }

package com.SmartHealthRemoteSystem.SHSR.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.DoctorRepository;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Doctor getDoctor(String doctorId) throws ExecutionException, InterruptedException {
        return doctorRepository.get(doctorId);
    }

    public List<Doctor> getAllDoctors() throws ExecutionException, InterruptedException {
        return doctorRepository.getAll();
    }

    public String saveDoctor(Doctor doctor) throws ExecutionException, InterruptedException {
        return doctorRepository.save(doctor);
    }

    public String updateDoctor(Doctor doctor) throws ExecutionException, InterruptedException {
        return doctorRepository.update(doctor);
    }

    public String deleteDoctor(String id) throws ExecutionException, InterruptedException {
        return doctorRepository.delete(id);
    }

    public List<Patient> getListPatient() throws ExecutionException, InterruptedException {
        return patientRepository.getAll();
    }

    // ADD THIS: Get only patients assigned to this doctor
    public List<Patient> findAllPatientAssignToDoctor(String doctorId) throws ExecutionException, InterruptedException {
        List<Patient> allPatients = patientRepository.getAll();
        List<Patient> assignedPatients = new ArrayList<>();

        for (Patient patient : allPatients) {
            if (patient.getAssigned_doctor() != null && patient.getAssigned_doctor().equals(doctorId)) {
                assignedPatients.add(patient);
            }
        }

        return assignedPatients;
    }
}
