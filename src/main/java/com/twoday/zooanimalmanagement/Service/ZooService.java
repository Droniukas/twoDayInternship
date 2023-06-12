package com.twoday.zooanimalmanagement.Service;

import com.twoday.zooanimalmanagement.dto.AnimalRequestDto;
import com.twoday.zooanimalmanagement.dto.EnclosureRequestDto;
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
        String zooName = zoo.getName();

        zooRepository.save(Zoo.builder().name(zooName).build());
        addEnclosures(zoo.getEnclosures(), zooName);
        saveAnimals(zoo.getAnimals(), zooName);

        return animalRepository.findByZooName(zooName);
    }

    private void addEnclosures(List<EnclosureRequestDto> enclosureRequestDtos, String zooName) {
        enclosureRequestDtos.forEach(enclosureRequestDto -> {
            Enclosure enclosure = Enclosure.builder()
                    .name(enclosureRequestDto.getName())
                    .size(enclosureRequestDto.getSize())
                    .location(enclosureRequestDto.getLocation())
                    .zooName(zooName)
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

    public void saveAnimals(List<AnimalRequestDto> newAnimalRequestDtos, String zooName) {
        List<Animal> animals = mapAnimalRequestDtosToModels(newAnimalRequestDtos, zooName);

        Map<String, List<Animal>> animalsGroupedBySpecies = animals.stream()
                .collect(Collectors.groupingBy(Animal::getFood));

        List<Animal> vegetarianAnimals = animalsGroupedBySpecies.get("Herbivore");

        List<Animal> carnivoreAnimals = animalsGroupedBySpecies.get("Carnivore");
        List<List<Animal>> groupedCarnivoreAnimals = getGroupedCarnivoreAnimals(carnivoreAnimals, zooName);

        List<List<Animal>> allAnimalsGrouped = new ArrayList<>(groupedCarnivoreAnimals);
        allAnimalsGrouped.add(vegetarianAnimals);

        List<Enclosure> enclosures = enclosureRepository.findByZooName(zooName);
        List<Animal> outputAnimals = assignEnclosuresToAnimalGroups(allAnimalsGrouped, enclosures);

        animalRepository.saveAll(outputAnimals);
    }

    private List<Animal> mapAnimalRequestDtosToModels(List<AnimalRequestDto> animalRequestDtos, String zooName) {
        List<Animal> animals = new ArrayList<>(animalRepository.findByZooName(zooName));
        if (animalRequestDtos == null) return animals;

        for (AnimalRequestDto animalRequestDto : animalRequestDtos) {

            Optional<Animal> possibleExistingAnimal = animals.stream().filter(animal ->
                    animal.getSpecies().equals(animalRequestDto.getSpecies())
                            && animal.getFood().equals(animalRequestDto.getFood())).findFirst();

            if (possibleExistingAnimal.isPresent()) {
                Animal existingAnimal = possibleExistingAnimal.get();
                existingAnimal.setAmount(existingAnimal.getAmount() + animalRequestDto.getAmount());
                continue;
            }

            animals.add(Animal.builder()
                    .species(animalRequestDto.getSpecies())
                    .food(animalRequestDto.getFood())
                    .amount(animalRequestDto.getAmount())
                    .zooName(zooName)
                    .build());
        };

        return animals;
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
            List<Animal> carnivoreAnimals,
            String zooName
    ) {
        int MAX_CARNIVORES_PER_ENCLOSURE = 2;
        if (carnivoreAnimals == null) return List.of();
        int carnivoreSpeciesCount = carnivoreAnimals.size();

        int emptyEnclosureCount = enclosureRepository.findByZooName(zooName).size() - 1;

        int numOfEnclosuresNeeded = (int) Math.ceil((float) carnivoreSpeciesCount / MAX_CARNIVORES_PER_ENCLOSURE);
        if (numOfEnclosuresNeeded > emptyEnclosureCount) throw new RuntimeException("not enough space");

        HashMap<Integer, List<Animal>> carnivoreAnimalGroups = new HashMap<>();
        Iterator<Animal> carnivoreAnimalIterator = carnivoreAnimals.iterator();

        List<Integer> emptyEnclosuresNumbered = new ArrayList<>();
        for (int i = 0; i < emptyEnclosureCount; i++) emptyEnclosuresNumbered.add(i);
        Iterator<Integer> emptyEnclosuresNumberedIterator = emptyEnclosuresNumbered.iterator();

        while (carnivoreAnimalIterator.hasNext()) {
            if (!emptyEnclosuresNumberedIterator.hasNext()) {
                emptyEnclosuresNumberedIterator = emptyEnclosuresNumbered.iterator();
            }

            Animal animal = carnivoreAnimalIterator.next();
            Integer emptyEnclosureNumber = emptyEnclosuresNumberedIterator.next();

            if (carnivoreAnimalGroups.containsKey(emptyEnclosureNumber)) {
                List<Animal> assignedAnimals = carnivoreAnimalGroups.get(emptyEnclosureNumber);
                assignedAnimals.add(animal);
            } else {
                carnivoreAnimalGroups.put(
                        emptyEnclosureNumber,
                        new ArrayList<>(Collections.singletonList(animal))
                );
            }
        }

        List<List<Animal>> groupedCarnivoreAnimals = new ArrayList<>();
        carnivoreAnimalGroups.forEach((enclosure, animals) -> {
            groupedCarnivoreAnimals.add(animals);
        });

        return groupedCarnivoreAnimals;
    }
}
