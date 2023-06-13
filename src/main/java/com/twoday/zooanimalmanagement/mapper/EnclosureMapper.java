package com.twoday.zooanimalmanagement.mapper;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.model.EnclosureObject;

import java.util.ArrayList;
import java.util.List;

public class EnclosureMapper {

    public static List<Enclosure> mapEnclosureRequestDtosToModels(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        List<Enclosure> outputEnclosures = new ArrayList<>();
        enclosureRequestDtos.forEach(enclosureRequestDto -> {
            Enclosure enclosure = Enclosure.builder()
                    .name(enclosureRequestDto.getName())
                    .size(enclosureRequestDto.getSize())
                    .location(enclosureRequestDto.getLocation())
                    .zooName(zooName)
                    .build();

            outputEnclosures.add(enclosure);
        });
        return outputEnclosures;
    }

    public static List<EnclosureObject> mapEnclosureRequestDtosToEnclosureObjects(
            List<EnclosureRequestDto> enclosureRequestDtos
    ) {
        List<EnclosureObject> outputEnclosureObjects = new ArrayList<>();

        enclosureRequestDtos.forEach(enclosureRequestDto -> {
            enclosureRequestDto.getObjects().forEach(object -> {
                EnclosureObject enclosureObject = EnclosureObject.builder()
                        .name(object)
                        .enclosureName(enclosureRequestDto.getName())
                        .build();

                outputEnclosureObjects.add(enclosureObject);
            });
        });
        return outputEnclosureObjects;
    }
}
