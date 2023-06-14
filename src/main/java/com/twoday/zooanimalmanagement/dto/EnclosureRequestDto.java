package com.twoday.zooanimalmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    private String name;
    @NotNull
    @Pattern(regexp = "Small|Medium|Large|Huge", message =
            "invalid enclosure size value, " +
                    "value must match one of the provided: 'Small', 'Medium', 'Large', 'Huge'")
    private String size;
    @NotBlank
    private String location;
    private List<String> objects;

}
