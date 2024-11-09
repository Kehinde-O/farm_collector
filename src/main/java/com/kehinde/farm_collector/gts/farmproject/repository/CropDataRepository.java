package com.kehinde.farm_collector.gts.farmproject.repository;

import com.kehinde.farm_collector.gts.farmproject.entity.CropData;
import com.kehinde.farm_collector.gts.farmproject.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropDataRepository extends JpaRepository<CropData, Long> {
    List<CropData> findBySeason(Season season);
}
