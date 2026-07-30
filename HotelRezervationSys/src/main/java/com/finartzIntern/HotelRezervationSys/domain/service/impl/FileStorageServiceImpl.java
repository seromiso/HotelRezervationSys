package com.finartzIntern.HotelRezervationSys.domain.service.impl;

import com.finartzIntern.HotelRezervationSys.domain.exceptions.BadRequestException;
import com.finartzIntern.HotelRezervationSys.domain.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    // Yüklenen dosyaların kaydedileceği yerel dizin (Klasör otomatik oluşturulur)
    private final Path fileStorageLocation = Paths.get("uploads/licenses").toAbsolutePath().normalize();

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BadRequestException("Dosyaların yükleneceği dizin oluşturulamadı!", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Yüklenecek dosya boş olamaz!");
        }

        // Orijinal dosya adını temizle
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Dosya adında geçersiz karakter kontrolü
            if (originalFileName.contains("..")) {
                throw new BadRequestException("Geçersiz dosya yolu formatı: " + originalFileName);
            }

            // Aynı isimde dosyalar birbirini ezmesin diye benzersiz bir isim (UUID) üretiyoruz
            String fileExtension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFileName.substring(i);
            }

            String newFileName = UUID.randomUUID() + fileExtension;

            // Dosyayı hedef dizine kopyala/kaydet
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Veritabanına kaydedilecek dosya yolunu/ismini döndür
            return "/uploads/licenses/" + newFileName;

        } catch (IOException ex) {
            throw new RuntimeException("Dosya sunucuya kaydedilirken bir hata oluştu: " + originalFileName, ex);
        }
    }
}