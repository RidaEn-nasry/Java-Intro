package fr._42.reflections.classes;

public class Motorcycle {
    private String brand;
    private String model;
    private int year;
    private int mileage;

    public Motorcycle() {
        this.brand = "Yamaha";
        this.model = "MT-07";
        this.year = 2018;
        this.mileage = 0;
    }

    public Motorcycle(String brand, String model, int year, int mileage) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }

    public void start() {
        // vroom vroom
    }

    public int drive(int distance) {
        this.mileage += distance;
        return this.mileage;
    }

    @Override
    public String toString() {
        return "Motorcycle[" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", mileage=" + mileage +
                ']';
    }
}
