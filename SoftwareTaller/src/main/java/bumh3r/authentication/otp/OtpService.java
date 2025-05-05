package bumh3r.authentication.otp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.mail.MessagingException;

public class OtpService {

    private final EmailSender emailSender;
    private final Map<String, OtpEntry> otpStorage;
    private final int durationsMinutesOtp = 5;

    public OtpService(EmailSender emailSender) {
        this.emailSender = emailSender;
        this.otpStorage = new HashMap<>();
    }

    public void generateAndSendOtp(String userEmail) throws MessagingException {
        if (otpStorage.containsKey(userEmail)) {
            otpStorage.remove(userEmail);
        }
        OtpEntry otpEntry = new OtpEntry(String.format("%06d", new Random().nextInt(1000000)),
                durationsMinutesOtp);
        emailSender.sendOtpEmail(userEmail, otpEntry.getOtp());
        otpStorage.put(userEmail, otpEntry);
    }

    public boolean validateOtp(String userEmail, String inputOtp) throws Exception {
        OtpEntry otpEntry = otpStorage.get(userEmail);
        if (otpEntry == null) {
            throw new Exception("No se ha generado un OTP para el correo electrónico proporcionado.");
        }
        if (otpEntry.isExpired()) {
            otpStorage.remove(userEmail);
            throw new Exception("El OTP ha expirado.");
        }
        return otpEntry.getOtp().equals(inputOtp);
    }

}