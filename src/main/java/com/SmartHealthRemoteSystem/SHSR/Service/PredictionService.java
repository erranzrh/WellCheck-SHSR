// package com.SmartHealthRemoteSystem.SHSR.Service;

// import java.util.concurrent.ExecutionException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.SmartHealthRemoteSystem.SHSR.Prediction.Prediction;
// import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;

// import java.util.List;
// import java.util.Optional;
// import java.util.Comparator;

// @Service
// public class PredictionService {
//   private final SubCollectionSHSRDAO<Prediction> predictionRepository;

//   @Autowired
//   public PredictionService(SubCollectionSHSRDAO<Prediction> predictionRepository){
//     this.predictionRepository = predictionRepository;
//   }

//   public String createPrediction(Prediction prediction, String patientId) throws ExecutionException, InterruptedException{
//     System.out.println("patient id inside service "+patientId);
//     return predictionRepository.save(prediction, patientId);
//   }

//   public Prediction getPrediction(String predictionId, String patientId) throws ExecutionException, InterruptedException{
//     return predictionRepository.get(predictionId, patientId);
//   }

//   public List<Prediction> getListPrediction(String patientId) throws ExecutionException, InterruptedException{
//     return predictionRepository.getAll(patientId);
//   }

//   public String updatePrediction(Prediction prediction, String patientId) throws ExecutionException, InterruptedException{
//     return predictionRepository.update(prediction, patientId);
//   }

//   public String deletePrediction(String predictionId, String patientId) throws ExecutionException, InterruptedException{
//     return predictionRepository.delete(predictionId, patientId);
//   }

//   public Optional<Prediction> getRecentPrediction(String patientId) throws ExecutionException, InterruptedException{
//     List<Prediction> PredictionList = predictionRepository.getAll(patientId);
//     return PredictionList.stream().max(Comparator.comparing(Prediction::getTimestamp));
//   }
// }



//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Prediction.Prediction;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class PredictionService {

    @Autowired
    private PatientRepository patientRepository;

    public String createPrediction(Prediction prediction, String patientId) throws ExecutionException, InterruptedException {
        Patient patient = patientRepository.get(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        String predictionId = UUID.randomUUID().toString();
        prediction.setPredictionID(predictionId);
        prediction.setTimestamp(Instant.now());

        if (patient.getPrediction() == null) {
            patient.setPrediction(new HashMap<>());
        }
        patient.getPrediction().put(predictionId, prediction);
       
        System.out.println("📌 Storing prediction ID: " + predictionId);
        System.out.println("📝 Prediction data: " + prediction);
        System.out.println("🧑‍⚕️ Patient ID: " + patientId);
        System.out.println("🧠 Current prediction map size: " + patient.getPrediction().size());

        patientRepository.save(patient);
        return prediction.getTimestamp().toString();
    }

    public List<Prediction> getPatientPredictions(String patientId) throws ExecutionException, InterruptedException {
        Patient patient = patientRepository.get(patientId);
        if (patient == null || patient.getPrediction() == null) {
            return Collections.emptyList();
        }

        List<Prediction> predictions = new ArrayList<>(patient.getPrediction().values());
        predictions.sort(Comparator.comparing(Prediction::getTimestamp).reversed());
        return predictions;
    }

    public Optional<Prediction> getRecentPrediction(String patientId) throws ExecutionException, InterruptedException {
        List<Prediction> predictions = getPatientPredictions(patientId);
        return predictions.stream().max(Comparator.comparing(Prediction::getTimestamp));
    }
}
