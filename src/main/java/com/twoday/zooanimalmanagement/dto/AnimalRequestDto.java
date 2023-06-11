package com.twoday.zooanimalmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnimalRequestDto {
    private String species;
    private String food;
    private Integer amount;
}
