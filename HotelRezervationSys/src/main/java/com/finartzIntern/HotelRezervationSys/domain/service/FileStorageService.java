package com.finartzIntern.HotelRezervationSys.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
}