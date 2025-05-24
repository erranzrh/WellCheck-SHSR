// package com.SmartHealthRemoteSystem.SHSR.registerPatient;

// import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
// import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
// import com.SmartHealthRemoteSystem.SHSR.Service.UserService;
// import com.SmartHealthRemoteSystem.SHSR.User.User;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.concurrent.ExecutionException;

// @Controller
// @RequestMapping("/registerPatient")
// public class registerPatientController {
//     private final PatientService patientService;
//     private final UserService userService;

//     @Autowired
//     public registerPatientController(PatientService patientService, UserService userService) {
//         this.patientService = patientService;
//         this.userService = userService;
//     }
//     @GetMapping()
//     public String registerForm(){
//         return "registerPatientForm";
//     }

//     @PostMapping("/submit")
//     public String registerPatient(
//             @RequestParam("fullname")String fullName,@RequestParam("id")String id,
//             @RequestParam("password")String password, @RequestParam("phone")String phoneNum,
//             @RequestParam("address")String address, @RequestParam("emergency")String emergency,
//             @RequestParam("role")String role, Model model) throws ExecutionException, InterruptedException {

//         String status= "Under Surveillance";
//         Patient newPatient=new Patient(id,fullName,password,phoneNum,role,"",address,emergency,"",status);
//         patientService.createPatient(newPatient);

//         return "login";
//     }
// }



//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.registerPatient;

import java.util.concurrent.ExecutionException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.UserService;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.User;

@Controller
@RequestMapping("/registerPatient")
public class RegisterPatientController {

    private final PatientService patientService;
    private final UserService userService;

    @Autowired
    public RegisterPatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "registerPatientForm";
    }

    @PostMapping
    public String registerPatient(
            @Valid @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("contact") String contact,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("emergencyContact") String emergencyContact,
            Model model) throws ExecutionException, InterruptedException {

        if (userService.getUser(userId) != null) {
            model.addAttribute("error", "User ID already exists.");
            model.addAttribute("patient", new Patient()); // refill form
            return "registerPatientForm";
        }

        // 1. Create Patient object with raw password
        Patient newPatient = new Patient(
                userId,
                name,
                password, // ⚠️ raw password
                contact,
                "PATIENT",
                email,
                address,
                emergencyContact,
                "", // sensorDataId
                "", // assigned_doctor
                "Under Surveillance"
        );

        // 2. Save to Patient collection
        String result = patientService.createPatient(newPatient);
        if (result.contains("already exists")) {
            model.addAttribute("error", result);
            model.addAttribute("patient", new Patient()); // refill form
            return "registerPatientForm";
        }

        // 3. Save to User collection (encoding will be handled inside userService)
        User user = new User(
                userId,
                name,
                password, // ⚠️ raw password
                contact,
                "PATIENT",
                email
        );
        userService.createUser(user);

        return "redirect:/login";
    }
}
