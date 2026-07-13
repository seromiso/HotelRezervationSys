package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.ImageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageCategoryRepository extends JpaRepository<ImageCategory,Long> {

    List<ImageCategory> findAllImageCategory(Long id);

}
