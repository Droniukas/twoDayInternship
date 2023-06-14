package com.twoday.zooanimalmanagement.dto;

import com.twoday.zooanimalmanagement.model.Enclosure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ZooRequestDto {
    @NotBlank(message = "Name must not be null and have at least one non whitespace character")
    private String name;
    private List<@Valid AnimalRequestDto> animals;
    private List<@Valid EnclosureRequestDto> enclosures;
}
