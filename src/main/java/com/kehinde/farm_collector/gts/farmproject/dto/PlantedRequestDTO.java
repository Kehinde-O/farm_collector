package com.kehinde.farm_collector.gts.farmproject.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class PlantedRequestDTO {
    @NotBlank(message = "Farm name is required")
    private String farmName;

    @NotBlank(message = "Field name is required")
    private String fieldName;

    @NotNull(message = "Season ID is required")
    private Long seasonId;

    @NotBlank(message = "Crop type is required")
    private String cropType;

    @NotNull(message = "Planting area is required")
    @Positive(message = "Planting area must be greater than 0")
    private Double plantingArea;

    @NotNull(message = "Expected amount is required")
    @Positive(message = "Expected amount must be greater than 0")
    private Double expectedAmount;
}
