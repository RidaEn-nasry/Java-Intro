
package fr._42.reflections.classes;

public class Car {

    private String engine;
    private String brand;
    private double price;
    private int year;

    public Car() {
        this.engine = "3.9L twin-turbo V8";
        this.brand = "Ferrari";
        this.price = 250000;
        this.year = 2019;
    }

    public Car(String engine, String brand, double price, int year) {
        this.engine = engine;
        this.brand = brand;
        this.price = price;
        this.year = year;
    }

    public void startEngine() {
        // starting engine
    }

    public void stopEngine() {
        // stopping engine
    }

    public int drive(int distance) {
        return distance;
    }

    @Override
    public String toString() {
        return "Car[" +
                "engine='" + engine + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", year=" + year +
                ']';
    }
}
