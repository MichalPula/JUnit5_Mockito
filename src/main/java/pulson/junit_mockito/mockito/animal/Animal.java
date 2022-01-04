package pulson.junit_mockito.mockito.animal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "zoo_id", referencedColumnName = "id")
    private Zoo zoo;


    public Animal(String name) {
        this.name = name;
    }

    public Animal(String name, Integer age) {
        this(name);
        this.age = age;
    }

    public Animal(String name, Integer age, Zoo zoo) {
        this(name, age);
        this.zoo = zoo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id) && Objects.equals(name, animal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
