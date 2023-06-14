package com.twoday.zooanimalmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnimalRequestDto {
    @NotBlank
    private String species;
    @NotBlank
    @Pattern(regexp = "Carnivore|Herbivore", message =
            "invalid animal food value, " +
                    "value must match one of the provided: 'Carnivore', 'Herbivore'")
    private String food;
    @Positive
    @NotNull
    private Integer amount;
}
