package com.twoday.zooanimalmanagement.repository;

import com.twoday.zooanimalmanagement.model.Zoo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZooRepository extends JpaRepository<Zoo, Integer> {
    List<Zoo> findByName(String name);
}
