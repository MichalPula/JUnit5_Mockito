package pulson.junit_mockito.mockito.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class Initializer {

    private final AnimalRepository animalRepository;
    private final ZooRepository zooRepository;

    @Autowired
    public Initializer(AnimalRepository animalRepository, ZooRepository zooRepository) {
        this.animalRepository = animalRepository;
        this.zooRepository = zooRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Zoo krakowZoo = new Zoo("Kraków");
        Zoo warsawZoo = new Zoo("Warszawa");
        zooRepository.save(krakowZoo);
        zooRepository.save(warsawZoo);

        animalRepository.save(new Animal("Cat", 3, warsawZoo));
        animalRepository.save(new Animal("Dog",5, warsawZoo));
        animalRepository.save(new Animal("Lion",10, warsawZoo));
        animalRepository.save(new Animal("Elephant",50, krakowZoo));
        animalRepository.save(new Animal("Tortoise",70, krakowZoo));
        animalRepository.save(new Animal("Greenland shark",200, krakowZoo));
    }
}
