package com.twoday.zooanimalmanagement.repository;

import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.model.Zoo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZooRepository extends JpaRepository<Zoo, Integer> {
}
