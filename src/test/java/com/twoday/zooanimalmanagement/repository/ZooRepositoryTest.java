package com.twoday.zooanimalmanagement.repository;

import com.twoday.zooanimalmanagement.model.Zoo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class ZooRepositoryTest {

    @Autowired
    private ZooRepository underTest;

    @Test
    void itShouldZooFindByName() {
        String zooName = "zooName";
        underTest.save(Zoo.builder().name(zooName).build());

        List<Zoo> actualZoo = underTest.findByName(zooName);

        assertThat(actualZoo.stream().findFirst()).isPresent();
        assertThat(actualZoo.stream().findFirst().get().getName()).isEqualTo(zooName);
    }
}