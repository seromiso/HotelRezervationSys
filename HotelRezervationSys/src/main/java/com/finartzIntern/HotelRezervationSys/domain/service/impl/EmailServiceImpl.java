package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String name, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("HotelReservationSys - E-posta Doğrulama");
        message.setText("Merhaba " + name + ",\n\n"
                + "Sisteme başarıyla kayıt oldunuz! Rezervasyon işlemlerine başlayabilmek için "
                + "lütfen aşağıdaki linke tıklayarak e-posta adresinizi doğrulayın:\n\n"
                + verificationLink + "\n\n"
                + "Keyifli tatiller dileriz!");

        mailSender.send(message);
    }

}
