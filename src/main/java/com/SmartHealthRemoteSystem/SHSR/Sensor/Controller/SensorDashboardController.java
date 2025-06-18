package com.SmartHealthRemoteSystem.SHSR.Sensor.Controller;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.HistorySensorData;
import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SensorDashboardController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private PatientService patientService;

  @GetMapping("/patient/sensorDashboard")
public String viewSensorDashboard(
        @RequestParam(value = "patientId", required = false) String patientIdParam,
        @RequestParam(value = "role", defaultValue = "patient") String role,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "filterDate", required = false) String filterDate,
        Model model) throws Exception {

    // Step 1: Determine current user or passed-in patient
    String patientId;
    if (patientIdParam != null && !patientIdParam.isEmpty()) {
        patientId = patientIdParam; // doctor/admin viewing another patient
    } else {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        patientId = userDetails.getUsername(); // current logged-in patient
    }

    Patient patient = patientService.getPatientById(patientId);
    model.addAttribute("patient", patient);
    model.addAttribute("patientid", patientId);
    model.addAttribute("role", role);

    // Step 2: Handle missing sensor
    if (patient.getSensorDataId() == null || patient.getSensorDataId().isEmpty()) {
        model.addAttribute("sensorData", null);
        model.addAttribute("sensorDataPage", null);
        model.addAttribute("latestFiveReadings", null);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "sensorDashboard";
    }

    // Step 3: Load sensor data
    SensorData sensorData = sensorDataService.getSensorById(patient.getSensorDataId());
    model.addAttribute("sensorData", sensorData);

    if (sensorData == null || sensorData.getHistory() == null) {
        model.addAttribute("sensorDataPage", null);
        model.addAttribute("latestFiveReadings", null);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "sensorDashboard";
    }

    // ✅ Step 4: Get and sort history
    List<HistorySensorData> history = sensorData.getHistory();
    history.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp())); // newest first
    List<HistorySensorData> top50 = history.stream().limit(50).toList();

    // Step 5: Apply filter if present
    if (filterDate != null && !filterDate.isEmpty()) {
        top50 = top50.stream()
            .filter(h -> h.getTimestamp().toString().startsWith(filterDate))
            .toList();
        model.addAttribute("filterDate", filterDate);
    }

    // Step 6: Apply pagination
    int pageSize = 5;
    int start = Math.max(0, (page - 1) * pageSize);
    int end = Math.min(start + pageSize, top50.size());
    List<HistorySensorData> paginated = top50.subList(start, end);

    model.addAttribute("sensorDataPage", paginated);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", (int) Math.ceil((double) top50.size() / pageSize));

    // Step 7: Latest 5 readings
    model.addAttribute("latestFiveReadings", top50.stream().limit(5).toList());

    return "sensorDashboard";
}


}