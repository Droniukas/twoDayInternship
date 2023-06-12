package com.twoday.zooanimalmanagement.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EnclosureRequestDto {
    private String name;
    private String size;
    private String location;
    private List<String> objects;

}
