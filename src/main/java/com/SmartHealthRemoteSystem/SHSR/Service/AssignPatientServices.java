


//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;

import org.springframework.stereotype.Service;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.DoctorRepository;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AssignPatientServices {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    public AssignPatientServices(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // ✅ Get unassigned patients that are still under surveillance
   public List<Patient> getListUnassignedPatients() throws ExecutionException, InterruptedException {
    return patientRepository.getAll().stream()
        .filter(p -> (p.getAssigned_doctor() == null || p.getAssigned_doctor().isEmpty())
            && !"Released".equalsIgnoreCase(p.getStatus())) // accept "Unassigned" or "Under Surveillance"
        .collect(Collectors.toList());
}


    // ✅ Get doctor by ID
    public Doctor getDoctor(String doctorId) throws ExecutionException, InterruptedException {
        return doctorRepository.get(doctorId);
    }

    // ✅ Get patient by ID
    public Patient getPatient(String patientId) throws ExecutionException, InterruptedException {
        return patientRepository.get(patientId);
    }

    // ✅ Assign patient to doctor
    public void assignPatient(String patientId, String doctorId) throws ExecutionException, InterruptedException {
        Patient patient = getPatient(patientId);
        patient.setAssigned_doctor(doctorId);
        patient.setStatus("Under Surveillance");
        patientRepository.update(patient);
    }

   public String UnassignDoctor(String patientId, String doctorId) throws ExecutionException, InterruptedException {
    Patient patient = patientRepository.get(patientId);

    if (patient != null && doctorId.equals(patient.getAssigned_doctor())) {
        patient.setAssigned_doctor(""); // unassign doctor
        patient.setStatus("Under Surveillance");
        return patientRepository.update(patient); // persist the update
    }

    return "Error: Patient not found or not assigned to this doctor.";
}


    // ✅ Release patient
    public void releasePatient(String patientId) throws ExecutionException, InterruptedException {
    Patient patient = getPatient(patientId);
    if (patient != null) {
        patient.setAssigned_doctor(""); // clear doctor
        patient.setStatus("Released");
        patientRepository.update(patient);
    }
}


    

}

