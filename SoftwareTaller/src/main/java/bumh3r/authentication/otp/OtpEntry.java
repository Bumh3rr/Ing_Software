package bumh3r.authentication.otp;

import java.time.LocalDateTime;
import lombok.Getter;

public class OtpEntry {

    private final String code;
    @Getter
    private final LocalDateTime expirationTime;

    public OtpEntry(String code, int validityDurationInMinutes) {
        this.code = code;
        this.expirationTime = LocalDateTime.now().plusMinutes(validityDurationInMinutes);
    }

    public String getOtp() {
        return this.code;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }
}
