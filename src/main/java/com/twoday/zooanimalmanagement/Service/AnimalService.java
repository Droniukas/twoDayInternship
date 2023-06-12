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
        zooService.saveAnimals(animalRequestDtos, zooName);
        return animalRepository.findByZooName(zooName);
    }

    public Animal removeAnimalById(Integer id, Integer amount) {
        Optional<Animal> animalToRemove = animalRepository.findById(id);
        if (animalToRemove.isEmpty()) throw new RuntimeException("No animal found");

        if (animalToRemove.get().getAmount() <= amount) {
            animalRepository.deleteById(id);
        } else {
            animalToRemove.get().setAmount(animalToRemove.get().getAmount() - amount);
        }

        zooService.saveAnimals(null, animalToRemove.get().getZooName());
        return animalToRemove.get(); // make sure to finish implementing getting the original animal
    }
}
