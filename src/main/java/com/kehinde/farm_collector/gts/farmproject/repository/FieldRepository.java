package com.kehinde.farm_collector.gts.farmproject.repository;

import com.kehinde.farm_collector.gts.farmproject.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<Field> findByNameAndFarm(String fieldName, String farmName);
}
