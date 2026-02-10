import static org.junit.jupiter.api.Assertions.*;

import academy.models.ClassInfo;
import academy.output.formatters.TextFormatter;
import academy.sample.Employee;
import academy.sample.Manager;
import academy.sample.Person;
import academy.services.InspectorService;
import academy.services.ObjectCreatorService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ServicesTest {
    @Test
    void shouldInspectAndFormatPersonExactlyAsInExample() {
        // arrange
        InspectorService inspector = new InspectorService();
        TextFormatter formatter = new TextFormatter();

        // act
        ClassInfo info = inspector.inspect(Person.class);
        String result = formatter.format(info);

        // assert
        String expected =
                """
            Class: Person
            Superclass: Human
            Interfaces:
              - Named
            Fields:
              - private id (Long)
              - private name (String)
              - private age (int)
            Methods:
              - public getAge() : int
              - public getId() : Long
              - public getName() : String
              - public setAge(int) : void
              - public setId(Long) : void
              - public setName(String) : void
            Annotations:
              - @Entity
            Hierarchy:
              Human
                └── Person
                  └── Employee
                    └── Manager
               \s""";

        assertEquals(expected.trim(), result.trim());
    }

    private final ObjectCreatorService creator = new ObjectCreatorService();

    @Test
    void shouldCreatePrimitive() {
        int value = creator.create(int.class);
        assertTrue(value >= 0);
    }

    @Test
    void shouldCreateString() {
        String value = creator.create(String.class);
        assertNotNull(value);
        assertFalse(value.isEmpty());
    }

    enum Color {
        RED,
        GREEN,
        BLUE
    }

    @Test
    void shouldCreateEnum() {
        Color color = creator.create(Color.class);
        assertNotNull(color);
    }

    @Test
    void shouldCreateArray() {
        String[] array = creator.create(String[].class);
        assertNotNull(array);
        assertTrue(array.length > 0);
        assertNotNull(array[0]);
    }

    @Test
    void shouldCreateCollection() {
        List<String> list = creator.create(List.class);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void shouldCreateMap() {
        Map<Object, Object> map = creator.create(Map.class);
        assertNotNull(map);
        assertFalse(map.isEmpty());
    }

    @Test
    void shouldCreateDate() {
        Date date = creator.create(Date.class);
        assertNotNull(date);
    }

    @Test
    void shouldCreateLocalDate() {
        LocalDate date = creator.create(LocalDate.class);
        assertNotNull(date);
    }

    @Test
    void shouldCreateLocalDateTime() {
        LocalDateTime dateTime = creator.create(LocalDateTime.class);
        assertNotNull(dateTime);
    }

    static class Node {
        Node next;
    }

    static class A {
        B b;
    }

    static class B {
        A a;
    }

    @Test
    void shouldPreventCyclicCreation() {
        A a = creator.create(A.class);

        assertNotNull(a);
        assertNotNull(a.b);
        assertSame(a, a.b.a); // цикл разорван через cache
    }

    @Test
    void shouldCreatePerson() {
        Person person = creator.create(Person.class);

        assertNotNull(person);
        assertNotNull(person.getName());
        assertTrue(person.getAge() >= 0);
    }

    @Test
    void shouldCreateEmployee() {
        Employee employee = creator.create(Employee.class);

        assertNotNull(employee);
        assertNotNull(employee.getName());
        assertTrue(employee.getAge() >= 0);
    }

    @Test
    void shouldCreateManager() {
        Manager manager = creator.create(Manager.class);

        assertNotNull(manager);
        assertNotNull(manager.getName());
        assertTrue(manager.getAge() >= 0);
    }

    @Test
    void shouldCreateFullHierarchyObject() {
        Manager manager = creator.create(Manager.class);

        assertNotNull(manager);
        assertNotNull(manager.getName());
        assertTrue(manager.getAge() >= 0);

        // Проверяем, что поля Employee/Person тоже заполнены
        assertNotNull(manager.getName());
    }
}
