//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.DoctorRepository;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private  DoctorRepository doctorRepository;

    public String createPatient(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Patient getPatientById(String id) throws ExecutionException, InterruptedException {
        return patientRepository.get(id);
    }

    public List<Patient> getAllPatients() throws ExecutionException, InterruptedException {
        System.out.println("[PatientService] getAllPatients() called");
        return patientRepository.getAll();
    }
    

    public String updatePatient(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.update(patient);
    }

    public String deletePatient(String id) throws ExecutionException, InterruptedException {
        return patientRepository.delete(id);
    }

        public String getPatientSensorId(String patientId) throws ExecutionException, InterruptedException {
        Patient patient = patientRepository.get(patientId);
        return patient.getSensorDataId();
    }

    public Doctor getDoctor(String doctorId) throws ExecutionException, InterruptedException {
    return doctorRepository.get(doctorId);
}



    
}
