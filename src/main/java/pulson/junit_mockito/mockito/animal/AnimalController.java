package pulson.junit_mockito.mockito.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private AnimalService animalService;

    @Autowired
    public AnimalController (AnimalService animalService){
        this.animalService = animalService;
    }


    @GetMapping("/greeting")
    public @ResponseBody String greeting() {
        return animalService.getGreeting();
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Animal>> getAllAnimals(){
        return ResponseEntity.ok().body(animalService.getAllAnimals());
    }

    @GetMapping(value = "/oldest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Animal> getOldestAnimal(){
        return ResponseEntity.ok().body(animalService.getOldestAnimal());
    }

    @PostMapping("/add")
    public void addAnimal(@RequestBody Animal animal){
        animalService.addAnimal(animal);
    }
}
