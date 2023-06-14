package com.twoday.zooanimalmanagement.service;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.dto.EnclosureReturnDto;
import com.twoday.zooanimalmanagement.exception.ApiException;
import com.twoday.zooanimalmanagement.mapper.EnclosureMapper;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import com.twoday.zooanimalmanagement.repository.ZooRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnclosureService {

    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    ZooRepository zooRepository;

    @Autowired
    ZooService zooService;

    @Transactional
    public List<EnclosureReturnDto> addEnclosures(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        if (zooRepository.findByName(zooName).isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "Zoo with provided name doesn't exist");

        enclosureRepository.saveAll(
                EnclosureMapper.mapEnclosureRequestDtosToModels(enclosureRequestDtos, zooName));

        animalRepository.saveAll(zooService.getReassignedAnimals(null, zooName));

        return EnclosureMapper.mapEnclosuresToEnclosureReturnDtos(enclosureRepository.findByZooName(zooName));
    }

    public List<EnclosureReturnDto> getAllEnclosuresByZooName(String zooName) {
        return EnclosureMapper.mapEnclosuresToEnclosureReturnDtos(enclosureRepository.findByZooName(zooName));
    }

    @Transactional
    public List<EnclosureReturnDto> removeEnclosureById(Integer id) {
        Optional<Enclosure> originalEnclosure = enclosureRepository.findById(id);
        if (originalEnclosure.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "Enclosure not found");

        enclosureRepository.deleteById(id);
        animalRepository.saveAll(zooService.getReassignedAnimals(
                null, originalEnclosure.get().getZooName()));

        return EnclosureMapper.mapEnclosuresToEnclosureReturnDtos(enclosureRepository.findByZooName(originalEnclosure.get().getZooName()));
    }
}
