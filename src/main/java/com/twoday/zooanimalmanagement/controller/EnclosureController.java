package com.twoday.zooanimalmanagement.controller;

import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
import com.twoday.zooanimalmanagement.dto.EnclosureRequestDtoList;
import com.twoday.zooanimalmanagement.dto.EnclosureReturnDto;
import com.twoday.zooanimalmanagement.service.EnclosureService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
    public List<EnclosureReturnDto> addEnclosures(
            @RequestBody @Valid EnclosureRequestDtoList enclosures, @RequestParam String zooName) {
        return enclosureService.addEnclosures(enclosures.getEnclosures(), zooName);
    }

    @DeleteMapping("/removeEnclosureById/{enclosureId}")
    public List<EnclosureReturnDto> removeEnclosureById(@PathVariable Integer enclosureId) {
        return enclosureService.removeEnclosureById(enclosureId);
    }

    @GetMapping("/getAllByZooName/{zooName}")
    public List<EnclosureReturnDto> getAllEnclosuresByZooName(@PathVariable String zooName) {
        return enclosureService.getAllEnclosuresByZooName(zooName);
    }
}
