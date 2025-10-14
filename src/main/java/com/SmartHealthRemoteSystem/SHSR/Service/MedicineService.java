

//Mongodb//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Medicine.MongoMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MongoMedicineRepository medicineRepository;

    public String createMedicine(Medicine medicine) {
        Medicine saved = medicineRepository.save(medicine);
        return saved.getMedId(); // assuming medId is set before or auto-generated
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public String deleteMedicine(String medId) {
        medicineRepository.deleteById(medId);
        return "Medicine with ID " + medId + " deleted.";
    }

    public Medicine getMedicine(String medId) {
        Optional<Medicine> medicine = medicineRepository.findById(medId);
        return medicine.orElse(null);
    }

    public String updateMedicine(Medicine medicine) {
        if (medicineRepository.existsById(medicine.getMedId())) {
            medicineRepository.save(medicine);
            return "Medicine updated successfully.";
        }
        return "Error: Medicine with ID " + medicine.getMedId() + " not found.";
    }

    public List<Medicine> getMedicineByPatientId(String patientId) {
        return medicineRepository.findByPatientId(patientId); // if declared in repository
    }

    public Medicine getMedicineById(String medId) {
        return medicineRepository.findById(medId).orElse(null);
    }

    public void prescribeMedicine(String patientId, String medId, int quantity) {
    Optional<Medicine> optionalMedicine = medicineRepository.findById(medId);
    if (optionalMedicine.isPresent()) {
        Medicine medicine = optionalMedicine.get();
        int updatedQuantity = medicine.getQuantity() - quantity;
        medicine.setQuantity(Math.max(0, updatedQuantity));  // Avoid negative values
        medicineRepository.save(medicine);
    }
    
    }

    //addStock//
    public String addStock(String medId, int quantityToAdd) {
    Optional<Medicine> optionalMedicine = medicineRepository.findById(medId);
    if (optionalMedicine.isPresent()) {
        Medicine medicine = optionalMedicine.get();
        medicine.setQuantity(medicine.getQuantity() + quantityToAdd);
        medicineRepository.save(medicine);
        return "Stock added successfully.";
    }
    return "Medicine not found.";
}


public boolean isMedicineNameExists(String medName) {
    List<Medicine> all = medicineRepository.findAll();
    return all.stream().anyMatch(m -> m.getMedName().equalsIgnoreCase(medName));
}


    


    
}

