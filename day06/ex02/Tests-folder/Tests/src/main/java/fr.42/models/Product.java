
package fr._42.numbers.models;

public class Product {

    private long id;
    private String name;
    private double price;

    public Product() {

    }

    public Product(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        Product p = (Product) o;
        return (this.id == p.getId() && this.name.equals(p.getName()) && this.price == p.getPrice());
    }

    @Override
    public int hashCode() {
        return (int) (this.id + this.name.hashCode() + this.price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", price=" + this.price +
                '}';
    }

}