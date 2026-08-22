package com.finartzIntern.HotelRezervationSys.domain.service;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.User;

public interface EmailService {
    void sendVerificationEmail(User user);

}
