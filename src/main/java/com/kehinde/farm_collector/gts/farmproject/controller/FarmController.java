package com.kehinde.farm_collector.gts.farmproject.controller;

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
}