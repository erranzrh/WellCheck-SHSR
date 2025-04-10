// package com.SmartHealthRemoteSystem.SHSR.Service;

// import com.SmartHealthRemoteSystem.SHSR.Mail.MailStructure;
// import com.SmartHealthRemoteSystem.SHSR.User.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.MailException;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @Service
// @Transactional
// public class MailService {
    
//     private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    
//     private final JavaMailSender mailSender;
//     private final UserRepository userRepository;

//     @Value("${spring.mail.username}")
//     private String fromMail;

//     @Autowired
//     public MailService(JavaMailSender mailSender, UserRepository userRepository) {
//         this.mailSender = mailSender;
//         this.userRepository = userRepository;
//     }

//     public void sendNewUserMail(String mail, MailStructure mailStructure) {
//         try {
//             String subject = "Welcome to CDPRSystem - New User Registration";
//             String message = "Refer the following details to login.\nTemporary password: " + mailStructure.getPassword();

//             sendMail(mail, subject, message);
//             logger.info("New user mail sent successfully to {}", mail);
//         } catch (MailException e) {
//             logger.error("Failed to send new user mail to {}: {}", mail, e.getMessage());
//         }
//     }

//     public void sendMail(String mail, String subject, String message) {
//         try {
//             SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//             simpleMailMessage.setFrom(fromMail);
//             simpleMailMessage.setTo(mail);
//             simpleMailMessage.setSubject(subject);
//             simpleMailMessage.setText(message);

//             mailSender.send(simpleMailMessage);
//             logger.info("Email sent successfully to {}", mail);
//         } catch (MailException e) {
//             logger.error("Failed to send email to {}: {}", mail, e.getMessage());
//         }
//     }

//     public void sendAssignedMail(String mail, String subject, String message) {
//         sendMail(mail, subject, message);
//     }

//     public void sendUnassignedMail(String mail, String subject, String message) {
//         sendMail(mail, subject, message);
//     }

//     public void sendReleasedMail(String mail, String subject, String message) {
//         sendMail(mail, subject, message);
//     }
// }
