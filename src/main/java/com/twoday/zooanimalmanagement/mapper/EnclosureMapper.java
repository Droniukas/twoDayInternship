package com.twoday.zooanimalmanagement.mapper;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.dto.EnclosureReturnDto;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.model.EnclosureObject;

import java.util.ArrayList;
import java.util.List;

public class EnclosureMapper {

    public static List<Enclosure> mapEnclosureRequestDtosToModels(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        List<Enclosure> outputEnclosures = new ArrayList<>();
        if (enclosureRequestDtos == null) return outputEnclosures;
        enclosureRequestDtos.forEach(enclosureRequestDto -> {
            Enclosure enclosure = Enclosure.builder()
                    .name(enclosureRequestDto.getName())
                    .size(enclosureRequestDto.getSize())
                    .location(enclosureRequestDto.getLocation())
                    .zooName(zooName)
                    .build();
            List<EnclosureObject> enclosureObjects = new ArrayList<>();
            enclosureRequestDto.getObjects().forEach(enclosureObject -> {
                enclosureObjects.add(EnclosureObject.builder()
                        .name(enclosureObject)
                        .enclosure(enclosure)
                        .build());
            });
            enclosure.setEnclosureObjects(enclosureObjects);
            outputEnclosures.add(enclosure);
        });
        return outputEnclosures;
    }

    public static List<EnclosureReturnDto> mapEnclosuresToEnclosureReturnDtos(List<Enclosure> enclosures) {
        List<EnclosureReturnDto> enclosureReturnDtos = new ArrayList<>();
        enclosures.forEach(enclosure -> {
            EnclosureReturnDto enclosureReturnDto = EnclosureReturnDto.builder()
                    .id(enclosure.getId())
                    .name(enclosure.getName())
                    .size(enclosure.getSize())
                    .location(enclosure.getLocation())
                    .zooName(enclosure.getZooName())
                    .build();
            List<String> enclosureObjectNames = new ArrayList<>();
            enclosure.getEnclosureObjects().forEach(enclosureObject -> {
                enclosureObjectNames.add(enclosureObject.getName());
            });
            enclosureReturnDto.setEnclosureObjectNames(enclosureObjectNames);
            enclosureReturnDtos.add(enclosureReturnDto);
        });
        return enclosureReturnDtos;
    }
}
