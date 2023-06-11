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
public class EnclosureObject {
    @Id
    @SequenceGenerator(
            sequenceName = "enclosure_object_id_sequence",
            name = "enclosure_object_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "enclosure_object_id_sequence"
    )
    private Integer id;
    private String name;
    private String enclosureName;
}
