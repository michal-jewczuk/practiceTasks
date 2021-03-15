package Task3;

import java.util.Objects;

/*
Let’s have a class with 2 fields - one Integer and one String. The objects of this class are equal when the String elements are equal, however they are sorted in a different way:
a) the greater the Integer part the lesser is the object and when equal
b) the longer the String part the greater is the object

Test class functionality in three separate ways:
1) using any implementation of a Set test equality of your objects
2) using any implementation of a List and Comparator test sorting of your objects
3) using any implementation of a List and Predicate test filtering of your objects (as a filter can be used an object of this class or any of the fields: Integer or String)
 */
public class Cat {

    private final Integer lives;
    private final String name;

    public Cat(Integer lives, String name) {
        this.lives = lives != null ? lives : Integer.MAX_VALUE;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat that = (Cat) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Integer getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }
}
