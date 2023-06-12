package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.dto.ZooRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.model.EnclosureObject;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureObjectRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ZooService {
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    EnclosureObjectRepository enclosureObjectRepository;


    public List<Animal> createNewZoo(ZooRequestDto zoo) {
        saveEnclosures(zoo);
        saveAnimals(zoo);
        return animalRepository.findAll();
    }

    private void saveEnclosures(ZooRequestDto zoo) {
        zoo.getEnclosures().forEach(enclosureRequestDto -> {
            Enclosure enclosure = Enclosure.builder()
                    .name(enclosureRequestDto.getName())
                    .size(enclosureRequestDto.getSize())
                    .location(enclosureRequestDto.getLocation())
                    .zooName(zoo.getName())
                    .build();

            enclosureRequestDto.getObjects().forEach(object -> {
                EnclosureObject enclosureObject = EnclosureObject.builder()
                        .name(object)
                        .enclosureName(enclosureRequestDto.getName())
                        .build();

                enclosureObjectRepository.save(enclosureObject);
            });

            enclosureRepository.save(enclosure);
        });
    }

    private void saveAnimals(ZooRequestDto zoo) {
        Map<String, List<AnimalRequestDto>> animalsGroupedBySpecies = zoo.getAnimals().stream()
                .collect(Collectors.groupingBy(AnimalRequestDto::getFood));

        List<AnimalRequestDto> vegetarianAnimalDtos = animalsGroupedBySpecies.get("Herbivore");
        List<Animal> vegetarianAnimals = getMappedAnimals(vegetarianAnimalDtos, zoo);

        List<AnimalRequestDto> carnivoreAnimalDtos = animalsGroupedBySpecies.get("Carnivore");
        List<List<Animal>> groupedCarnivoreAnimals = getGroupedCarnivoreAnimals(carnivoreAnimalDtos, zoo);

        List<List<Animal>> allAnimalsGrouped = new ArrayList<>(groupedCarnivoreAnimals);
        allAnimalsGrouped.add(vegetarianAnimals);
        assignEnclosuresToAllAnimals(allAnimalsGrouped);

        System.out.println(enclosureRepository.findAll());
    }



    private void assignEnclosuresToAllAnimals(List<List<Animal>> groupedAnimals) {
        groupedAnimals.sort(Comparator.comparingInt(animals -> animals.stream().mapToInt(Animal::getAmount).sum()));
    }


//    private EnclosureRequestDto findSuitableEnclosure(List<EnclosureRequestDto> enclosures) {
//        List<EnclosureRequestDto> emptyEnclosures = new ArrayList<>();
//        enclosures.forEach(enclosure -> {
//            if (animalRepository.findAll().stream().noneMatch(
//                    animal -> animal.getEnclosureName().equals(enclosure.getName()))
//            ) {
//                emptyEnclosures.add(enclosure);
//            }
//        });
//        return emptyEnclosures.stream().findFirst().get(); // tas yra negerai nes mes cia nekreipiam demesio i enclosure dydi (fix this)
//    }

    private List<Animal> getMappedAnimals(
            List<AnimalRequestDto> animalDtosToMap,
            ZooRequestDto zoo
    ) {
        List<Animal> mappedAnimals = new ArrayList<>();
        animalDtosToMap.forEach(animalToMap -> {
            Animal animal = Animal.builder()
                    .species(animalToMap.getSpecies())
                    .food(animalToMap.getFood())
                    .zooName(zoo.getName())
                    .amount(animalToMap.getAmount())
                    .build();
            mappedAnimals.add(animal);
        });
        return mappedAnimals;
    }

    private List<List<Animal>> getGroupedCarnivoreAnimals(
            List<AnimalRequestDto> carnivoreAnimalDtos,
            ZooRequestDto zoo
    ) {
        int MAX_CARNIVORES_PER_ENCLOSURE = 2;
        int carnivoreSpeciesCount = carnivoreAnimalDtos.size();
        if (carnivoreSpeciesCount == 0) return List.of();

        int emptyEnclosureCount = zoo.getEnclosures().size() - 1;

        int numOfEnclosuresNeeded = (int) Math.ceil((float) carnivoreSpeciesCount / MAX_CARNIVORES_PER_ENCLOSURE);
        if (numOfEnclosuresNeeded > emptyEnclosureCount) throw new RuntimeException("not enough space");

        HashMap<Integer, List<AnimalRequestDto>> animalDtoGroups = new HashMap<>();
        Iterator<AnimalRequestDto> carnivoreAnimalIterator = carnivoreAnimalDtos.iterator();

        List<Integer> numbersRepresentingEmptyEnclosures = new ArrayList<>();
        for (int i = 0; i < emptyEnclosureCount; i++) numbersRepresentingEmptyEnclosures.add(i);
        Iterator<Integer> numbersRepresentingEmptyEnclosuresIterator = numbersRepresentingEmptyEnclosures.iterator();

        while (carnivoreAnimalIterator.hasNext()) {
            if (!numbersRepresentingEmptyEnclosuresIterator.hasNext()) {
                numbersRepresentingEmptyEnclosuresIterator = numbersRepresentingEmptyEnclosures.iterator();
            }

            AnimalRequestDto animalDto = carnivoreAnimalIterator.next();
            Integer numberRepresentingEmptyEnclosure = numbersRepresentingEmptyEnclosuresIterator.next();

            if (animalDtoGroups.containsKey(numberRepresentingEmptyEnclosure)) {
                List<AnimalRequestDto> assignedAnimals = animalDtoGroups.get(numberRepresentingEmptyEnclosure);
                assignedAnimals.add(animalDto);
            } else {
                animalDtoGroups.put(
                        numberRepresentingEmptyEnclosure,
                        new ArrayList<>(Collections.singletonList(animalDto))
                );
            }
        }

        List<List<Animal>> groupedCarnivoreAnimals = new ArrayList<>();

        animalDtoGroups.forEach((enclosure, animalRequestDtos) -> {
            groupedCarnivoreAnimals.add(getMappedAnimals(
                    animalRequestDtos,
                    zoo));
        });

        return groupedCarnivoreAnimals;
    }
}
