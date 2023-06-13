package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.mapper.EnclosureMapper;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureObjectRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnclosureService {

    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    EnclosureObjectRepository enclosureObjectRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    ZooService zooService;

    public List<Enclosure> addEnclosures(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        enclosureRepository.saveAll(
                EnclosureMapper.mapEnclosureRequestDtosToModels(enclosureRequestDtos, zooName));
        enclosureObjectRepository.saveAll(
                EnclosureMapper.mapEnclosureRequestDtosToEnclosureObjects(enclosureRequestDtos));

        animalRepository.saveAll(zooService.getReassignedAnimals(null, zooName));

        return enclosureRepository.findByZooName(zooName);
    }
}
