package com.kehinde.farm_collector.gts.farmproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "crop_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    private String cropType;

    private Double plantingArea;

    private Double expectedAmount;

    private Double actualAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType dataType;
}

