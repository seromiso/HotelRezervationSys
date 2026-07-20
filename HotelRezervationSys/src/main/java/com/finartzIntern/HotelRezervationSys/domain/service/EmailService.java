package com.finartzIntern.HotelRezervationSys.domain.service;

public interface EmailService {
    void sendVerificationEmail(String toEmail,String name,String verificationLink);

}
