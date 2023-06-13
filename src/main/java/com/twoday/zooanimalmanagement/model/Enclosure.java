package com.twoday.zooanimalmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class Enclosure {
    @Id
    @SequenceGenerator(
            sequenceName = "animal_id_sequence",
            name = "animal_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "animal_id_sequence"
    )
    private Integer id;
    private String name;
    private String size;
    private String location;
    private String zooName;

}
