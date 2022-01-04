package pulson.junit_mockito.mockito.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AnimalService{
    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }


    public String getGreeting() {
        return "Hello, World";
    }


    public String customString;
    public void setCustomString(String customString) {
        this.customString = customString;
    }
    public String getCustomString() {
        return customString;
    }


    public static String staticMethodReturningOK() {
        return "OK";
    }
    public static Integer staticMethodAdd(int a, int b) {
        return a + b;
    }

    public String methodReturningPassedString(String string) {
        return string;
    }


    public List<Animal> getAllAnimals(){
        return animalRepository.findAll();
    }

    public Animal addAnimal(Animal animal){
        return animalRepository.save(animal);
    }

    public Animal getOldestAnimal() {
        List<Animal> animals = animalRepository.findAll();
        return animals.stream().max(Comparator.comparing(Animal::getAge)).get();
    }
}
