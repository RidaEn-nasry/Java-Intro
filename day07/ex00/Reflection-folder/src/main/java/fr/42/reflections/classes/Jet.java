
package fr._42.reflections.classes;

public class Jet {
   private String name;
   private String engine;
   private double speed;
   private int year;

   public Jet() {
      this.name = "F-22 Raptor";
      this.engine = "2x Pratt & Whitney F119-PW-100 turbofans";
      this.speed = 2410;
      this.year = 1997;
   }

   public Jet(String name, String engine, double speed, int year) {
      this.name = name;
      this.engine = engine;
      this.speed = speed;
      this.year = year;
   }

   public void fly() {
      // flying
   }

   public void land() {
      // landing
   }

   @Override
   public String toString() {
      return "Jet[" +
            "name='" + name + '\'' +
            ", engine='" + engine + '\'' +
            ", speed=" + speed +
            ", year=" + year +
            ']';
   }
}