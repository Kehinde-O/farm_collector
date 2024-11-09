package com.kehinde.farm_collector.gts.farmproject.service;

import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.entity.Farm;

import java.util.List;

public interface FarmService {
    Farm addFarm(Farm farm);

    List<Farm> getAllFarms();

    Farm getFarmById(Long id);

    void addPlantedData(PlantedRequestDTO request);

    void addHarvestedData(HarvestedRequestDTO request);
}
