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

    public List<Animal> createNewZoo(ZooRequestDto zoo) {
        saveEnclosures(zoo);
        saveAnimals(zoo);
        return List.of();
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
        List<AnimalRequestDto> vegetarianAnimalDtos = zoo.getAnimals().stream()
                .filter(animal -> animal.getFood().equals("Herbivore"))
                .toList();
        EnclosureRequestDto enclosure = findSuitableEnclosure(zoo.getEnclosures());
        List<Animal> animals = getMappedAnimals(vegetarianAnimalDtos, enclosure, zoo);
        animals.forEach(animal -> animalRepository.save(animal));
    }

    private EnclosureRequestDto findSuitableEnclosure(List<EnclosureRequestDto> enclosures) {
        List<EnclosureRequestDto> emptyEnclosures = new ArrayList<>();
        enclosures.forEach(enclosure -> {
            if (animalRepository.findAll().stream().noneMatch(
                    animal -> animal.getEnclosureName().equals(enclosure.getName()))
            ) {
                emptyEnclosures.add(enclosure);
            }
        });
        return emptyEnclosures.stream().findFirst().get(); // tas yra negerai nes mes cia nekreipiam demesio i enclosure dydi (fix this)
    }

    private List<Animal> getMappedAnimals(
            List<AnimalRequestDto> animalDtosToMap,
            EnclosureRequestDto enclosure,
            ZooRequestDto zoo
    ) {
        List<Animal> animals = new ArrayList<>();
        animalDtosToMap.forEach(animalRequestDto -> {
            Animal animal = Animal.builder()
                    .species(animalRequestDto.getSpecies())
                    .food(animalRequestDto.getFood())
                    .zooName(zoo.getName())
                    .enclosureName(enclosure.getName())
                    .build();
            for (int i = 1; i <= animalRequestDto.getAmount(); i++) {
                animals.add(animal);
            }
        });
        return animals;
    }
//
//    public void transferAnimals(List<Animal> animals, List<Enclosure> enclosures) {
//        // Group vegetarian animals in the same enclosure
//        List<Animal> vegetarianAnimals = animals.stream()
//                .filter(animal -> animal.getFood().equals("Herbivore"))
//                .toList();
//
//        assignAnimalsToEnclosures(vegetarianAnimals, enclosures);
//
//        // Group carnivore animals by species
//        Map<String, List<Animal>> carnivoreAnimalsBySpecies = animals.stream()
//                .filter(animal -> animal.getFood().equals("Carnivore"))
//                .collect(Collectors.groupingBy(Animal::getSpecies));
//        System.out.println(carnivoreAnimalsBySpecies);
//
//        // Assign carnivore animals of the same species to the same enclosure
//        carnivoreAnimalsBySpecies.forEach((species, carnivoreAnimals) ->
//                assignAnimalsToEnclosures(carnivoreAnimals, enclosures)
//        );
//
//        // Group carnivore animals of different species in the same enclosure (if necessary)
//        List<Animal> carnivoreAnimals = animals.stream()
//                .filter(animal -> animal.getFood().equals("Carnivore"))
//                .toList();
//
//        groupCarnivoreAnimals(carnivoreAnimals, enclosures);
//    }
//
//    private void assignAnimalsToEnclosures(List<Animal> animals, List<Enclosure> enclosures) {
//        for (Animal animal : animals) {
//            Enclosure enclosure = findSuitableEnclosure(animal, enclosures);
//            if (enclosure != null) {
//                animal.setEnclosureName(enclosure.getName());
//                animalRepository.save(animal);
//            } else {
//                // Handle the case when there are no suitable enclosures available
//                // You can throw an exception, log a warning, or implement custom logic
//            }
//        }
//    }
//
//    private Enclosure findSuitableEnclosure(Animal animal, List<Enclosure> enclosures) {
//        // Find an enclosure that matches the animal's requirements (e.g., size, location)
//        // You can implement your own logic here based on your specific requirements and constraints
//
//        // Example logic: Find the first available enclosure of the required size and location
//        return enclosures.stream()
//                .filter(enclosure -> enclosure.getSize().equals("Large")) // Replace with suitable condition
//                .filter(enclosure -> enclosure.getLocation().equals("Outside")) // Replace with suitable condition
////                .filter(enclosure -> zoo) // Ensure the enclosure is empty
//                .findFirst()
//                .orElse(null);
//    }
//
//    private void groupCarnivoreAnimals(List<Animal> carnivoreAnimals, List<Enclosure> enclosures) {
//        int carnivoreCount = carnivoreAnimals.size();
//        int enclosureCount = enclosures.size();
//
//        if (carnivoreCount > 1 && enclosureCount > 1) {
//            int maxCarnivoresPerEnclosure = 2; // Maximum number of different carnivore species per enclosure
//
//            int numOfEnclosuresNeeded = (int) Math.ceil(carnivoreCount / (double) maxCarnivoresPerEnclosure);
//            if (numOfEnclosuresNeeded <= enclosureCount) {
//                // Distribute carnivore animals across enclosures
//                List<Animal> shuffledCarnivores = new ArrayList<>(carnivoreAnimals);
//                Collections.shuffle(shuffledCarnivores); // Randomize the order of animals
//
//                Iterator<Animal> iterator = shuffledCarnivores.iterator();
//                for (Enclosure enclosure : enclosures) {
//                    for (int i = 0; i < maxCarnivoresPerEnclosure && iterator.hasNext(); i++) {
//                        Animal animal = iterator.next();
//                        animal.setEnclosureName(enclosure.getName());
//                        animalRepository.save(animal);
//                    }
//                }
//            } else {
//                // Handle the case when there are not enough enclosures for the carnivores
//                // You can throw an exception, log a warning, or implement custom logic
//            }
//        }
//    }

}
