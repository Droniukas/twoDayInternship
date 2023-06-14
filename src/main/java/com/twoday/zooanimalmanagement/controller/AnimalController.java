package com.twoday.zooanimalmanagement.controller;

import com.twoday.zooanimalmanagement.service.AnimalService;
import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.twoday.zooanimalmanagement.ZooAnimalManagementApplication.BASE_URL;

@RestController
@RequestMapping(value = BASE_URL + "/zoo/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addAnimals")
    public List<Animal> addAnimals(@RequestBody List<@Valid AnimalRequestDto> animals, @RequestParam String zooName) {
        return animalService.addAnimals(animals, zooName);
    }

    @PutMapping("/removeById/{id}")
    public List<Animal> removeById(@PathVariable Integer id, @RequestParam @Valid @Positive Integer amount) {
        return animalService.removeById(id, amount);
    }

    @GetMapping("/getAllByZooName/{zooName}")
    public List<Animal> getAllByZooName(@PathVariable String zooName) {
        return animalService.getAllByZooName(zooName);
    }
}
