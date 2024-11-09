package com.kehinde.farm_collector.gts.farmproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kehinde.farm_collector.gts.farmproject.controller.FarmController;
import com.kehinde.farm_collector.gts.farmproject.dto.FarmReportDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.HarvestedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.PlantedRequestDTO;
import com.kehinde.farm_collector.gts.farmproject.dto.SeasonReportDTO;
import com.kehinde.farm_collector.gts.farmproject.entity.Farm;
import com.kehinde.farm_collector.gts.farmproject.service.FarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@WebMvcTest(FarmController.class)
class FarmProjectApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FarmService farmService;

    @Autowired
    private ObjectMapper objectMapper;

    private Farm greenAcres;
    private Farm sunnyFields;
    private PlantedRequestDTO cornPlantedData;
    private PlantedRequestDTO wheatPlantedData;
    private HarvestedRequestDTO cornHarvestedData;
    private HarvestedRequestDTO wheatHarvestedData;
    private SeasonReportDTO seasonReport;

    @BeforeEach
    public void setUp() {
        // Realistic farm data
        greenAcres = new Farm(1L, "Green Acres", "Valley View");
        sunnyFields = new Farm(2L, "Sunny Fields", "River Bend");

        // Dummy planted data for Green Acres - Corn
        cornPlantedData = new PlantedRequestDTO();
        cornPlantedData.setFarmId(1L);
        cornPlantedData.setFieldId(1L);
        cornPlantedData.setSeasonId(1L);
        cornPlantedData.setCropType("Corn");
        cornPlantedData.setPlantingArea(120.0);
        cornPlantedData.setExpectedAmount(300.0);

        // Dummy planted data for Sunny Fields - Wheat
        wheatPlantedData = new PlantedRequestDTO();
        wheatPlantedData.setFarmId(2L);
        wheatPlantedData.setFieldId(2L);
        wheatPlantedData.setSeasonId(1L);
        wheatPlantedData.setCropType("Wheat");
        wheatPlantedData.setPlantingArea(80.0);
        wheatPlantedData.setExpectedAmount(150.0);

        // Dummy harvested data for Green Acres - Corn
        cornHarvestedData = new HarvestedRequestDTO();
        cornHarvestedData.setFarmId(1L);
        cornHarvestedData.setFieldId(1L);
        cornHarvestedData.setSeasonId(1L);
        cornHarvestedData.setCropType("Corn");
        cornHarvestedData.setActualAmount(280.0);

        // Dummy harvested data for Sunny Fields - Wheat
        wheatHarvestedData = new HarvestedRequestDTO();
        wheatHarvestedData.setFarmId(2L);
        wheatHarvestedData.setFieldId(2L);
        wheatHarvestedData.setSeasonId(1L);
        wheatHarvestedData.setCropType("Wheat");
        wheatHarvestedData.setActualAmount(140.0);

        // Season report based on dummy inputs
        seasonReport = new SeasonReportDTO(1L, "Spring 2024", List.of(
                new FarmReportDTO(1L, "Green Acres", 1L, "Corn", 300.0, 280.0),
                new FarmReportDTO(2L, "Sunny Fields", 2L, "Wheat", 150.0, 140.0)
        ));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testCreateFarm() throws Exception {
        when(farmService.addFarm(any(Farm.class))).thenReturn(greenAcres);

        mockMvc.perform(post("/api/v1/farms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(greenAcres)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greenAcres.getId()))
                .andExpect(jsonPath("$.name").value(greenAcres.getName()))
                .andExpect(jsonPath("$.location").value(greenAcres.getLocation()));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testGetAllFarms() throws Exception {
        when(farmService.getAllFarms()).thenReturn(List.of(greenAcres, sunnyFields));

        mockMvc.perform(get("/api/v1/farms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(greenAcres.getId()))
                .andExpect(jsonPath("$[0].name").value(greenAcres.getName()))
                .andExpect(jsonPath("$[0].location").value(greenAcres.getLocation()))
                .andExpect(jsonPath("$[1].id").value(sunnyFields.getId()))
                .andExpect(jsonPath("$[1].name").value(sunnyFields.getName()))
                .andExpect(jsonPath("$[1].location").value(sunnyFields.getLocation()));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testGetFarmById() throws Exception {
        when(farmService.getFarmById(1L)).thenReturn(greenAcres);

        mockMvc.perform(get("/api/v1/farms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greenAcres.getId()))
                .andExpect(jsonPath("$.name").value(greenAcres.getName()))
                .andExpect(jsonPath("$.location").value(greenAcres.getLocation()));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testSubmitPlantedData() throws Exception {
        mockMvc.perform(post("/api/v1/farms/planted")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cornPlantedData)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Planted data submitted successfully"));

        mockMvc.perform(post("/api/v1/farms/planted")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheatPlantedData)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Planted data submitted successfully"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testSubmitHarvestedData() throws Exception {
        mockMvc.perform(post("/api/v1/farms/harvested")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cornHarvestedData)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Harvested data submitted successfully"));

        mockMvc.perform(post("/api/v1/farms/harvested")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheatHarvestedData)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Harvested data submitted successfully"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    public void testGetSeasonReport() throws Exception {
        when(farmService.generateSeasonReport(1L)).thenReturn(seasonReport);

        mockMvc.perform(get("/api/v1/farms/report/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seasonId").value(seasonReport.getSeasonId()))
                .andExpect(jsonPath("$.seasonName").value(seasonReport.getSeasonName()))
                .andExpect(jsonPath("$.farmReports[0].farmId").value(seasonReport.getFarmReports().get(0).getFarmId()))
                .andExpect(jsonPath("$.farmReports[0].cropType").value(seasonReport.getFarmReports().get(0).getCropType()))
                .andExpect(jsonPath("$.farmReports[0].expectedAmount").value(seasonReport.getFarmReports().get(0).getExpectedAmount()))
                .andExpect(jsonPath("$.farmReports[0].actualAmount").value(seasonReport.getFarmReports().get(0).getActualAmount()))
                .andExpect(jsonPath("$.farmReports[1].farmId").value(seasonReport.getFarmReports().get(1).getFarmId()))
                .andExpect(jsonPath("$.farmReports[1].cropType").value(seasonReport.getFarmReports().get(1).getCropType()))
                .andExpect(jsonPath("$.farmReports[1].expectedAmount").value(seasonReport.getFarmReports().get(1).getExpectedAmount()))
                .andExpect(jsonPath("$.farmReports[1].actualAmount").value(seasonReport.getFarmReports().get(1).getActualAmount()));
    }
}
