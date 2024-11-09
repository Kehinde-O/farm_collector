package com.kehinde.farm_collector.gts.farmproject.repository;

import com.kehinde.farm_collector.gts.farmproject.entity.CropData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropDataRepository extends JpaRepository<CropData, Long> {
}
