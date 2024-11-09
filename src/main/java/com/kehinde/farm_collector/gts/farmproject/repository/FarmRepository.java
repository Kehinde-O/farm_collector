package com.kehinde.farm_collector.gts.farmproject.repository;

import com.kehinde.farm_collector.gts.farmproject.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
}