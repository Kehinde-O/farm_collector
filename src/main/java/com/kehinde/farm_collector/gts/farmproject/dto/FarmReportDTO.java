package com.kehinde.farm_collector.gts.farmproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FarmReportDTO {
    private Long farmId;
    private String farmName;
    private Long fieldId;
    private String cropType;
    private Double expectedAmount;
    private Double actualAmount;
}
