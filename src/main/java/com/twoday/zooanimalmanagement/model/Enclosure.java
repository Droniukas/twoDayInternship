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
    private String name;
    private Integer size;
    private String location;
    private List<String> objects;
    private String zooName;

}
