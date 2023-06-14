package com.twoday.zooanimalmanagement.service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.exception.ApiException;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.ZooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ZooRepository zooRepository;

    @Autowired
    private ZooService zooService;

    public List<Animal> addAnimals(List<AnimalRequestDto> animalRequestDtos, String zooName) {
        if (zooRepository.findByName(zooName).isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "Zoo with provided name doesn't exist");

        animalRepository.saveAll(zooService.getReassignedAnimals(animalRequestDtos, zooName));
        return animalRepository.findByZooName(zooName);
    }

    public List<Animal> removeById(Integer id, Integer amount) {
        Optional<Animal> animalToRemove = animalRepository.findById(id);
        if (animalToRemove.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "Animal not found");

        if (animalToRemove.get().getAmount() <= amount) {
            animalRepository.deleteById(id);
        } else {
            animalToRemove.get().setAmount(animalToRemove.get().getAmount() - amount);
        }

        animalRepository.saveAll(
                zooService.getReassignedAnimals(null, animalToRemove.get().getZooName()));
        return animalRepository.findAll();
    }

    public List<Animal> getAllByZooName(String zooName) {
        return animalRepository.findByZooName(zooName);
    }
}
