package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ZooService zooService;

    public List<Animal> addAnimals(List<AnimalRequestDto> animalRequestDtos, String zooName) {
        animalRepository.saveAll(zooService.getReassignedAnimals(animalRequestDtos, zooName));
        return animalRepository.findByZooName(zooName);
    }

    public List<Animal> removeById(Integer id, Integer amount) {
        Optional<Animal> animalToRemove = animalRepository.findById(id);
        if (animalToRemove.isEmpty()) throw new RuntimeException("No animal found");

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
