package com.twoday.zooanimalmanagement.controller;

import com.twoday.zooanimalmanagement.Service.ZooService;
import com.twoday.zooanimalmanagement.dto.ZooRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.twoday.zooanimalmanagement.ZooAnimalManagementApplication.BASE_URL;

@RestController
@RequestMapping(value = BASE_URL + "/zoo")
public class ZooController {

    @Autowired
    ZooService zooService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public List<Animal> createNewZoo(@RequestBody ZooRequestDto zoo) {
        return zooService.createNewZoo(zoo);
    }

}
