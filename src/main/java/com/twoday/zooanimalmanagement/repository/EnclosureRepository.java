package com.twoday.zooanimalmanagement.repository;

import com.twoday.zooanimalmanagement.model.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnclosureRepository extends JpaRepository<Enclosure, Integer> {
    List<Enclosure> findByZooName(String zooName);
    void deleteByZooName(String zooName);

}
