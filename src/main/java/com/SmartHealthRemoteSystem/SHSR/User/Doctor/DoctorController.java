// package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

// import com.SmartHealthRemoteSystem.SHSR.Prediction.Prediction;
// import com.SmartHealthRemoteSystem.SHSR.Prediction.PredictionWrapper;
// import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
// import com.SmartHealthRemoteSystem.SHSR.Sensor.Model.Sensor;
// import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
// import com.SmartHealthRemoteSystem.SHSR.Service.PredictionService;
// import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
// import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.util.ArrayList;
// import java.util.Base64;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.concurrent.ExecutionException;
// import java.util.stream.Collectors;

// import com.SmartHealthRemoteSystem.SHSR.Service.PredictionService;

// @Controller
// @RequestMapping("/doctor")
// public class DoctorController {

//     private final DoctorService doctorService;
//     private final PatientService patientService;
//     @Autowired
//     private SensorDataService sensorDataService;
//     @Autowired
//     private PredictionService predictionService;



//     @Autowired
//     public DoctorController(DoctorService doctorService, PatientService patientService) {
//         this.doctorService = doctorService;
//         this.patientService = patientService;
//     }

//     @GetMapping
//     public String getDoctorDashboard(Model model) throws ExecutionException, InterruptedException {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//         Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
//         List<Patient> patientList = doctorService.getListPatient();

//         model.addAttribute("doctor", doctor);
//         model.addAttribute("patientList", patientList);
//         return "doctorDashboard";
//     }

    

//     @GetMapping("/myPatient")
//     public String getPatientListThatAssignedToDoctor(Model model,
//                                                      @RequestParam(defaultValue = "0") int pageNo,
//                                                      @RequestParam(defaultValue = "5") int pageSize,
//                                                      @RequestParam(defaultValue = "") String searchQuery) throws ExecutionException, InterruptedException {
//         model.addAttribute("pageSize", pageSize);
                                                    
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//         Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        

//         List<Patient> allPatients = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());  //error this part

//         if (!searchQuery.isEmpty()) {
//             allPatients = allPatients.stream()
//                     .filter(p -> p.getName().toLowerCase().contains(searchQuery.toLowerCase()) || p.getUserId().contains(searchQuery))
//                     .collect(Collectors.toList());
//         }

//         int total = allPatients.size();
//         int start = Math.min(pageNo * pageSize, total);
//         int end = Math.min((pageNo + 1) * pageSize, total);
//         int startIndex = pageNo * pageSize;

//         List<Patient> patientList = allPatients.subList(start, end);

//         model.addAttribute("startIndex", startIndex);
//         model.addAttribute("currentPage", pageNo);
//         model.addAttribute("totalPages", (total + pageSize - 1) / pageSize);
//         model.addAttribute("patientList", patientList);
//         model.addAttribute("searchQuery", searchQuery);
//         model.addAttribute("doctor", doctor);

//         return "myPatient";
//     }

//    @GetMapping("updateProfile")
// public String showEditProfile(Model model) throws ExecutionException, InterruptedException {
//     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//     Doctor doctor = doctorService.getDoctor(userDetails.getUsername());
//     model.addAttribute("doctor", doctor);
//     return "editProfileDoctor"; // Use this standardized name
// }

// @PostMapping("/updateProfile/profile")
// public String updateProfile(@ModelAttribute Doctor updatedDoctor,
//                             @RequestParam("profileImage") MultipartFile imageFile,
//                             Model model) throws Exception {
//     Doctor existingDoctor = doctorService.getDoctor(updatedDoctor.getUserId());

//     existingDoctor.setName(updatedDoctor.getName());
//     existingDoctor.setContact(updatedDoctor.getContact());

//     if (!imageFile.isEmpty()) {
//         String fileType = imageFile.getContentType();
//         if (fileType != null && fileType.startsWith("image/")) {
//             byte[] imageBytes = imageFile.getBytes();
//             String base64 = Base64.getEncoder().encodeToString(imageBytes);
//             existingDoctor.setProfilePicture(base64);
//             existingDoctor.setProfilePictureType(fileType);
//         }
//     }

//     doctorService.updateDoctor(existingDoctor);
//     return "redirect:/doctor";
// }


// @GetMapping("/sensorDashboard")
// public String viewSensorDashboard(@RequestParam("patientId") String patientId, Model model) throws Exception {
//     Patient patient = patientService.getPatientById(patientId); // Or doctorService.getPatient(patientId);
//     model.addAttribute("patientid", patientId);

//     if (patient.getSensorDataId() == null || patient.getSensorDataId().isEmpty()) {
//         model.addAttribute("sensorDataList", null);
//         model.addAttribute("sensorDataListHistory", null);
//         return "sensorDashboard";
//     }

//     SensorData sensorData = sensorDataService.getSensorById(patient.getSensorDataId());

//     if (sensorData != null) {
//         model.addAttribute("sensorDataList", sensorData);
//         model.addAttribute("sensorDataListHistory", sensorData.getHistory());
//     } else {
//         model.addAttribute("sensorDataList", null);
//         model.addAttribute("sensorDataListHistory", null);
//     }

//     return "sensorDashboard";  // or "HistorySensorDashboard" if you're using that
// }


// //view maunual Diagnosis Request//
// @GetMapping("/manualDiagnosisRequests")
// public String getManualDiagnosisRequests(Model model) throws ExecutionException, InterruptedException {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
//     String doctorId = userDetails.getUsername();

//     // Debug doctorId
//     System.out.println("🩺 Doctor ID (logged in): " + doctorId);

//     List<Patient> allPatients = patientService.getAllPatients();

//     // Debug total patient count
//     System.out.println("🔎 Total patients in system: " + allPatients.size());

//     List<Patient> requests = allPatients.stream()
//         .filter(p -> {
//             boolean match = p.isNeedsManualDiagnosis() && doctorId.equals(p.getAssigned_doctor());
//             // Extra debug inside stream:
//             if (p.isNeedsManualDiagnosis()) {
//                 System.out.println("📌 Patient with request: " + p.getUserId() + " assignedDoctor=" + p.getAssigned_doctor());
//             }
//             return match;
//         })
//         .collect(Collectors.toList());

//     // Debug request count
//     System.out.println("📊 Total requests for doctor: " + requests.size());

//     model.addAttribute("requests", requests);
//     return "manualDiagnosisRequests";
// }

// @GetMapping("/predictionReview")
// public String viewPredictions(Model model) throws ExecutionException, InterruptedException {
//     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
//     String doctorId = userDetails.getUsername();

//     List<Patient> allPatients = patientService.getAllPatients();
//     List<Patient> assignedPatients = allPatients.stream()
//             .filter(p -> doctorId.equals(p.getAssigned_doctor()))
//             .collect(Collectors.toList());

//     Map<Patient, Prediction> latestPredictions = new LinkedHashMap<>();

//     for (Patient patient : assignedPatients) {
//         Optional<Prediction> latestPrediction = predictionService.getRecentPrediction(patient.getUserId());
//         latestPrediction.ifPresent(pred -> latestPredictions.put(patient, pred));
//     }

//     model.addAttribute("latestPredictions", latestPredictions);
//     model.addAttribute("doctor", doctorService.getDoctor(doctorId));
//     return "predictionReview";
// }





// }


package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

import com.SmartHealthRemoteSystem.SHSR.Prediction.Prediction;
import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import com.SmartHealthRemoteSystem.SHSR.Service.DiseaseDescriptionService;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PredictionService;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    @Autowired private SensorDataService sensorDataService;
    @Autowired private PredictionService predictionService;

    @Autowired
    public DoctorController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Autowired
    private DiseaseDescriptionService descriptionService;


    @GetMapping
    public String getDoctorDashboard(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList = doctorService.getListPatient();

        model.addAttribute("doctor", doctor);
        model.addAttribute("patientList", patientList);
        return "doctorDashboard";
    }

    @GetMapping("myPatient")
    public String getPatientListThatAssignedToDoctor(Model model,
                                                     @RequestParam(defaultValue = "0") int pageNo,
                                                     @RequestParam(defaultValue = "5") int pageSize,
                                                     @RequestParam(defaultValue = "") String searchQuery) throws ExecutionException, InterruptedException {
        model.addAttribute("pageSize", pageSize);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<Patient> allPatients = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());

        if (!searchQuery.isEmpty()) {
            allPatients = allPatients.stream()
                    .filter(p -> p.getName().toLowerCase().contains(searchQuery.toLowerCase()) || p.getUserId().contains(searchQuery))
                    .collect(Collectors.toList());
        }

        int total = allPatients.size();
        int start = Math.min(pageNo * pageSize, total);
        int end = Math.min((pageNo + 1) * pageSize, total);
        int startIndex = pageNo * pageSize;

        List<Patient> patientList = allPatients.subList(start, end);

        model.addAttribute("startIndex", startIndex);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", (total + pageSize - 1) / pageSize);
        model.addAttribute("patientList", patientList);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("doctor", doctor);
        return "myPatient";
    }

    @GetMapping("updateProfile")
    public String showEditProfile(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(userDetails.getUsername());
        model.addAttribute("doctor", doctor);
        return "editProfileDoctor";
    }

   @PostMapping("updateProfile/profile")
public String updateProfile(@ModelAttribute Doctor updatedDoctor,
                            @RequestParam("profileImage") MultipartFile imageFile,
                            Model model) throws Exception {
    Doctor existingDoctor = doctorService.getDoctor(updatedDoctor.getUserId());
    existingDoctor.setName(updatedDoctor.getName());
    existingDoctor.setContact(updatedDoctor.getContact());

    if (!imageFile.isEmpty()) {
        String fileType = imageFile.getContentType();
        if (fileType != null && fileType.startsWith("image/")) {
            byte[] imageBytes = imageFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            existingDoctor.setProfilePicture(base64);
            existingDoctor.setProfilePictureType(fileType);
        }
    }

    doctorService.updateDoctor(existingDoctor);
    return "redirect:/doctor";
}


    @GetMapping("/sensorDashboard")
public String viewSensorDashboard(@RequestParam("patientId") String patientId, Model model) throws Exception {
    Patient patient = patientService.getPatientById(patientId);
    model.addAttribute("patientid", patientId);

    if (patient.getSensorDataId() == null || patient.getSensorDataId().isEmpty()) {
        model.addAttribute("sensorData", null);
        model.addAttribute("sensorDataPage", null);
        model.addAttribute("latestFiveReadings", null);
        model.addAttribute("role", "doctor"); // ✅ this is needed for the back button
        return "sensorDashboard";
    }

    SensorData sensorData = sensorDataService.getSensorById(patient.getSensorDataId());
    model.addAttribute("sensorData", sensorData);
    model.addAttribute("sensorDataPage", sensorData.getHistory());
    model.addAttribute("latestFiveReadings", sensorData.getHistory().stream()
        .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
        .limit(5)
        .toList());
    model.addAttribute("role", "doctor"); // ✅ make sure this is passed
    return "sensorDashboard";
}


    @GetMapping("manualDiagnosisRequests")
    public String getManualDiagnosisRequests(Model model) throws ExecutionException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String doctorId = userDetails.getUsername();

        List<Patient> allPatients = patientService.getAllPatients();
        List<Patient> requests = allPatients.stream()
            .filter(p -> p.isNeedsManualDiagnosis() && doctorId.equals(p.getAssigned_doctor()))
            .collect(Collectors.toList());

        model.addAttribute("requests", requests);
        return "manualDiagnosisRequests";
    }

   
    @GetMapping("predictionReview")
    public String viewPredictions(
        @RequestParam(defaultValue = "") String searchQuery,
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "5") int pageSize,    
         Model model) throws ExecutionException, InterruptedException {
   
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
            String doctorId = userDetails.getUsername();

    List<Patient> allPatients = patientService.getAllPatients();
    List<Patient> assignedPatients = allPatients.stream()
            .filter(p -> doctorId.equals(p.getAssigned_doctor()))
            .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(searchQuery.toLowerCase())) // 👈 Filter by name
            .collect(Collectors.toList());

    List<Map.Entry<Patient, Prediction>> filteredEntries = new ArrayList<>();
    // Map<Patient, Prediction> latestPredictions = new LinkedHashMap<>();
    Map<String, String> descriptionMap = new HashMap<>();

    for (Patient patient : assignedPatients) {
        Optional<Prediction> latestPrediction = predictionService.getRecentPrediction(patient.getUserId());
        if (latestPrediction.isPresent()) {
            Prediction pred = latestPrediction.get();
            filteredEntries.add(new AbstractMap.SimpleEntry<>(patient, pred));
            // latestPredictions.put(patient, pred);

            for (String disease : pred.getDiagnosisList()) {
                descriptionMap.put(disease, descriptionService.getDescription(disease));
            }
        }
    }

    // 🔄 Pagination
    int total = filteredEntries.size();
    int start = Math.min(pageNo * pageSize, total);
    int end = Math.min(start + pageSize, total);
    List<Map.Entry<Patient, Prediction>> paginatedEntries = filteredEntries.subList(start, end);


    Map<Patient, Prediction> latestPredictions = paginatedEntries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
   
            model.addAttribute("latestPredictions", latestPredictions);
            model.addAttribute("descriptionMap", descriptionMap);
            model.addAttribute("doctor", doctorService.getDoctor(doctorId));
            model.addAttribute("searchQuery", searchQuery);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", (int) Math.ceil((double) total / pageSize));
    
            return "predictionReview";
}

@GetMapping("/predictionHistory")
public String viewPredictionHistory(
        @RequestParam("patientId") String patientId,
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "5") int pageSize,
        Model model) throws ExecutionException, InterruptedException {

    Patient patient = patientService.getPatientById(patientId);
    List<Prediction> predictions = predictionService.getPatientPredictions(patientId);

    int total = predictions.size();
    int start = Math.min(pageNo * pageSize, total);
    int end = Math.min((pageNo + 1) * pageSize, total);
    int totalPages = (total + pageSize - 1) / pageSize;

    List<Prediction> pagedPredictions = predictions.subList(start, end);

    model.addAttribute("patient", patient);
    model.addAttribute("predictions", pagedPredictions);
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("patientId", patientId);
    return "predictionHistory";
}



}

