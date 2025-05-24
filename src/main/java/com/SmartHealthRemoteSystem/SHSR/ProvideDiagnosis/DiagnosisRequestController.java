// package com.SmartHealthRemoteSystem.SHSR.ProvideDiagnosis;
// import com.SmartHealthRemoteSystem.SHSR.Service.DiagnosisService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.concurrent.ExecutionException;

// @RestController
// @RequestMapping("/api/diagnosis") // Base path for all operations related to diagnoses
// public class DiagnosisRequestController {

//     private final DiagnosisService diagnosisService;
//     private final Logger logger = LoggerFactory.getLogger(DiagnosisRequestController.class);


//     //@Autowired
//     public DiagnosisRequestController(DiagnosisService diagnosisService) {
//         this.diagnosisService = diagnosisService;
//     }

//     // Endpoint to add a new diagnosis
//     @PostMapping("/diagnosisRequest")
//     public ResponseEntity<Diagnosis> addDiagnosis(@RequestBody Diagnosis diagnosis) throws ExecutionException, InterruptedException {
//     Diagnosis savedDiagnosis = diagnosisService.saveDiagnosis(diagnosis);
//     return ResponseEntity.ok(savedDiagnosis); // Returns the saved diagnosis
// }


//     // Endpoint to retrieve all diagnoses
//     @GetMapping
//     public ResponseEntity<List<Diagnosis>> getAllDiagnoses() throws ExecutionException, InterruptedException {
//         List<Diagnosis> diagnoses = diagnosisService.getAllDiagnoses();
//         return ResponseEntity.ok(diagnoses); // Returns the list of diagnoses
//     }

// // New endpoint to receive diagnosis data from System B
// @PostMapping("/receiveDiagnosisData")
// public ResponseEntity<String> receiveDiagnosisData(@RequestBody Diagnosis diagnosisData) throws ExecutionException, InterruptedException {
//     // Save the received diagnosis data to the database
//     //Diagnosis savedDiagnosis = diagnosisService.saveDiagnosis(diagnosisData); // Use diagnosisData

//     // Process the diagnosis data and assign it to the patient on CDPRPS
//     logger.info("Received diagnosis data: {}", diagnosisData);

//     String response = "Diagnosis data received and processed on CDPRPS system console.";
//     return ResponseEntity.ok(response);
// }

    
    
    
// }

//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.ProvideDiagnosis;

import com.SmartHealthRemoteSystem.SHSR.Service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosis")
public class DiagnosisRequestController {

    private final DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisRequestController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDiagnosis(@RequestParam String patientId, @RequestBody Diagnosis diagnosis) {
        String result = diagnosisService.saveDiagnosis(patientId, diagnosis);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Diagnosis>> getAllDiagnoses(@RequestParam String patientId) {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses(patientId));
    }

    @GetMapping("/{patientId}/{diagnosisId}")
    public ResponseEntity<Diagnosis> getDiagnosis(@PathVariable String patientId, @PathVariable String diagnosisId) {
        Diagnosis diagnosis = diagnosisService.getDiagnosis(patientId, diagnosisId);
        return diagnosis != null ? ResponseEntity.ok(diagnosis) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDiagnosis(@RequestParam String patientId, @RequestBody Diagnosis updatedDiagnosis) {
        String result = diagnosisService.updateDiagnosis(patientId, updatedDiagnosis);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDiagnosis(@RequestParam String patientId, @RequestParam String diagnosisId) {
        return ResponseEntity.ok(diagnosisService.deleteDiagnosis(patientId, diagnosisId));
    }
}
