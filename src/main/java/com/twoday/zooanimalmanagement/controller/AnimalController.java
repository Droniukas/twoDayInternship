package com.twoday.zooanimalmanagement.controller;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDtoList;
import com.twoday.zooanimalmanagement.exception.ApiException;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.service.AnimalService;
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
    public List<Animal> addAnimals(@RequestBody @Valid AnimalRequestDtoList animals, @RequestParam String zooName) {
        return animalService.addAnimals(animals.getAnimals(), zooName);
    }

    @PutMapping("/removeById/{id}")
    public List<Animal> removeById(@PathVariable Integer id, @RequestParam Integer amount) {
        if (amount <= 0) throw new ApiException(HttpStatus.BAD_REQUEST, "Amount to delete must be more than 0");
        return animalService.removeById(id, amount);
    }

    @GetMapping("/getAllByZooName/{zooName}")
    public List<Animal> getAllByZooName(@PathVariable String zooName) {
        return animalService.getAllByZooName(zooName);
    }
}
