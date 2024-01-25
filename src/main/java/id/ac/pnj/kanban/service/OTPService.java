package id.ac.pnj.kanban.service;

import id.ac.pnj.kanban.mail.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPService {
    private OTPGenerator otpGenerator;
    private EmailService emailService;

    public OTPService(OTPGenerator otpGenerator, EmailService emailService) {
        this.otpGenerator = otpGenerator;
        this.emailService = emailService;
    }

    public void generateOtp(String username) throws MessagingException {
        int otp = otpGenerator.generateOtp(username);
        emailService.sendMimeMail(username, "OTP Code from Kanban",
                """
                        <header style="text-align:center; font-weight:bold; font-size: 24px; margin-bottom:16px;">
                        <p>Kanban</p>
                        </header>
                        <p style="margin: 16px 25px">This OTP code is used for registration. OTP is valid for 5 minutes.
                        Do not share this code with others.</p>
                        <p style="text-align:center; font-weight:bold; font-size: 18px; margin: 48px 25px">%d</p>
                        <p style="margin: 16px 25px">This email is an automated message. Please, do not reply to this email</p>
                        """.formatted(otp));
    }

    public boolean invalidateOtp(String username, Integer otpParam) {
        Integer cacheOtp = otpGenerator.getOTPByKey(username);
        if (cacheOtp != null && cacheOtp.equals(otpParam)) {
            otpGenerator.clearOtp(username);
            return true;
        }
        else
            return false;
    }


}
