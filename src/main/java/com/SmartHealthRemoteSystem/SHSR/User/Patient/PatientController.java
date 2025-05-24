//Firestore//

// package com.SmartHealthRemoteSystem.SHSR.User.Patient;

// // import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
// // import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
// // import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
// // import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
// // import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
// // import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;
// import com.google.api.core.ApiFuture;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.FieldValue;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.Query;
// import com.google.cloud.firestore.QueryDocumentSnapshot;
// import com.google.cloud.firestore.QuerySnapshot;
// import com.google.cloud.firestore.Query.Direction;
// import com.google.firebase.cloud.FirestoreClient;
// // import javax.persistence.EntityManager;

// import com.google.api.core.ApiFuture;
// import com.google.cloud.Timestamp;
// import com.google.cloud.firestore.DocumentReference;
// import com.google.cloud.firestore.DocumentSnapshot;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.QueryDocumentSnapshot;
// import com.google.cloud.firestore.QuerySnapshot;
// import com.google.cloud.storage.Acl.Entity;

// import java.util.ArrayList;

// import java.util.concurrent.ExecutionException;
// import java.util.Base64;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;

// @Controller
// @RequestMapping("/patient")
// public class PatientController {

//     private final PatientService patientService;
//     // private final DoctorService doctorService;
//    /*  @Autowired    
//         EntityManager entityManager; */

//     @Autowired
//     public PatientController(PatientService patientService, DoctorService doctorService) {
//         this.patientService = patientService;
//         // this.doctorService = doctorService;
//     }

//     @GetMapping
//     public String getPatientDashboard(Model model) throws ExecutionException, InterruptedException {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//         Patient patient = patientService.getPatient(myUserDetails.getUsername());
//         // Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());
//         // List<Patient> patientList = patientService.getPatientList();

//         model.addAttribute("patient", patient);
//         // model.addAttribute("doctor", doctor);
//         // model.addAttribute("patientList", patientList);

//         return "patientDashBoard";
//     }

//     // @GetMapping("/editProfile")
//     // public String editUserProfile( Model model) throws ExecutionException, InterruptedException{
        
//     //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     //     MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//     //     Patient patient = patientService.getPatient(myUserDetails.getUsername());
//     //     Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());
//     //     List<Patient> patientList = patientService.getPatientList();

//     //     model.addAttribute("patient", patient);
//     //     model.addAttribute("doctor", doctor);
//     //     model.addAttribute("patientList", patientList);
  
//     //     return "test";
//     // }

//     // @PostMapping("/editProfile/submit")
//     // public String submitUserProfile(@ModelAttribute Patient patient) throws ExecutionException, InterruptedException{
        
        
//     //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     //     MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
//     //     patient.setUserId(myUserDetails.getUsername());
//     //     patientService.updatePatient(patient);
        
        
       
//     //     return "test";

        
//     // }

//     // @GetMapping("/viewPrescription")
//     // public String getPatientListThatAssignedToDoctor(Model model, @RequestParam(value = "pageNo") int pageNo) throws ExecutionException, InterruptedException {


//     //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     //     MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();

//     //     Patient patient = patientService.getPatient(myUserDetails.getUsername());
//     //     Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());
        
//     //     ArrayList<Prescription> prescriptionTemp = (ArrayList<Prescription>) patientService.getAllPrescription(patient.getUserId());

//     //     ArrayList<Prescription> prescriptionList = quickSort(prescriptionTemp);

//     //     Prescription prescription = null;
//     //     if ((!prescriptionList.isEmpty()) && pageNo == -1) {
//     //         prescription = prescriptionList.get(prescriptionList.size() - 1);
//     //         pageNo = prescriptionList.size() - 1;
//     //     } else if (!prescriptionList.isEmpty()) {
//     //         prescription = prescriptionList.get(pageNo);
//     //     }
//     //     int totalPage = prescriptionList.size();

//     //     model.addAttribute("patient", patient);
//     //     model.addAttribute("doctor", doctor);
//     //     model.addAttribute("prescription", prescription);
//     //     model.addAttribute("currentPage", pageNo);
//     //     model.addAttribute("totalPage", totalPage);

//     //     return "viewPrescription";
//     // }

//     // @PostMapping("/backDashboard")
//     // public String backDashboard(@RequestParam(value = "patientId") String patientId, Model model) throws ExecutionException, InterruptedException {
//     //     Patient patient = patientService.getPatient(patientId);
//     //     Doctor doctor = doctorService.getDoctor(patient.getAssigned_doctor());
//     //     model.addAttribute("patient", patient);
//     //     model.addAttribute("doctor", doctor);
//     //     return "patientDashBoard";
//     // }

//     // @GetMapping("/list") 
//     // public String ListAssignedPatient(Model model)throws ExecutionException, InterruptedException{
//     //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     //     MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
//     //     Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
//     //     List<Patient> patientList = patientService.getPatientList();
//     //     //Remove unassigned patients
//     //     for(int i=patientList.size()-1;i>=0;i--)
//     //     {
//     //         if ((patientList.get(i).getAssigned_doctor().isEmpty())){
//     //             patientList.remove(i);
//     //         }
//     //     }
//     //     model.addAttribute("patientList", patientList);
//     //     model.addAttribute("doctor", doctor);
//     //     return "listAssignedPatient";
//     // }

//     //      //SENSOR DASHBOARD FOR PATIENT VIEW // 
//     //      @GetMapping("/sensorDashboard")
//     //     public String getSensorDashboard(Model model, @RequestParam(value= "patientId") String patientId) throws Exception {
//     //     Firestore firestore = FirestoreClient.getFirestore();
//     //       Patient patient = doctorService.getPatient(patientId);
//     //       SensorDataService sensorDataService = new SensorDataService();
//     //       SensorData sensorData = sensorDataService.getSensorData(patient.getSensorDataId());
        
//     //       Query query = firestore.collection("SensorData")
//     //       .document(patient.getSensorDataId())
//     //       .collection("SensorDataHistory").orderBy("#", Direction.DESCENDING).limit(1);
//     //       ApiFuture<QuerySnapshot> querySnapshot = query.get();
//     //       List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
//     //       int highestDocumentNumber = 0;
//     //       if (!documents.isEmpty()) {
//     //         highestDocumentNumber = documents.get(0).getLong("#").intValue();
//     //       }
          
//     //       // Create a new document with the next document number
//     //       DocumentReference docRef = firestore.collection("SensorData")
//     //       .document(patient.getSensorDataId())
//     //       .collection("SensorDataHistory")
//     //       .document("sensordata" + String.format("%03d", highestDocumentNumber + 1));
  
//     // // Populate the document with the sensor data fields
//     //       Map<String, Object> data = new HashMap<>();
//     //       data.put("#", highestDocumentNumber +1 );
//     //       data.put("Heart_Rate", sensorData.getHeart_Rate());
//     //       data.put("bodyTemperature", sensorData.getBodyTemperature());
//     //       data.put("ecgReading", sensorData.getEcgReading());
//     //       data.put("oxygenReading", sensorData.getOxygenReading());
//     //       data.put("sensorDataId", sensorData.getSensorDataId());
//     //       data.put("timestamp", sensorData.getTimestamp());
  
//     //       model.addAttribute("sensorDataList",sensorData);
//     //       model.addAttribute("patientid",patientId);
  
//     //       // Write the data to the document
//     //       docRef.set(data);

         
       
//     //     Iterable<DocumentReference> documentReference = firestore.collection("SensorData")
//     //     .document(patient.getSensorDataId())
//     //     .collection("SensorDataHistory").listDocuments();
//     //     Iterator<DocumentReference> iterator = documentReference.iterator();

//     //     List<SensorData> sensorDataList = new ArrayList<>();
//     //     SensorData sensorDatahistory;
//     //     while (iterator.hasNext()) {
//     //         DocumentReference documentReference1=iterator.next();
//     //         ApiFuture<DocumentSnapshot> future = documentReference1.get();
//     //         DocumentSnapshot document = future.get();
//     //         sensorData = document.toObject(SensorData.class);
//     //         sensorDataList.add(sensorData);
//     //         model.addAttribute("sensorDataListHistory",sensorDataList);
//     //         System.out.println("-------------------------------------------------------------------------------");
//     //         System.out.println(sensorDataList);
//     //         System.out.println("-------------------------------------------------------------------------------");

//     //     }

//     //     model.addAttribute("success","success");
       
//     //       return "sensorDashboard";
//     //   }

    


//     //   //Prescription//
//     // public ArrayList<Prescription> quickSort(ArrayList<Prescription> list)
//     // {
//     //     if (list.isEmpty())
//     //         return list;
//     //     ArrayList<Prescription> sorted;
//     //     ArrayList<Prescription> smaller = new ArrayList<Prescription>();
//     //     ArrayList<Prescription> greater = new ArrayList<Prescription>();
//     //     Prescription pivot = list.get(0);
//     //     int i;
//     //     Prescription j;
//     //     for (i=1;i<list.size();i++)
//     //     {
//     //         j=list.get(i);
//     //         if (compare(j,pivot)<0)
//     //             smaller.add(j);
//     //         else
//     //             greater.add(j);
//     //     }
//     //     smaller=quickSort(smaller);
//     //     greater=quickSort(greater);
//     //     smaller.add(pivot);
//     //     smaller.addAll(greater);
//     //     sorted = smaller;

//     //     return sorted;
//     // }

//     // int compare(Prescription obj1, Prescription obj2){
//     //     String ts1 = obj1.getTimestamp();
//     //     String ts2 = obj2.getTimestamp();
//     //     Timestamp timestamp1 = Timestamp.parseTimestamp(ts1);
//     //     Timestamp timestamp2 = Timestamp.parseTimestamp(ts2);
//     //     return timestamp1.compareTo(timestamp2);

//     // }

// }

//V1//

// package com.SmartHealthRemoteSystem.SHSR.User.Patient;

// import java.util.Optional;
// import java.util.concurrent.ExecutionException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;

// @RestController
// @RequestMapping("/patients")
// @CrossOrigin(origins = "*")  // Enable CORS (if needed)
// public class PatientController {
//     private final PatientService patientService;
//     private final BCryptPasswordEncoder passwordEncoder;

//     @Autowired
//     public PatientController(PatientService patientService, BCryptPasswordEncoder passwordEncoder) {
//         this.patientService = patientService;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @PostMapping("/register")
//     public ResponseEntity<?> registerPatient(@RequestBody Patient patient) throws ExecutionException, InterruptedException {
//         // Check if the patient already exists
//         Optional<Patient> existingPatient = patientService.getPatientByEmail(patient.getEmail());
//         if (existingPatient.isPresent()) {
//             return ResponseEntity.badRequest().body("Error: Email is already registered.");
//         }

//         // Hash the password before saving
//         patient.setPassword(passwordEncoder.encode(patient.getPassword()));

//         // Save the new patient
//         String savedResponse = patientService.createPatient(patient); 

//         return ResponseEntity.status(201).body(savedResponse); // 201 = Created
//     }

//     @GetMapping("/get")
//     public ResponseEntity<?> getPatient(@RequestParam String email) {
//         Optional<Patient> patient = patientService.getPatientByEmail(email);
        
//         if (patient.isPresent()) {
//             return ResponseEntity.ok(patient.get());  // Return patient data
//         } else {
//             return ResponseEntity.status(404).body("Patient not found"); // Return error as a string
//         }
//     }
// }

//MONGODB//
//V2//
package com.SmartHealthRemoteSystem.SHSR.User.Patient;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

     @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    // After login → go here
    @GetMapping
    public String getPatientDashboard(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    
        String patientId = myUserDetails.getUsername();
    
        // Fetch patient from MongoDB
        Patient patient = patientService.getPatientById(patientId);
    

        System.out.println("🖼️ Profile Picture Length: " + (patient.getProfilePicture() != null ? patient.getProfilePicture().length() : "null"));
        System.out.println("🖼️ Profile Picture Type: " + patient.getProfilePictureType());

        System.out.println("🔍 Checking Patient: " + patient);
        if (patient == null) {
            model.addAttribute("error", "Patient not found.");
            return "error"; // Show error.html
        }
    
        Doctor doctor = null;
        if (patient.getAssigned_doctor() != null && !patient.getAssigned_doctor().isEmpty()) {
            doctor = doctorService.getDoctor(patient.getAssigned_doctor());
        }
    
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
    
        return "patientDashboard";
    }
    

    @GetMapping("/editProfile")
   public String editProfile(Model model) throws ExecutionException, InterruptedException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

    Patient patient = patientService.getPatientById(userDetails.getUsername());
    model.addAttribute("patient", patient);
    return "editPatientProfile";
}

@PostMapping("/editProfile/submit")
public String updatePatientProfile(@ModelAttribute Patient updatedPatient,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   Model model) throws Exception {
    // ✅ Fetch the original patient from DB
    Patient existingPatient = patientService.getPatientById(updatedPatient.getUserId());

    if (existingPatient == null) {
        model.addAttribute("error", "Patient not found.");
        return "editPatientProfile";
    }

    // ✅ Overwrite fields that are allowed to change
    existingPatient.setName(updatedPatient.getName());
    existingPatient.setContact(updatedPatient.getContact());
    existingPatient.setAddress(updatedPatient.getAddress());
    existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());

    // ✅ Handle image update
    if (!imageFile.isEmpty()) {
    String fileType = imageFile.getContentType();
    if (!fileType.startsWith("image/")) {
        model.addAttribute("error", "Only image files are allowed.");
        return "editPatientProfile";
    }


    System.out.println("📸 File name: " + imageFile.getOriginalFilename());
    System.out.println("📸 File type: " + imageFile.getContentType());
    System.out.println("📸 File size: " + imageFile.getSize());


    byte[] imageBytes = imageFile.getBytes();
    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

    existingPatient.setProfilePicture(base64Image);
    existingPatient.setProfilePictureType(fileType); // ✅ SAVE TYPE

}

    // ✅ Save updated patient
    patientService.updatePatient(existingPatient);

    return "redirect:/patient";
}




    
    @GetMapping("/sensorDashboard")
public String viewSensorDashboard(@RequestParam("patientId") String patientId, Model model) throws Exception {
    Patient patient = patientService.getPatientById(patientId);
    model.addAttribute("patientid", patientId);

    if (patient.getSensorDataId() == null || patient.getSensorDataId().isEmpty()) {
        model.addAttribute("sensorDataList", null);
        model.addAttribute("sensorDataListHistory", null);
        return "sensorDashboard";
    }

    SensorData sensorData = sensorDataService.getSensorById(patient.getSensorDataId());

    if (sensorData != null) {
        model.addAttribute("sensorDataList", sensorData);
        model.addAttribute("sensorDataListHistory", sensorData.getHistory());
    } else {
        model.addAttribute("sensorDataList", null);
        model.addAttribute("sensorDataListHistory", null);
    }

    return "sensorDashboard";
}



    //Pagination//
   @GetMapping("/list")
public String getAllPatientsWithPagination(Model model,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "5") int pageSize) throws ExecutionException, InterruptedException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    String doctorId = myUserDetails.getUsername();

    List<Patient> allPatients = patientService.getAllPatients();

    // ❗ Filter only those assigned to the current doctor
    List<Patient> assignedPatients = allPatients.stream()
            .filter(p -> doctorId.equals(p.getAssigned_doctor()))
            .collect(Collectors.toList());

    int total = assignedPatients.size();
    int start = Math.min(pageNo * pageSize, total);
    int end = Math.min((pageNo + 1) * pageSize, total);
    int startIndex = pageNo * pageSize;

    List<Patient> patientList = assignedPatients.subList(start, end);

    model.addAttribute("startIndex", startIndex);
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalPages", (total + pageSize - 1) / pageSize);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("patientList", patientList);
    model.addAttribute("doctor", patientService.getDoctor(doctorId)); // ✅ Make sure this method exists

    return "listAssignedPatient";
}


//ViewPrescription//
@GetMapping("/viewPrescription")
    public String viewLatestPrescription(@RequestParam(defaultValue = "0") int pageNo,
                                         Model model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        String patientId = userDetails.getUsername();

        Patient patient = patientService.getPatientById(patientId);
        Map<String, Prescription> prescriptions = patient.getPrescription();

        if (prescriptions.isEmpty()) {
            model.addAttribute("error", "No prescription found.");
            return "viewPrescription";
        }

        Prescription latestPrescription = prescriptions.values().stream()
            .sorted(Comparator.comparing(Prescription::getTimestamp).reversed())
            .findFirst()
            .orElse(null);

        Doctor doctor = doctorService.getDoctor(latestPrescription.getDoctorId());

        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("prescription", latestPrescription);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", 1);

        return "viewPrescription";
    }


    //BackController//
    @PostMapping("/backDashboard")
public String backToDashboard(@RequestParam("patientId") String patientId, Model model) throws Exception {
    Patient patient = patientService.getPatientById(patientId);

    Doctor doctor = null;
    if (patient.getAssigned_doctor() != null && !patient.getAssigned_doctor().isEmpty()) {
        doctor = doctorService.getDoctor(patient.getAssigned_doctor());
    }

    model.addAttribute("patient", patient);
    model.addAttribute("doctor", doctor);

    return "patientDashboard";
}




// @GetMapping("/sensorDashboard")
    // public String viewSensorDashboard(@RequestParam("patientId") String patientId, Model model) throws ExecutionException, InterruptedException {
    //     Patient patient = patientService.getPatientById(patientId);
    //     model.addAttribute("patient", patient);
    //     return "sensorDashboard";
    // }
  
    // @GetMapping("/sensorDashboard")
    // public String viewSensorDashboard(@RequestParam("patientId") String patientId, Model model) throws ExecutionException, InterruptedException {
    //     Patient patient = patientService.getPatientById(patientId);
    //     Sensor sensorData = sensorDataService.getSensorById(patient.getSensorDataId());

    //     if (sensorData == null) {
    //         model.addAttribute("error", "No sensor data found.");
    //         return "sensorDashboard";
    //     }

    //     List<HistorySensorData> history = sensorData.getHistory();

    //     model.addAttribute("sensorData", sensorData);
    //     model.addAttribute("sensorDataListHistory", history);
    //     model.addAttribute("patientid", patientId);

    //     return "sensorDashboard";
    // }



}
