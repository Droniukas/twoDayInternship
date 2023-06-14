package com.twoday.zooanimalmanagement.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnimalRequestDtoList {
    private List<@Valid AnimalRequestDto> animals;
}
