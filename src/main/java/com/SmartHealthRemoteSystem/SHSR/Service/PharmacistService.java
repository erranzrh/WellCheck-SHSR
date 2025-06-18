

//MongoDB//

package com.SmartHealthRemoteSystem.SHSR.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.User.User;
import com.SmartHealthRemoteSystem.SHSR.User.Pharmacist.Pharmacist;
import com.SmartHealthRemoteSystem.SHSR.User.Pharmacist.PharmacistRepository;
import com.SmartHealthRemoteSystem.SHSR.Medicine.MongoMedicineRepository;



@Service
public class PharmacistService {

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoMedicineRepository medicineRepository;

    public String createPharmacist(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
        return pharmacistRepository.save(pharmacist);
    }

    public Pharmacist getPharmacist(String pharmacistId) throws ExecutionException, InterruptedException {
        Pharmacist pharmacist = pharmacistRepository.get(pharmacistId);
        User user = userService.getUser(pharmacistId);

        if (pharmacist == null || user == null) {
            return null;
        }

        pharmacist.setUserId(user.getUserId());
        pharmacist.setName(user.getName());
        pharmacist.setPassword(user.getPassword());
        pharmacist.setContact(user.getContact());
        pharmacist.setRole(user.getRole());
        pharmacist.setEmail(user.getEmail());

        return pharmacist;
    }

    public List<Pharmacist> getListPharmacist() throws ExecutionException, InterruptedException {
        return pharmacistRepository.getAll();
    }

    public String updatePharmacist(Pharmacist pharmacist) throws ExecutionException, InterruptedException {
        return pharmacistRepository.update(pharmacist);
    }

    public String deletePharmacist(String id) throws ExecutionException, InterruptedException {
        return pharmacistRepository.delete(id);
    }

    public List<Medicine> getListMedicine() {
    return medicineRepository.findAll(); // ✅ Spring MongoDB built-in method
}





}