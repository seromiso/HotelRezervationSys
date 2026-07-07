package com.finartzIntern.HotelRezervationSys.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Hotelname;




}
