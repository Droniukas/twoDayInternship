package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.mapper.EnclosureMapper;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureObjectRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public List<Enclosure> addEnclosures(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        enclosureRepository.saveAll(
                EnclosureMapper.mapEnclosureRequestDtosToModels(enclosureRequestDtos, zooName));
        enclosureObjectRepository.saveAll(
                EnclosureMapper.mapEnclosureRequestDtosToEnclosureObjects(enclosureRequestDtos));

        animalRepository.saveAll(zooService.getReassignedAnimals(null, zooName));

        return enclosureRepository.findByZooName(zooName);
    }

    public List<Enclosure> getAllEnclosuresByZooName(String zooName) {
        return enclosureRepository.findByZooName(zooName);
    }

    @Transactional
    public List<Enclosure> removeEnclosureById(Integer id) {
        Optional<Enclosure> originalEnclosure = enclosureRepository.findById(id);
        if (originalEnclosure.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enclosure not found");

        enclosureRepository.deleteById(id);
        animalRepository.saveAll(zooService.getReassignedAnimals(
                null, originalEnclosure.get().getZooName()));

        return enclosureRepository.findByZooName(originalEnclosure.get().getZooName());
    }
}
