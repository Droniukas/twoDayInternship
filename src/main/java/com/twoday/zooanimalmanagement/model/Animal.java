package com.twoday.zooanimalmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class Animal {
    @Id
    @SequenceGenerator(
            sequenceName = "animal_id_sequence",
            name = "animal_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "animal_id_sequence"
    )
    private Integer id;
    private String species;
    private String food;
    private String enclosureName;
    private String zooName;
}
