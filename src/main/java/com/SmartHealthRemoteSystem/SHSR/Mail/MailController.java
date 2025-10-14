

package com.SmartHealthRemoteSystem.SHSR.Mail;

import com.SmartHealthRemoteSystem.SHSR.Service.MailService;
import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {
        if (emailExists(mail)) {
            return "Error: Email " + mail + " already exists. Mail not sent.";
        }

        mailService.sendNewUserMail(mail, mailStructure);
        return "Mail sent successfully to " + mail;
    }

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}

