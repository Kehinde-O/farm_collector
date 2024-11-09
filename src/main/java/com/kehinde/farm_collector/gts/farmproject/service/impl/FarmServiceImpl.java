package com.kehinde.farm_collector.gts.farmproject.service.impl;

import com.kehinde.farm_collector.gts.farmproject.dto.FarmReportDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.SeasonReportDTO;
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
import java.util.stream.Collectors;


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

        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + request.getFieldId()));

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

        Field field = fieldRepository.findById(request.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found with id: " + request.getFieldId()));

        CropData cropData = new CropData();
        cropData.setField(field);
        cropData.setSeason(season);
        cropData.setCropType(request.getCropType());
        cropData.setActualAmount(request.getActualAmount());
        cropData.setDataType(DataType.HARVESTED);

        cropDataRepository.save(cropData);
    }

    @Override
    public SeasonReportDTO generateSeasonReport(Long seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new RuntimeException("Season not found with id: " + seasonId));

        List<CropData> cropDataList = cropDataRepository.findBySeason(season);

        // Group by farm and crop type, then calculate expected and actual amounts
        List<FarmReportDTO> farmReports = cropDataList.stream()
                .collect(Collectors.groupingBy(data -> data.getField().getFarm().getId()))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .collect(Collectors.groupingBy(CropData::getCropType))
                        .entrySet().stream()
                        .map(cropEntry -> {
                            Long farmId = entry.getKey();
                            String farmName = cropEntry.getValue().get(0).getField().getFarm().getName();
                            Long fieldId = cropEntry.getValue().get(0).getField().getId();
                            String cropType = cropEntry.getKey();

                            double expectedAmount = cropEntry.getValue().stream()
                                    .filter(data -> data.getDataType() == DataType.PLANTED)
                                    .mapToDouble(CropData::getExpectedAmount)
                                    .sum();

                            double actualAmount = cropEntry.getValue().stream()
                                    .filter(data -> data.getDataType() == DataType.HARVESTED)
                                    .mapToDouble(CropData::getActualAmount)
                                    .sum();

                            return new FarmReportDTO(farmId, farmName, fieldId, cropType, expectedAmount, actualAmount);
                        }))
                .collect(Collectors.toList());

        return new SeasonReportDTO(season.getId(), season.getName(), farmReports);
    }
}
