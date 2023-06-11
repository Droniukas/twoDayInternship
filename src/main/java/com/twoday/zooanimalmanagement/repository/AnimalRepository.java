package com.twoday.zooanimalmanagement.repository;

import com.twoday.zooanimalmanagement.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {
}
