package pulson.junit_mockito.mockito.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

   default void someVoidMethod(String str) { int a = 5; };
   default String getGOATString() {
      return "Ye";
   }
}
