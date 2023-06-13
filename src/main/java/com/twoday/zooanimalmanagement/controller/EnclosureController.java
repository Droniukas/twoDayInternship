package com.twoday.zooanimalmanagement.controller;

import com.twoday.zooanimalmanagement.Service.EnclosureService;
import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.model.Enclosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.twoday.zooanimalmanagement.ZooAnimalManagementApplication.BASE_URL;

@RestController
@RequestMapping(value = BASE_URL + "/zoo/enclosure")
public class EnclosureController {

    @Autowired
    EnclosureService enclosureService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addEnclosures")
    public List<Enclosure> addEnclosures(
            @RequestBody List<EnclosureRequestDto> enclosures, @RequestParam String zooName) {
        return enclosureService.addEnclosures(enclosures, zooName);
    }

    @DeleteMapping("/removeEnclosureById/{enclosureId}")
    public List<Enclosure> removeEnclosureById(@PathVariable Integer enclosureId) {
        return enclosureService.removeEnclosureById(enclosureId);
    }

    @GetMapping("/getAllByZooName/{zooName}")
    public List<Enclosure> getAllEnclosuresByZooName(@PathVariable String zooName) {
        return enclosureService.getAllEnclosuresByZooName(zooName);
    }
}
