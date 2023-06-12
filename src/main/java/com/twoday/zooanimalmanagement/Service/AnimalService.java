package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ZooService zooService;

    public List<Animal> addAnimals(List<AnimalRequestDto> animalRequestDtos, String zooName) {
        zooService.saveAnimals(animalRequestDtos, zooName);
        return animalRepository.findByZooName(zooName);
    }
}
