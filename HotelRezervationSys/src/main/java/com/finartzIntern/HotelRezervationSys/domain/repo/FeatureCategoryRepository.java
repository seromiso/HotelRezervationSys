package com.finartzIntern.HotelRezervationSys.domain.repo;

import com.finartzIntern.HotelRezervationSys.domain.model.entities.FeatureCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureCategoryRepository extends JpaRepository<FeatureCategory,Long> {
}
