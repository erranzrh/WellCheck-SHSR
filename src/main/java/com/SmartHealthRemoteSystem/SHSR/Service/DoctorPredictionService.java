
//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Prediction.DoctorPrediction;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class DoctorPredictionService {

    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ Create doctor prediction
    public String createDoctorPrediction(DoctorPrediction prediction, String doctorId) {
        Doctor doctor = doctorRepository.get(doctorId);
        if (doctor == null) return "Doctor not found.";

        Map<String, DoctorPrediction> predictionMap = doctor.getDoctorPrediction();
        if (predictionMap == null) predictionMap = new HashMap<>();

        String predictionId = UUID.randomUUID().toString();
        prediction.setPredictionId(predictionId);
        prediction.setTimestamp(Instant.now());

        predictionMap.put(predictionId, prediction);
        doctor.setDoctorPrediction(predictionMap);

        doctorRepository.update(doctor);
        return predictionId;
    }

    // ✅ Get prediction by ID
    public DoctorPrediction getDoctorPrediction(String predictionId, String doctorId) {
        Doctor doctor = doctorRepository.get(doctorId);
        if (doctor == null || doctor.getDoctorPrediction() == null) return null;
        return doctor.getDoctorPrediction().get(predictionId);
    }

    // ✅ Get all predictions
    public List<DoctorPrediction> getListDoctorPrediction(String doctorId) {
        Doctor doctor = doctorRepository.get(doctorId);
        if (doctor == null || doctor.getDoctorPrediction() == null) return new ArrayList<>();
        return new ArrayList<>(doctor.getDoctorPrediction().values());
    }

    // ✅ Update prediction
    public String updateDoctorPrediction(DoctorPrediction updatedPrediction, String doctorId) {
        Doctor doctor = doctorRepository.get(doctorId);
        if (doctor == null) return "Doctor not found.";

        Map<String, DoctorPrediction> predictionMap = doctor.getDoctorPrediction();
        if (predictionMap == null || !predictionMap.containsKey(updatedPrediction.getPredictionId())) {
            return "Prediction not found.";
        }

        updatedPrediction.setTimestamp(Instant.now());
        predictionMap.put(updatedPrediction.getPredictionId(), updatedPrediction);
        doctor.setDoctorPrediction(predictionMap);

        doctorRepository.update(doctor);
        return "Prediction updated.";
    }

    // ✅ Delete prediction
    public String deleteDoctorPrediction(String predictionId, String doctorId) {
        Doctor doctor = doctorRepository.get(doctorId);
        if (doctor == null || doctor.getDoctorPrediction() == null) return "Doctor not found or no predictions.";

        Map<String, DoctorPrediction> predictionMap = doctor.getDoctorPrediction();
        if (predictionMap.remove(predictionId) != null) {
            doctor.setDoctorPrediction(predictionMap);
            doctorRepository.update(doctor);
            return "Prediction deleted.";
        }
        return "Prediction not found.";
    }

    // ✅ Get latest prediction
    public Optional<DoctorPrediction> getRecentDoctorPrediction(String doctorId) {
        List<DoctorPrediction> predictions = getListDoctorPrediction(doctorId);
        return predictions.stream().max(Comparator.comparing(DoctorPrediction::getTimestamp));
    }
}

