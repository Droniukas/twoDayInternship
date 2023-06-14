package com.twoday.zooanimalmanagement.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EnclosureReturnDto {
    private Integer id;
    private String name;
    private String size;
    private String location;
    private String zooName;
    private List<String> enclosureObjectNames;
}
