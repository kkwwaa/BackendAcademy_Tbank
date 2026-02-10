package academy.dto;

import static org.junit.jupiter.api.Assertions.*;

import academy.maze.dto.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Point")
class PointTest {

    @Test
    @DisplayName("should parse valid x,y string")
    void shouldParse_WhenValidFormat() {
        // Act
        Point point = Point.parse("3,5");

        // Assert
        assertEquals(3, point.x());
        assertEquals(5, point.y());
    }

    @Test
    @DisplayName("should throw when format is invalid")
    void shouldThrow_WhenInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> Point.parse("3"));
        assertThrows(IllegalArgumentException.class, () -> Point.parse("3,"));
        assertThrows(IllegalArgumentException.class, () -> Point.parse("a,5"));
        assertThrows(IllegalArgumentException.class, () -> Point.parse("3,5,1"));
    }

    @Test
    @DisplayName("should be equal when coordinates match")
    void shouldBeEqual_WhenSameCoordinates() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(3, 4);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
