package bumh3r.authentication.otp;

import bumh3r.model.Usuario;
import javax.mail.MessagingException;

public class ControllerEmailSender {
    private static ControllerEmailSender instance;
    private final String EMAIL_USERNAME = "flipfloptoolsoporte@gmail.com";
    private final String EMAIL_PASSWORD = "pdgc xjvb oeze ceqc";
    private OtpService otpService;
    private EmailSender emailSender;

    public static void closeSession() {
        instance = null;
    }
    public static ControllerEmailSender getInstance() {
        if (instance == null) {
            instance = new ControllerEmailSender();
        }
        return instance;
    }

    public ControllerEmailSender() {
        emailSender = new EmailSender(EMAIL_USERNAME, EMAIL_PASSWORD);
        otpService = new OtpService(emailSender);
    }

    public void sendOtp(String userEmail) throws MessagingException {
         otpService.generateAndSendOtp(userEmail);
    }

    public boolean validateOtp(String userEmail, String inputOtp) throws Exception {
        return otpService.validateOtp(userEmail, inputOtp);
    }

    public void sendRegisterSuccess(String userEmail,Usuario user)throws MessagingException {
       emailSender.sendRegisterSuccessEmail(userEmail,user);
    }

}
