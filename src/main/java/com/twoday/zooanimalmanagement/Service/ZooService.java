package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZooService {
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    EnclosureRepository enclosureRepository;


}
