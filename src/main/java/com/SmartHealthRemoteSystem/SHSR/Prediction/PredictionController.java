package com.SmartHealthRemoteSystem.SHSR.Prediction;

import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PredictionService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class PredictionController {

    @Autowired private PredictionService predictionService;
    @Autowired private PredictionRestController predictionRestController;
    @Autowired private PatientService patientService;
    @Autowired private DoctorService doctorService;

    // ✅ Handle ML Prediction and Save to MongoDB
    @PostMapping("/DiagnosisResult")
public String makePrediction(@RequestParam("symptom[]") List<String> symptoms,
                             @RequestParam("patientId") String patientId,
                             Model model) throws ExecutionException, InterruptedException {

    ResponseEntity<String> response = predictionRestController.callDjangoAPI(symptoms);
    String predictionResult = response.getBody();

    // ADD THIS: full response logging
    System.out.println("🔥 Django full response: " + predictionResult);

    JsonObject jsonObject = new Gson().fromJson(predictionResult, JsonObject.class);
    JsonArray topDiseasesArray = jsonObject.getAsJsonArray("top_diseases");

    List<String> diseases = new ArrayList<>();
    List<Float> probabilities = new ArrayList<>();

    for (int i = 0; i < topDiseasesArray.size(); i++) {
        String diseaseWithProbability = topDiseasesArray.get(i).getAsString();
        String[] parts = diseaseWithProbability.split(": ");
        diseases.add(parts[0]);
        probabilities.add(Float.parseFloat(parts[1].replace("%", "")));
    }

    Prediction prediction = new Prediction();
    prediction.setDiagnosisList(diseases);
    prediction.setProbabilityList(probabilities);
    prediction.setSymptomsList(symptoms);
    prediction.setApproved(false);
    prediction.setRejected(false);

    String timeCreated = predictionService.createPrediction(prediction, patientId);

    Patient patient = patientService.getPatientById(patientId);
    Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());

    model.addAttribute("notice", "Your diagnosis has been submitted and is pending approval by your doctor.");
    model.addAttribute("patient", patient);
    model.addAttribute("doctor", doctor);
    model.addAttribute("symptoms", symptoms);
    model.addAttribute("diseases", diseases);
    model.addAttribute("probabilities", probabilities);
    model.addAttribute("timeCreated", timeCreated);

    return "DiagnosisResult";
}

    // ✅ Display prediction history (approved & rejected)
    @GetMapping("/predictionHistory")
    public String showPredictionHistory(@RequestParam("patientId") String patientId, Model model) throws ExecutionException, InterruptedException {
        Patient patient = patientService.getPatientById(patientId);
        Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());

        List<Prediction> approvedList = predictionService.getApprovedPredictions(patientId);
        List<Prediction> rejectedList = predictionService.getRejectedPredictions(patientId);

        // ⛔ Prevent Thymeleaf null crashes
        if (approvedList == null) approvedList = new ArrayList<>();
        if (rejectedList == null) rejectedList = new ArrayList<>();

        model.addAttribute("approvedList", approvedList);
        model.addAttribute("rejectedList", rejectedList);
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);

        return "Diagnosis";
    }
}
