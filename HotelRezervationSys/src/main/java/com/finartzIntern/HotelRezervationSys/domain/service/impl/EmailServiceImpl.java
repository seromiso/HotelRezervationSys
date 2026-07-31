package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;
import com.finartzIntern.HotelRezervationSys.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.verification.base-url:http://localhost:8080/api/v1/auth/verify?email=}")
    private String verificationBaseUrl;

    @Override
    public void sendVerificationEmail(User user) {
        String verificationLink = verificationBaseUrl + user.getEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("HotelReservationSys - E-posta Doğrulama");


        message.setText("Merhaba " + user.getUsername() + ",\n\n"
                + "Sistemimizi kullandığınız için teşekkürler! Rezervasyon işlemlerine ve profil güncellemelerine "
                + "başlayabilmek için lütfen aşağıdaki bağlantıya tıklayarak e-posta adresinizi doğrulayın:\n\n"
                + verificationLink + "\n\n"
                + "Keyifli tatiller dileriz!");

        mailSender.send(message);
    }


}


