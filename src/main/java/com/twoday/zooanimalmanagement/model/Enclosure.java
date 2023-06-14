package com.twoday.zooanimalmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    private String size;
    private String location;
    private String zooName;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="enclosure")
    private List<EnclosureObject> enclosureObjects;
}
