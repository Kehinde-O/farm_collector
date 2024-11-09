package com.kehinde.farm_collector.gts.farmproject.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class PlantedRequestDTO {
    @NotNull(message = "Farm ID is required")
    private Long farmId;

    @NotNull(message = "Field ID is required")
    private Long fieldId;

    @NotNull(message = "Season ID is required")
    private Long seasonId;

    private String cropType;

    @Positive(message = "Planting area must be greater than 0")
    private Double plantingArea;

    @Positive(message = "Expected amount must be greater than 0")
    private Double expectedAmount;
}
