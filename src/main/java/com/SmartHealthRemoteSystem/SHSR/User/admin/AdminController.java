// package com.SmartHealthRemoteSystem.SHSR.User.admin;

// import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
// import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// import com.SmartHealthRemoteSystem.SHSR.User.Pharmacist.Pharmacist;
// import com.SmartHealthRemoteSystem.SHSR.Mail.MailStructure;
// import com.SmartHealthRemoteSystem.SHSR.Pagination.PaginationInfo;
// import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
// import com.SmartHealthRemoteSystem.SHSR.Service.MailService;
// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
// import com.SmartHealthRemoteSystem.SHSR.Service.PharmacistService;
// import com.SmartHealthRemoteSystem.SHSR.Service.UserService;
// import com.SmartHealthRemoteSystem.SHSR.User.User;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.List;
// import java.util.concurrent.ExecutionException;

// @Controller
// @RequestMapping("/admin")
// public class AdminController {

//     private final UserService userService;
//     private final PatientService patientService;
//     private final DoctorService doctorService;
//     private final PharmacistService pharmacistService;
//     private final MailService mailService;

//     public AdminController(UserService userService, PatientService patientService, DoctorService doctorService, PharmacistService pharmacistsService, MailService mailService) {
//         this.userService = userService;
//         this.patientService = patientService;
//         this.doctorService = doctorService;
//         this.pharmacistService = pharmacistsService;
//         this.mailService = mailService;
//     }

//     @GetMapping
//     public String getAdminDashboard(Model model,
//     @RequestParam(value = "pageNo", required = false, defaultValue = "1")int pageNo) throws ExecutionException, InterruptedException {
//         List<User> adminList = userService.getAdminList();
//         List<Patient> patientList = patientService.getPatientList();
//         List<Doctor> doctorList = doctorService.getListDoctor();
//         List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();

//          // Set Pagination details for each list
//         PaginationInfo patientPagination = getPaginationInfo(patientList, pageNo);
//         PaginationInfo adminPagination = getPaginationInfo(adminList, pageNo);
//         PaginationInfo doctorPagination = getPaginationInfo(doctorList, pageNo);
//         PaginationInfo pharmacistPagination = getPaginationInfo(pharmacistList, pageNo);

//         model.addAttribute("adminList", adminPagination.getDataToDisplay());
//         model.addAttribute("adminPagination", adminPagination);
//         model.addAttribute("patientList", patientPagination.getDataToDisplay());
//         model.addAttribute("patientPagination", patientPagination);
//         model.addAttribute("doctorList", doctorPagination.getDataToDisplay());
//         model.addAttribute("doctorPagination", doctorPagination);
//         model.addAttribute("pharmacistList", pharmacistPagination.getDataToDisplay());
//         model.addAttribute("pharmacistPagination", pharmacistPagination);
        
//         return "adminDashboard";
//     }

//     @PostMapping("/adduser")
//     public String saveUserInformation(@RequestParam(value = "userId") String id,
//                                       @RequestParam(value = "userFullName") String name,
//                                       @RequestParam(value = "userPassword") String password,
//                                       @RequestParam(value = "userEmail") String email,
//                                       @RequestParam(value = "contact") String contact,
//                                       @RequestParam(value = "role") String role,
//                                       @RequestParam(value = "address") String patientAddress,
//                                       @RequestParam(value = "emergencyContact") String emergencyContact,
//                                       @RequestParam(value = "doctorHospital") String doctorHospital,
//                                       @RequestParam(value = "doctorPosition") String doctorPosition,
//                                       @RequestParam(value = "pharmacistHospital") String pharmacistHospital,
//                                       @RequestParam(value = "pharmacistPosition") String pharmacistPosition,
//                                       @RequestParam(value="sensorId")String sensorId,
//                                       @RequestParam(value = "action") String action,
//                                       Model model,@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) throws ExecutionException, InterruptedException {
       
//         String Message;
//         if(action.equals("add")){
//             if(role.equals("PATIENT")){
//                 Message =  patientService.createPatient(new Patient(id,name,password,contact,role,email,"",patientAddress,emergencyContact,"","Under Surveillance"));
//             } else if(role.equals("DOCTOR")){
//                 Message = doctorService.createDoctor(new Doctor(id,name,password,contact,role,email, doctorHospital, doctorPosition));
//             } else if(role.equals("PHARMACIST")){
//                 Message = pharmacistService.createPharmacist(new Pharmacist(id,name,password,contact,role,email, pharmacistHospital, pharmacistPosition));
//             } else {
//                 Message =userService.createUser(new User(id,name,password,contact,role,email));
//             }
//             if (!Message.startsWith("Error")) {
//                 // If successful, send the email
//                 sendMail(email,password );
                
                
//             }
//         }else{
//             User user = new User(id,name,password,contact,role);
//             if(role.equals("PATIENT")){
//                 Patient patient = new Patient(id,name,password,contact,role, email,patientAddress,emergencyContact,sensorId);
//                 Message = patientService.updatePatient(patient);
//             } else if(role.equals("DOCTOR")){
//                 Message = doctorService.updateDoctor(new Doctor(id,name,password,contact,role, email, doctorHospital, doctorPosition));
//             } else if(role.equals("PHARMACIST")){
//                 Message = pharmacistService.updatePharmacist(new Pharmacist(id,name,password,contact,role, email, pharmacistHospital, pharmacistPosition));
//             } else {
//                 Message = userService.updateUser(new User(id,name,password,contact,role));
//             }
//         }
        
//         //get current timestamp for lastupdate
//         Date timestamp = new Date();

//         // Format the timestamp
//         SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy HH:mm");
//         String formattedTimestamp = dateFormat.format(timestamp);

//         // Set timestamp as message too
//         Message += " at " + formattedTimestamp;

//         List<User> adminList = userService.getAdminList();
//         List<Patient> patientList = patientService.getPatientList();
//         List<Doctor> doctorList = doctorService.getListDoctor();
//         List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();
//         model.addAttribute("message", Message);
//         model.addAttribute("adminList", adminList);
//         model.addAttribute("patientList", patientList);
//         model.addAttribute("doctorList", doctorList);
//         model.addAttribute("pharmacistList", pharmacistList);

//          // Set Pagination details for each list
//          PaginationInfo patientPagination = getPaginationInfo(patientList, pageNo);
//          PaginationInfo adminPagination = getPaginationInfo(adminList, pageNo);
//          PaginationInfo doctorPagination = getPaginationInfo(doctorList, pageNo);
//          PaginationInfo pharmacistPagination = getPaginationInfo(pharmacistList, pageNo);
 
//          model.addAttribute("adminList", adminPagination.getDataToDisplay());
//          model.addAttribute("adminPagination", adminPagination);
//          model.addAttribute("patientList", patientPagination.getDataToDisplay());
//          model.addAttribute("patientPagination", patientPagination);
//          model.addAttribute("doctorList", doctorPagination.getDataToDisplay());
//          model.addAttribute("doctorPagination", doctorPagination);
//          model.addAttribute("pharmacistList", pharmacistPagination.getDataToDisplay());
//          model.addAttribute("pharmacistPagination", pharmacistPagination);

//         return "adminDashboard";
//     }

//     private void sendMail(String email, String password) {
//         var to = email;
//         var mailStructure = new MailStructure(to,password);
//         mailService.sendNewUserMail(to, mailStructure);
//     }


//     @DeleteMapping("/deleteuser")
//     public String deleteSelectedUser(@RequestParam("userIdToBeDelete") String userId,
//                                      @RequestParam("userRoleToBeDelete") String role,
//                                      Model model, @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) throws ExecutionException, InterruptedException {
//         String message;
//         if(role.equals("PATIENT")){
//             System.out.println(userId);
//             message = patientService.deletePatient(userId);
//         } else if(role.equals("DOCTOR")){
//             message = doctorService.deleteDoctor(userId);
//         } else if(role.equals("PHARMACIST")){
//             message = pharmacistService.deletePharmacist(userId);
//         }else {
//             message = userService.deleteUser(userId);
//         }

//          //get current timestamp for lastupdate
//         Date timestamp = new Date();

//         // Format the timestamp
//         SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy HH:mm");
//         String formattedTimestamp = dateFormat.format(timestamp);

//         // Set timestamp as message too
//         message += " at " + formattedTimestamp;

//         List<User> adminList = userService.getAdminList();
//         List<Patient> patientList = patientService.getPatientList();
//         List<Doctor> doctorList = doctorService.getListDoctor();
//         List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();
//         model.addAttribute("message", message);
//         model.addAttribute("adminList", adminList);
//         model.addAttribute("patientList", patientList);
//         model.addAttribute("doctorList", doctorList);
//         model.addAttribute("pharmacistList", pharmacistList);

//         // Set Pagination details for each list
//         PaginationInfo patientPagination = getPaginationInfo(patientList, pageNo);
//         PaginationInfo adminPagination = getPaginationInfo(adminList, pageNo);
//         PaginationInfo doctorPagination = getPaginationInfo(doctorList, pageNo);
//         PaginationInfo pharmacistPagination = getPaginationInfo(pharmacistList, pageNo);

//         model.addAttribute("adminList", adminPagination.getDataToDisplay());
//         model.addAttribute("adminPagination", adminPagination);
//         model.addAttribute("patientList", patientPagination.getDataToDisplay());
//         model.addAttribute("patientPagination", patientPagination);
//         model.addAttribute("doctorList", doctorPagination.getDataToDisplay());
//         model.addAttribute("doctorPagination", doctorPagination);
//         model.addAttribute("pharmacistList", pharmacistPagination.getDataToDisplay());
//         model.addAttribute("pharmacistPagination", pharmacistPagination);

//         return "adminDashboard";
//     }
//     private PaginationInfo getPaginationInfo(List<?> dataList, int pageNo){
//         int pageSize = 5;
//         int totalItems = dataList.size();
//         int totalPages = (int) Math.ceil((double) totalItems / pageSize);
    
//         // Adjust pageNo to be within valid bounds
//         pageNo = Math.max(1, Math.min(pageNo, totalPages));
    
//         int start = (pageNo - 1) * pageSize;
//         int end = Math.min(start + pageSize, totalItems);
    
//         List<?> dataToDisplay = dataList.subList(start, end);
    
//         int prevPage = Math.max(1, pageNo - 1);
//         int nextPage = Math.min(totalPages, pageNo + 1);
    
//         return new PaginationInfo(dataToDisplay, pageSize, pageNo, totalPages, prevPage, nextPage);
//     }
// }

package com.SmartHealthRemoteSystem.SHSR.User.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SmartHealthRemoteSystem.SHSR.Pagination.PaginationInfo;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PharmacistService;
import com.SmartHealthRemoteSystem.SHSR.Service.UserService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Pharmacist.Pharmacist;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final PharmacistService pharmacistService;

    public AdminController(UserService userService, PatientService patientService,
                           DoctorService doctorService, PharmacistService pharmacistService) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.pharmacistService = pharmacistService;
    }

    @GetMapping
public String getAdminDashboard(Model model,
        @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) throws ExecutionException, InterruptedException {

            System.out.println("✅ AdminController: Dashboard accessed");
    List<User> adminList = userService.getAdminList();
    List<Patient> patientList = patientService.getAllPatients(); // ✅ Add this
    System.out.println("✅ Retrieved " + patientList.size() + " patients");
    List<Doctor> doctorList = doctorService.getAllDoctors();     // ✅ Add this
    List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist(); // ✅ Add this

    PaginationInfo adminPagination = getPaginationInfo(adminList, pageNo);
    PaginationInfo patientPagination = getPaginationInfo(patientList, pageNo);
    PaginationInfo doctorPagination = getPaginationInfo(doctorList, pageNo);
    PaginationInfo pharmacistPagination = getPaginationInfo(pharmacistList, pageNo);
    // PaginationInfo allUsersPagination = getPaginationInfo(userService.getAllUsersWithDetails(), pageNo);
    // model.addAttribute("allUsers", allUsersPagination.getDataToDisplay());
    // model.addAttribute("allUsersPagination", allUsersPagination);
    
    model.addAttribute("adminList", adminPagination.getDataToDisplay());
    model.addAttribute("adminPagination", adminPagination);
    model.addAttribute("patientList", patientPagination.getDataToDisplay());
    model.addAttribute("patientPagination", patientPagination);
    model.addAttribute("doctorList", doctorPagination.getDataToDisplay());
    model.addAttribute("doctorPagination", doctorPagination);
    model.addAttribute("pharmacistList", pharmacistPagination.getDataToDisplay());
    model.addAttribute("pharmacistPagination", pharmacistPagination);

    return "adminDashboard";
}

    


    @PostMapping("/adduser")
    public String saveUserInformation(@RequestParam("userId") String userId,
                                      @RequestParam("userFullName") String name,
                                      @RequestParam("userPassword") String password,
                                      @RequestParam("userEmail") String email,
                                      @RequestParam("contact") String contact,
                                      @RequestParam("role") String role,
                                      @RequestParam(value = "address", required = false) String address,
                                      @RequestParam(value = "emergencyContact", required = false) String emergencyContact,
                                      @RequestParam(value = "sensorId", required = false) String sensorId,
                                      @RequestParam(value = "doctorHospital", required = false) String doctorHospital,
                                      @RequestParam(value = "doctorPosition", required = false) String doctorPosition,
                                      @RequestParam(value = "pharmacistHospital", required = false) String pharmacistHospital,
                                      @RequestParam(value = "pharmacistPosition", required = false) String pharmacistPosition,
                                      @RequestParam("action") String action,
                                      Model model
                                      // , @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo
    ) throws ExecutionException, InterruptedException {

        String message = "";

        if (action.equalsIgnoreCase("add")) {
            switch (role) {
                case "PATIENT":
                    Patient patient = new Patient(userId, name, password, contact, role, email,
                            address, emergencyContact, sensorId, "", "Under Surveillance");
                    message = patientService.createPatient(patient);
                    break;

                case "DOCTOR":
                    Doctor doctor = new Doctor(userId, name, password, contact, role, email, doctorHospital, doctorPosition);
                    message = doctorService.saveDoctor(doctor);
                    break;

                case "PHARMACIST":
                    Pharmacist pharmacist = new Pharmacist(userId, name, password, contact, role, email, pharmacistHospital, pharmacistPosition);
                    message = pharmacistService.createPharmacist(pharmacist);
                    break;

                default:
                    User user = new User(userId, name, password, contact, role, email);
                    message = userService.createUser(user);
                    break;
            }
        }

        String timestamp = new SimpleDateFormat("MM dd yyyy HH:mm").format(new Date());
        message += " at " + timestamp;

        model.addAttribute("message", message);

        // Refresh lists after adding user
List<User> adminList = userService.getAdminList();
List<Patient> patientList = patientService.getAllPatients();
List<Doctor> doctorList = doctorService.getAllDoctors();
List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();

// Now apply pagination to updated lists
PaginationInfo adminPagination = getPaginationInfo(adminList, 1);
PaginationInfo patientPagination = getPaginationInfo(patientList, 1);
PaginationInfo doctorPagination = getPaginationInfo(doctorList, 1);
PaginationInfo pharmacistPagination = getPaginationInfo(pharmacistList, 1);

// Add to model
model.addAttribute("adminList", adminPagination.getDataToDisplay());
model.addAttribute("adminPagination", adminPagination);
model.addAttribute("patientList", patientPagination.getDataToDisplay());
model.addAttribute("patientPagination", patientPagination);
model.addAttribute("doctorList", doctorPagination.getDataToDisplay());
model.addAttribute("doctorPagination", doctorPagination);
model.addAttribute("pharmacistList", pharmacistPagination.getDataToDisplay());
model.addAttribute("pharmacistPagination", pharmacistPagination);


        return "adminDashboard";
    }

    // Commented for now since pagination is disabled
    
    private PaginationInfo getPaginationInfo(List<?> dataList, int pageNo) {
        int pageSize = 5;
        int totalItems = dataList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        pageNo = Math.max(1, Math.min(pageNo, totalPages));
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, totalItems);
        List<?> pageData = dataList.subList(start, end);

        return new PaginationInfo(pageData, pageSize, pageNo, totalPages,
                Math.max(1, pageNo - 1), Math.min(totalPages, pageNo + 1));
    }

    @PostMapping("/edituser")
    public String editUser(@RequestParam("userId") String userId,
                           @RequestParam("userFullName") String name,
                           @RequestParam("contact") String contact,
                           @RequestParam("userEmail") String email,
                           @RequestParam("role") String role,
                           @RequestParam(value = "address", required = false) String address,
                           @RequestParam(value = "emergencyContact", required = false) String emergencyContact,
                           @RequestParam(value = "sensorId", required = false) String sensorId,
                           @RequestParam(value = "doctorHospital", required = false) String doctorHospital,
                           @RequestParam(value = "doctorPosition", required = false) String doctorPosition,
                           @RequestParam(value = "pharmacistHospital", required = false) String pharmacistHospital,
                           @RequestParam(value = "pharmacistPosition", required = false) String pharmacistPosition,
                           Model model) throws ExecutionException, InterruptedException {
    
        String message = "";
    
        switch (role) {
            case "PATIENT":
                Patient patient = new Patient(userId, name, "", contact, role, email,
                        address, emergencyContact, sensorId, "", "Under Surveillance");
                message = patientService.updatePatient(patient);
                break;
    
            case "DOCTOR":
                Doctor doctor = new Doctor(userId, name, "", contact, role, email, doctorHospital, doctorPosition);
                message = doctorService.updateDoctor(doctor);
                break;
    
            case "PHARMACIST":
                Pharmacist pharmacist = new Pharmacist(userId, name, "", contact, role, email, pharmacistHospital, pharmacistPosition);
                message = pharmacistService.updatePharmacist(pharmacist);
                break;
    
            default:
                User user = new User(userId, name, "", contact, role, email);
                message = userService.updateUser(user);
                break;
        }
    
        model.addAttribute("message", message);
        return "redirect:/admin";
    }
    

@PostMapping("/deleteuser")
public String deleteUser(@RequestParam("userIdToBeDelete") String userId,
                         @RequestParam("userRoleToBeDelete") String role,
                         RedirectAttributes redirectAttributes) {
    String message;
    try {
        switch (role) {
            case "PATIENT":
                message = patientService.deletePatient(userId);
                break;
            case "DOCTOR":
                message = doctorService.deleteDoctor(userId);
                break;
            case "PHARMACIST":
                message = pharmacistService.deletePharmacist(userId);
                break;
            default:
                message = userService.deleteUser(userId);
                break;
        }
        redirectAttributes.addFlashAttribute("message", message);
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("message", "Error deleting user: " + e.getMessage());
    }

    return "redirect:/admin";
}


    
    
} 
