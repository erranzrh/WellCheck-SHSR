// package com.SmartHealthRemoteSystem.SHSR.Service;

// import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
// import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
// import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ExecutionException;

// @Service
// public class PrescriptionService {
    
//     private final SubCollectionSHSRDAO<Prescription> prescriptionRepository;
//     // private final SHSRDAO<Prescription> prescriptionRepository;

//     @Autowired
//     public PrescriptionService(SubCollectionSHSRDAO<Prescription> prescriptionRepository) {

//         this.prescriptionRepository = prescriptionRepository;
//     }

//     public String createPrescription(Prescription prescription, String patientId) throws ExecutionException, InterruptedException {
//         System.out.println("patient id inside service "+patientId);
//         return prescriptionRepository.save(prescription, patientId);
//     }

//     public Prescription getPrescription(String prescriptionIdId, String patientId) throws ExecutionException, InterruptedException {
//         return prescriptionRepository.get(prescriptionIdId, patientId);
//     }
    
//     public List<Prescription> getListPrescription(String patientId) throws ExecutionException, InterruptedException {
//         return prescriptionRepository.getAll(patientId);
//     }

//     public String updatePrescription(Prescription prescription, String patientId) throws ExecutionException, InterruptedException {
//         return prescriptionRepository.update(prescription, patientId);
//     }

//     public String deletePrescription(String prescriptionId, String patientId) throws ExecutionException, InterruptedException {
//         return prescriptionRepository.delete(patientId, prescriptionId);
//     }

//     public String prescribeMedicines(String patientId, Map<String, Integer> selectedMedicines, String prescriptionDescription, String diagnosisAilmentDescription, MedicineService medicineService) throws ExecutionException, InterruptedException {
//         // Obtain the doctor's ID from the current user's details
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//         String doctorId = myUserDetails.getUsername();

//         // Retrieve the full medicine details for each selected medicine
//         List<String> medicineDetailsList = new ArrayList<>();
//         for (Map.Entry<String, Integer> entry : selectedMedicines.entrySet()) {
//             Medicine medicine = medicineService.getMedicine(entry.getKey());
//             if (medicine != null) {
//                 // Decrease the stock available by the quantity prescribed
//                 medicineService.prescribeMedicine(patientId, medicine.getMedId(), entry.getValue());
//                 // Add a formatted string with the medicine name and prescribed quantity
//                 medicineDetailsList.add(medicine.getMedName() + " - Quantity: " + entry.getValue());
//             }
//         }

//         // Create a new Prescription object with the formatted list of medicines
//         Prescription prescription = new Prescription(doctorId, medicineDetailsList, prescriptionDescription, diagnosisAilmentDescription);

//         // Save the prescription in the repository using the provided patient ID
//         return prescriptionRepository.save(prescription, patientId);
//     }
// }

//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.MongoPatientRepository;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class PrescriptionService {

    @Autowired
    private MongoPatientRepository patientRepository;

    @Autowired
    private MedicineService medicineService;

    // ✅ Create and save a prescription
    public String createPrescription(String patientId, Prescription prescription) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) {
            return "Patient not found!";
        }

        Patient patient = patientOptional.get();

        // Generate ID and set timestamp
        String prescriptionId = UUID.randomUUID().toString();
        prescription.setPrescriptionId(prescriptionId);
        prescription.setTimestamp(Instant.now());


        // Save into embedded map
        Map<String, Prescription> currentPrescriptions = patient.getPrescription();
        currentPrescriptions.put(prescriptionId, prescription);
        patient.setPrescription(currentPrescriptions);

        patientRepository.save(patient);

        return prescriptionId;
    }

    // ✅ Retrieve all prescriptions for a patient
    public List<Prescription> getAllPrescriptions(String patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) return Collections.emptyList();

        Patient patient = patientOptional.get();
        return new ArrayList<>(patient.getPrescription().values());
    }

    // ✅ Get single prescription
    public Prescription getPrescription(String patientId, String prescriptionId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) return null;

        return patientOptional.get().getPrescription().get(prescriptionId);
    }

    // ✅ Update prescription
    public String updatePrescription(String patientId, Prescription updatedPrescription) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) return "Patient not found.";

        Patient patient = patientOptional.get();
        Map<String, Prescription> prescriptionMap = patient.getPrescription();

        if (!prescriptionMap.containsKey(updatedPrescription.getPrescriptionId())) {
            return "Prescription ID not found.";
        }

        updatedPrescription.setTimestamp(Instant.now());
        prescriptionMap.put(updatedPrescription.getPrescriptionId(), updatedPrescription);
        patient.setPrescription(prescriptionMap);
        patientRepository.save(patient);

        return "Prescription updated successfully.";
    }

    // ✅ Delete prescription
    public String deletePrescription(String patientId, String prescriptionId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) return "Patient not found.";

        Patient patient = patientOptional.get();
        Map<String, Prescription> prescriptionMap = patient.getPrescription();
        if (prescriptionMap.remove(prescriptionId) != null) {
            patient.setPrescription(prescriptionMap);
            patientRepository.save(patient);
            return "Prescription deleted.";
        }

        return "Prescription not found.";
    }

    // ✅ Convenience method for prescribing medicines
    public String prescribeMedicines(String patientId, Map<String, Integer> selectedMedicines,
                                     String prescriptionDescription, String diagnosisAilmentDescription) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        String doctorId = userDetails.getUsername();

        List<String> medicineDetailsList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : selectedMedicines.entrySet()) {
            Medicine medicine = medicineService.getMedicine(entry.getKey());
            if (medicine != null) {
                medicineService.prescribeMedicine(patientId, medicine.getMedId(), entry.getValue());
                medicineDetailsList.add(medicine.getMedName() + " - Quantity: " + entry.getValue());
            }
        }

        Prescription prescription = new Prescription(doctorId, medicineDetailsList,
                prescriptionDescription, diagnosisAilmentDescription);

        return createPrescription(patientId, prescription);
    }
}

