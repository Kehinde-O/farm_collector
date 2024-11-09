package com.kehinde.farm_collector.gts.farmproject.service.impl;

import com.kehinde.farm_collector.gts.farmproject.entity.Farm;
import com.kehinde.farm_collector.gts.farmproject.exception.ResourceNotFoundException;
import com.kehinde.farm_collector.gts.farmproject.repository.FarmRepository;
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
}
