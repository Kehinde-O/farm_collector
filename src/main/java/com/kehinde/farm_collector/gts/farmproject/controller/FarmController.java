package com.kehinde.farm_collector.gts.farmproject.controller;

import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.SeasonReportDTO;
import com.kehinde.farm_collector.gts.farmproject.entity.Farm;
import com.kehinde.farm_collector.gts.farmproject.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Create a new farm",
            description = "Adds a new farm to the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Farm created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Farm> createFarm(
            @Parameter(description = "Details of the farm to be created", required = true)
            @Valid @RequestBody Farm farm) {
        Farm createdFarm = farmService.addFarm(farm);
        return new ResponseEntity<>(createdFarm, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Retrieve all farms",
            description = "Fetches a list of all farms stored in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of farms"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Farm>> getAllFarms() {
        List<Farm> farms = farmService.getAllFarms();
        return ResponseEntity.ok(farms);
    }

    @Operation(
            summary = "Retrieve a specific farm by ID",
            description = "Fetches details of a specific farm using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the farm"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Farm> getFarmById(
            @Parameter(description = "ID of the farm to be retrieved", required = true)
            @PathVariable Long id) {
        Farm farm = farmService.getFarmById(id);
        return ResponseEntity.ok(farm);
    }

    @Operation(
            summary = "Submit planted data",
            description = "Submits data for crops that have been planted, including expected yield and planting area."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planted data submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/planted")
    public ResponseEntity<String> submitPlantedData(
            @Parameter(description = "Details of the planted data to be submitted", required = true)
            @Valid @RequestBody PlantedRequestDTO request) {
        farmService.addPlantedData(request);
        return new ResponseEntity<>("Planted data submitted successfully", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Submit harvested data",
            description = "Submits data for crops that have been harvested, including actual yield."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Harvested data submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/harvested")
    public ResponseEntity<String> submitHarvestedData(
            @Parameter(description = "Details of the harvested data to be submitted", required = true)
            @Valid @RequestBody HarvestedRequestDTO request) {
        farmService.addHarvestedData(request);
        return new ResponseEntity<>("Harvested data submitted successfully", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get report for a specific season",
            description = "Generates a report that compares expected vs. actual product amounts for each farm and crop type within a given season."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the season report"),
            @ApiResponse(responseCode = "404", description = "Season not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/report/{seasonId}")
    public ResponseEntity<SeasonReportDTO> getSeasonReport(
            @Parameter(description = "ID of the season for which to generate the report", required = true)
            @PathVariable Long seasonId) {
        SeasonReportDTO report = farmService.generateSeasonReport(seasonId);
        return ResponseEntity.ok(report);
    }
}