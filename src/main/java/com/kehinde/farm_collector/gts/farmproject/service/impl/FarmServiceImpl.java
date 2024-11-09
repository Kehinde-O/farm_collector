package com.kehinde.farm_collector.gts.farmproject.service.impl;

import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.entity.*;
import com.kehinde.farm_collector.gts.farmproject.exception.ResourceNotFoundException;
import com.kehinde.farm_collector.gts.farmproject.repository.CropDataRepository;
import com.kehinde.farm_collector.gts.farmproject.repository.FarmRepository;
import com.kehinde.farm_collector.gts.farmproject.repository.FieldRepository;
import com.kehinde.farm_collector.gts.farmproject.repository.SeasonRepository;
import com.kehinde.farm_collector.gts.farmproject.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class FarmServiceImpl implements FarmService {

    private final FarmRepository farmRepository;
    private final CropDataRepository cropDataRepository;
    private final FieldRepository fieldRepository;
    private final SeasonRepository seasonRepository;

    @Override
    public Farm addFarm(Farm farm) {
        // Additional business logic can be added here
        return farmRepository.save(farm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Farm getFarmById(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
    }

    @Override
    public void addPlantedData(PlantedRequestDTO request) {
        Season season = seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new RuntimeException("Season not found with id: " + request.getSeasonId()));

        Field field = fieldRepository.findByNameAndFarm(request.getFieldName(), request.getFarmName())
                .orElseThrow(() -> new RuntimeException("Field not found for name: " + request.getFieldName()));

        CropData cropData = new CropData();
        cropData.setField(field);
        cropData.setSeason(season);
        cropData.setCropType(request.getCropType());
        cropData.setPlantingArea(request.getPlantingArea());
        cropData.setExpectedAmount(request.getExpectedAmount());
        cropData.setDataType(DataType.PLANTED);

        cropDataRepository.save(cropData);
    }

    @Override
    public void addHarvestedData(HarvestedRequestDTO request) {
        Season season = seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new RuntimeException("Season not found with id: " + request.getSeasonId()));

        Field field = fieldRepository.findByNameAndFarm(request.getFieldName(), request.getFarmName())
                .orElseThrow(() -> new RuntimeException("Field not found for name: " + request.getFieldName()));

        CropData cropData = new CropData();
        cropData.setField(field);
        cropData.setSeason(season);
        cropData.setCropType(request.getCropType());
        cropData.setActualAmount(request.getActualAmount());
        cropData.setDataType(DataType.HARVESTED);

        cropDataRepository.save(cropData);
    }

}
