package com.kehinde.farm_collector.gts.farmproject.controller;

import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.entity.Farm;
import com.kehinde.farm_collector.gts.farmproject.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
public class FarmController {

    private final FarmService farmService;

    @PostMapping
    public ResponseEntity<Farm> createFarm(@Valid @RequestBody Farm farm) {
        Farm createdFarm = farmService.addFarm(farm);
        return new ResponseEntity<>(createdFarm, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Farm>> getAllFarms() {
        List<Farm> farms = farmService.getAllFarms();
        return ResponseEntity.ok(farms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farm> getFarmById(@PathVariable Long id) {
        Farm farm = farmService.getFarmById(id);
        return ResponseEntity.ok(farm);
    }

    @PostMapping("/planted")
    public ResponseEntity<String> submitPlantedData(@Valid @RequestBody PlantedRequestDTO request) {
        farmService.addPlantedData(request);
        return new ResponseEntity<>("Planted data submitted successfully", HttpStatus.CREATED);
    }

    @PostMapping("/harvested")
    public ResponseEntity<String> submitHarvestedData(@Valid @RequestBody HarvestedRequestDTO request) {
        farmService.addHarvestedData(request);
        return new ResponseEntity<>("Harvested data submitted successfully", HttpStatus.CREATED);
    }

//    @GetMapping("/reports/season/{seasonId}")
//    public ResponseEntity<ReportResponseDTO> getSeasonReport(@PathVariable Long seasonId) {
//        ReportResponseDTO report = farmService.generateReport(seasonId);
//        return ResponseEntity.ok(report);
//    }
}