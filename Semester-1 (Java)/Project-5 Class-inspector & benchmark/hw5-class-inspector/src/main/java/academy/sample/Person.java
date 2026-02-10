package academy.sample;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public sealed class Person extends Human implements Named permits Employee {
    @Id
    private Long id;

    private String name;
    private int age;
}
