package Task2;

import java.util.Objects;

public class Car {

    private int numberOfTires;
    private String brand;

    public Car(int numberOfTires, String brand) {
        this.numberOfTires = numberOfTires;
        this.brand = brand;
    }

    public int getNumberOfTires() {
        return numberOfTires;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return getNumberOfTires() == car.getNumberOfTires() && getBrand().equals(car.getBrand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfTires(), getBrand());
    }
}