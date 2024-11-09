package com.kehinde.farm_collector.gts.farmproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SeasonReportDTO {
    private Long seasonId;
    private String seasonName;
    private List<FarmReportDTO> farmReports;
}
