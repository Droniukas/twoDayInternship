package com.twoday.zooanimalmanagement.dto;

import com.twoday.zooanimalmanagement.model.Enclosure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Zoo {
    private String name;
    private List<AnimalRequestDto> animals;
    private List<Enclosure> enclosures;
}
