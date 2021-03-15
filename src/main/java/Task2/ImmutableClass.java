package Task2;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
Design an immutable class that will have at least 3 fields:
a)	A String field
b)	A field with a Collection – no matter what, can be generic List or Set or a specific one
c)	A filed with a reference to another class that has at least one field 
 */
public final class ImmutableClass {

    private final String companyName;
    private final List<Car> vendors;
    private final Car car;

    public ImmutableClass(String companyName, List<Car> vendors, Car car) {
        this.companyName = companyName;
        this.car = new Car(car.getNumberOfTires(), car.getBrand());
        this.vendors = vendors.stream().map(c -> new Car(c.getNumberOfTires(), c.getBrand())).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableClass that = (ImmutableClass) o;
        return companyName.equals(that.companyName) && vendors.equals(that.vendors) && car.equals(that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, vendors, car);
    }

}