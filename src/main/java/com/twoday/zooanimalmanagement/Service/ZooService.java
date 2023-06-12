package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.dto.ZooRequestDto;
import com.twoday.zooanimalmanagement.model.Animal;
import com.twoday.zooanimalmanagement.model.Enclosure;
import com.twoday.zooanimalmanagement.model.EnclosureObject;
import com.twoday.zooanimalmanagement.model.Zoo;
import com.twoday.zooanimalmanagement.repository.AnimalRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureObjectRepository;
import com.twoday.zooanimalmanagement.repository.EnclosureRepository;
import com.twoday.zooanimalmanagement.repository.ZooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZooService {
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    EnclosureObjectRepository enclosureObjectRepository;
    
    @Autowired
    ZooRepository zooRepository;

    public List<Animal> createNewZoo(ZooRequestDto zoo) {
        saveZoo(zoo);
        saveEnclosures(zoo);
        saveAnimals(zoo.getAnimals(), zoo.getName());
        return animalRepository.findByZooName(zoo.getName());
    }

    private void saveZoo(ZooRequestDto zoo) {
        Zoo zooToSave = Zoo.builder()
                .name(zoo.getName())
                .build();
        zooRepository.save(zooToSave);
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

    public void saveAnimals(List<AnimalRequestDto> animalRequestDtos, String zooName) {
        List<Animal> animals = new ArrayList<>();
        animalRequestDtos.forEach(animalRequestDto -> animals.add(Animal.builder()
                        .species(animalRequestDto.getSpecies())
                        .food(animalRequestDto.getFood())
                        .amount(animalRequestDto.getAmount())
                        .build()));

        Map<String, List<AnimalRequestDto>> animalsGroupedBySpecies = animalRequestDtos.stream()
                .collect(Collectors.groupingBy(AnimalRequestDto::getFood));

        List<AnimalRequestDto> vegetarianAnimalDtos = animalsGroupedBySpecies.get("Herbivore");
        List<Animal> vegetarianAnimals = getMappedAnimals(vegetarianAnimalDtos, zooName);

        List<AnimalRequestDto> carnivoreAnimalDtos = animalsGroupedBySpecies.get("Carnivore");
        List<List<Animal>> groupedCarnivoreAnimals = getGroupedCarnivoreAnimals(carnivoreAnimalDtos, zooName);

        List<List<Animal>> allAnimalsGrouped = new ArrayList<>(groupedCarnivoreAnimals);
        allAnimalsGrouped.add(vegetarianAnimals);
        List<Enclosure> enclosures = enclosureRepository.findByZooName(zooName);
        List<Animal> outputAnimals = assignEnclosuresToAnimalGroups(allAnimalsGrouped, enclosures);

        animalRepository.saveAll(outputAnimals);
    }

    private List<Animal> assignEnclosuresToAnimalGroups(List<List<Animal>> groupedAnimals, List<Enclosure> enclosures) {
        groupedAnimals.sort(Comparator.comparingInt(animals -> animals.stream().mapToInt(Animal::getAmount).sum()));
        enclosures.sort(Comparator.comparingInt(enclosure -> getSizeOrder(enclosure.getSize())));

        Iterator<List<Animal>> groupedAnimalsIterator = groupedAnimals.iterator();
        Iterator<Enclosure> enclosuresIterator = enclosures.iterator();

        List<Animal> outputAnimalList = new ArrayList<>();

        while (groupedAnimalsIterator.hasNext()) {
            Enclosure enclosure = enclosuresIterator.next();
            groupedAnimalsIterator.next().forEach(animal -> {
                animal.setEnclosureName(enclosure.getName());
                outputAnimalList.add(animal);
            });
        }

        return outputAnimalList;
    }

    private int getSizeOrder(String size) {
        return switch (size) {
            case "Huge" -> 4;
            case "Large" -> 3;
            case "Medium" -> 2;
            case "Small" -> 1;
            default -> throw new IllegalArgumentException("Unknown size: " + size);
        };
    }


    private List<List<Animal>> getGroupedCarnivoreAnimals(
            List<AnimalRequestDto> carnivoreAnimalDtos,
            String zooName
    ) {
        int MAX_CARNIVORES_PER_ENCLOSURE = 2;
        if (carnivoreAnimalDtos == null) return List.of();
        int carnivoreSpeciesCount = carnivoreAnimalDtos.size();

        int emptyEnclosureCount = enclosureRepository.findByZooName(zooName).size() - 1;

        int numOfEnclosuresNeeded = (int) Math.ceil((float) carnivoreSpeciesCount / MAX_CARNIVORES_PER_ENCLOSURE);
        if (numOfEnclosuresNeeded > emptyEnclosureCount) throw new RuntimeException("not enough space");

        HashMap<Integer, List<AnimalRequestDto>> animalDtoGroups = new HashMap<>();
        Iterator<AnimalRequestDto> carnivoreAnimalIterator = carnivoreAnimalDtos.iterator();

        List<Integer> emptyEnclosuresNumbered = new ArrayList<>();
        for (int i = 0; i < emptyEnclosureCount; i++) emptyEnclosuresNumbered.add(i);
        Iterator<Integer> emptyEnclosuresNumberedIterator = emptyEnclosuresNumbered.iterator();

        while (carnivoreAnimalIterator.hasNext()) {
            if (!emptyEnclosuresNumberedIterator.hasNext()) {
                emptyEnclosuresNumberedIterator = emptyEnclosuresNumbered.iterator();
            }

            AnimalRequestDto animalDto = carnivoreAnimalIterator.next();
            Integer emptyEnclosureNumber = emptyEnclosuresNumberedIterator.next();

            if (animalDtoGroups.containsKey(emptyEnclosureNumber)) {
                List<AnimalRequestDto> assignedAnimals = animalDtoGroups.get(emptyEnclosureNumber);
                assignedAnimals.add(animalDto);
            } else {
                animalDtoGroups.put(
                        emptyEnclosureNumber,
                        new ArrayList<>(Collections.singletonList(animalDto))
                );
            }
        }

        List<List<Animal>> groupedCarnivoreAnimals = new ArrayList<>();
        animalDtoGroups.forEach((enclosure, animalRequestDtos) -> {
            groupedCarnivoreAnimals.add(getMappedAnimals(
                    animalRequestDtos,
                    zooName));
        });

        return groupedCarnivoreAnimals;
    }

    private List<Animal> getMappedAnimals(
            List<AnimalRequestDto> animalDtosToMap,
            String zooName
    ) {
        List<Animal> mappedAnimals = new ArrayList<>();
        animalDtosToMap.forEach(animalToMap -> {
            Animal animal = Animal.builder()
                    .species(animalToMap.getSpecies())
                    .food(animalToMap.getFood())
                    .zooName(zooName) // this
                    .amount(animalToMap.getAmount())
                    .build();
            mappedAnimals.add(animal);
        });
        return mappedAnimals;
    }
}
