package com.SmartHealthRemoteSystem.SHSR.User.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SmartHealthRemoteSystem.SHSR.Pagination.PaginationInfo;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.MailService;
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
    private final MailService mailService;

    @Autowired
    public AdminController(UserService userService,
                           PatientService patientService,
                           DoctorService doctorService,
                           PharmacistService pharmacistService,
                           MailService mailService) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.pharmacistService = pharmacistService;
        this.mailService = mailService;
    }

   @GetMapping
public String getAdminDashboard(Model model,
        @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
        @RequestParam(value = "tab", required = false, defaultValue = "patient") String tab) throws ExecutionException, InterruptedException {

    List<User> adminList = userService.getAdminList();
    List<Patient> allPatients = patientService.getAllPatients();
    List<Doctor> doctorList = doctorService.getAllDoctors();
    List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();

    // Split into assigned and unassigned
    List<Patient> assignedPatientsList = allPatients.stream()
            .filter(p -> p.getAssigned_doctor() != null && !p.getAssigned_doctor().isEmpty())
            .collect(Collectors.toList());

    List<Patient> unassignedPatientsList = allPatients.stream()
            .filter(p -> p.getAssigned_doctor() == null || p.getAssigned_doctor().isEmpty())
            .collect(Collectors.toList());

    // Pagination for both assigned and unassigned patients
    PaginationInfo assignedPagination = getPaginationInfo(assignedPatientsList, pageNo);
    PaginationInfo unassignedPagination = getPaginationInfo(unassignedPatientsList, pageNo);

    // Assign all paginated data
    model.addAttribute("adminList", getPaginationInfo(adminList, pageNo).getDataToDisplay());
    model.addAttribute("adminPagination", getPaginationInfo(adminList, pageNo));
    model.addAttribute("patientList", getPaginationInfo(allPatients, pageNo).getDataToDisplay());
    model.addAttribute("patientPagination", getPaginationInfo(allPatients, pageNo));
    model.addAttribute("doctorList", getPaginationInfo(doctorList, pageNo).getDataToDisplay());
    model.addAttribute("doctorPagination", getPaginationInfo(doctorList, pageNo));
    model.addAttribute("pharmacistList", getPaginationInfo(pharmacistList, pageNo).getDataToDisplay());
    model.addAttribute("pharmacistPagination", getPaginationInfo(pharmacistList, pageNo));

    model.addAttribute("assignedPatients", assignedPagination.getDataToDisplay());
    model.addAttribute("assignedPagination", assignedPagination);
    model.addAttribute("unassignedPatients", unassignedPagination.getDataToDisplay());
    model.addAttribute("unassignedPagination", unassignedPagination);

    model.addAttribute("tab", tab); // optional, for future frontend use
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
                                      Model model) throws ExecutionException, InterruptedException {

        String message = "";

        if (action.equalsIgnoreCase("add")) {
            switch (role) {
                case "PATIENT":
                    Patient patient = new Patient(userId, name, password, contact, role, email,
                            address, emergencyContact, sensorId, "", "Under Surveillance");
                    message = patientService.createPatient(patient);
                    mailService.sendMail(email, "Welcome to WellCheck",
                            "Dear " + name + ",\n\nYou have been registered as a patient.\nUser ID: " + userId +
                            "\nTemporary Password: " + password + "\n\nPlease log in and change your password.");
                    break;

                case "DOCTOR":
                    Doctor doctor = new Doctor(userId, name, password, contact, role, email, doctorHospital, doctorPosition);
                    message = doctorService.saveDoctor(doctor);
                    mailService.sendMail(email, "Welcome to WellCheck",
                            "Dear Dr. " + name + ",\n\nYou have been registered as a doctor.\nUser ID: " + userId +
                            "\nTemporary Password: " + password + "\n\nPlease log in and change your password.");
                    break;

                case "PHARMACIST":
                    Pharmacist pharmacist = new Pharmacist(userId, name, password, contact, role, email, pharmacistHospital, pharmacistPosition);
                    message = pharmacistService.createPharmacist(pharmacist);
                    mailService.sendMail(email, "Welcome to WellCheck",
                            "Dear " + name + ",\n\nYou have been registered as a pharmacist.\nUser ID: " + userId +
                            "\nTemporary Password: " + password + "\n\nPlease log in and change your password.");
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

        List<User> adminList = userService.getAdminList();
        List<Patient> patientList = patientService.getAllPatients();
        List<Doctor> doctorList = doctorService.getAllDoctors();
        List<Pharmacist> pharmacistList = pharmacistService.getListPharmacist();

        model.addAttribute("adminList", getPaginationInfo(adminList, 1).getDataToDisplay());
        model.addAttribute("adminPagination", getPaginationInfo(adminList, 1));
        model.addAttribute("patientList", getPaginationInfo(patientList, 1).getDataToDisplay());
        model.addAttribute("patientPagination", getPaginationInfo(patientList, 1));
        model.addAttribute("doctorList", getPaginationInfo(doctorList, 1).getDataToDisplay());
        model.addAttribute("doctorPagination", getPaginationInfo(doctorList, 1));
        model.addAttribute("pharmacistList", getPaginationInfo(pharmacistList, 1).getDataToDisplay());
        model.addAttribute("pharmacistPagination", getPaginationInfo(pharmacistList, 1));

        return "adminDashboard";
    }

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


    @PostMapping("/resetPassword")
@ResponseBody
public String resetPatientPassword(@RequestParam String patientId,
                                   @RequestParam String newPassword) {
    try {
        return patientService.resetPatientPassword(patientId, newPassword);
    } catch (Exception e) {
        return "❌ Error resetting password: " + e.getMessage();
    }
}




}
