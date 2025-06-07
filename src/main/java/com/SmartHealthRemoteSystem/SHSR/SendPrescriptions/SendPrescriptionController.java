//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.SendPrescriptions;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.MedicineService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PrescriptionService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/prescription")
public class SendPrescriptionController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final MedicineService medicineService;

    @Autowired
    public SendPrescriptionController(DoctorService doctorService, PatientService patientService,
                                      PrescriptionService prescriptionService, MedicineService medicineService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.medicineService = medicineService;
    }

    // Show prescription form
    @GetMapping("/form")
    public String getPrescriptionForm(@RequestParam String patientId, Model model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(userDetails.getUsername());

        Patient patient = patientService.getPatientById(patientId);
        List<Medicine> medicineList = medicineService.getAllMedicines();

        model.addAttribute("doctor", doctor);
        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("medicineList", medicineList);

        return "sendPrescriptionForm";
    }

    // Show medicine selection
    @GetMapping("/add-prescription")
    public String addMedication(@RequestParam String patientId, Model model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(userDetails.getUsername());

        List<Medicine> medicineList = medicineService.getAllMedicines();
        Patient patient = patientService.getPatientById(patientId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("medicineList", medicineList);

        return "patientMedicine";
    }

    

    @PostMapping("/form/submit")
public String submitPrescriptionForm(@RequestParam String patientId,
                                     @RequestParam String doctorId,
                                     @RequestParam String prescription,
                                     @RequestParam String diagnosisAilment,
                                     HttpServletRequest request,
                                     Model model) {

    Map<String, String[]> paramMap = request.getParameterMap();
    Map<String, Integer> prescribedMedicines = new HashMap<>();

    int index = 0;
    while (paramMap.containsKey("medicines[" + index + "].medId")) {
        String medId = request.getParameter("medicines[" + index + "].medId");
        String selected = request.getParameter("medicines[" + index + "].selected");
        String quantityStr = request.getParameter("medicines[" + index + "].quantity");

        if ("true".equals(selected) && quantityStr != null && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                prescribedMedicines.put(medId, quantity);
            } catch (NumberFormatException e) {
                // handle invalid input
            }
        }

        index++;
    }

    // Save prescription
    prescriptionService.prescribeMedicines(patientId, prescribedMedicines, prescription, diagnosisAilment);

    return "redirect:/prescription/history?patientId=" + patientId;
}


    // Submit selected medicines with quantity
    @PostMapping("/prescribemedicine/submit")
    public String submitMedicineForm(@RequestParam String patientId,
                                     @RequestParam Map<String, String> allParams,
                                     RedirectAttributes redirectAttributes,
                                     Model model) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(userDetails.getUsername());

        // Extract medicine ID and quantity
        Map<String, Integer> selectedMedicines = allParams.entrySet().stream()
                .filter(e -> e.getKey().startsWith("quantity_") && !e.getValue().isEmpty())
                .collect(Collectors.toMap(
                        e -> e.getKey().substring("quantity_".length()),
                        e -> Integer.parseInt(e.getValue())
                ));

        prescriptionService.prescribeMedicines(patientId, selectedMedicines,
                "Prescription Description", "Diagnosis Ailment Description");

        redirectAttributes.addAttribute("patientId", patientId);
        return "redirect:/prescription/form";
    }

    // Prescription history for a patient
    @GetMapping("/history")
    public String viewPrescriptionHistory(@RequestParam String patientId,
                                          @RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "") String searchQuery,
                                          Model model) throws Exception {

        List<Prescription> allPrescriptions = prescriptionService.getAllPrescriptions(patientId);

        if (!searchQuery.isEmpty()) {
            allPrescriptions = allPrescriptions.stream()
                    .filter(p -> p.getPrescriptionDescription().toLowerCase().contains(searchQuery.toLowerCase())
                            || p.getDiagnosisAilmentDescription().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int total = allPrescriptions.size();
        int start = Math.min(pageNo * pageSize, total);
        int end = Math.min((pageNo + 1) * pageSize, total);

        List<Prescription> pagedList = allPrescriptions.subList(start, end);

        model.addAttribute("prescriptionHistory", pagedList);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", (total + pageSize - 1) / pageSize);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("startIndex", pageNo * pageSize);

        return "prescriptionHistory";
    }
}
