package auto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class CarDealerCrawler {

	public static void main(String[] args) {
		List<Car> cars = new ArrayList<>();
		cars.addAll(getCarsFromMazda());
		cars.addAll(getCarsFromFord());
		cars.addAll(getCarsFromChrysler());

		// Define search parameters
		String make = "Ford";
		String model = "Mustang";

		// Find the best deal
		Car bestDeal = null;
		for (Car car : cars) {
			if (car.getMake().equals(make) && car.getModel().equals(model)) {
				if (bestDeal == null || (car.getPrice() < bestDeal.getPrice())) {
					bestDeal = car;
				}
			}
		}

		// Display the best deal to the user
		if (bestDeal != null) {
			System.out.println("Best deal for " + make + " " + model + ": " + bestDeal.getPrice());
		} else {
			System.out.println("No matching deals found.");
		}
	}

	private static List<Car> getCarsFromMazda() {
	    List<Car> cars = new ArrayList<>();
	    try {
	        Document doc = Jsoup.connect("https://www.mazda.ca/en/inventory/new-vehicles/").get();
	        Elements carElements = doc.select(".inv-item");
	        for (Element carElement : carElements) {
	            String make = "Mazda";
	            String model = carElement.select(".model-name").text();
	            double price = Double.parseDouble(carElement.select(".price").text().replaceAll("[^\\d.]+", ""));
	            String fuelEfficiency = carElement.select(".mpg hwy").text();
	            int seatingCapacity = Integer.parseInt(carElement.select(".passenger-capacity").text());

	            Car car = new Car(make, model, price, fuelEfficiency, seatingCapacity);
	            cars.add(car);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cars;
	}

	private static List<Car> getCarsFromFord() {
	    List<Car> cars = new ArrayList<>();
	    try {
	        Document doc = Jsoup.connect("https://www.ford.ca/cars/").get();
	        Elements carElements = doc.select(".vehicle-listing-item");
	        for (Element carElement : carElements) {
	            String make = "Ford";
	            String model = carElement.select(".vehicle-name").text();
	            double price = Double.parseDouble(carElement.select(".vehicle-price").text().replace(",", "").substring(1));
	            String fuelEfficiency = carElement.select(".vehicle-mpg").text();
	            int seatingCapacity = Integer.parseInt(carElement.select(".vehicle-seating").text());

	            Car car = new Car(make, model, price, fuelEfficiency, seatingCapacity);
	            cars.add(car);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cars;
	}

	private static List<Car> getCarsFromChrysler() {
	    List<Car> cars = new ArrayList<>();
	    try {
	        Document doc = Jsoup.connect("https://www.chrysler.ca/en/carline").get();
	        Elements carElements = doc.select(".vehicle-tile");
	        for (Element carElement : carElements) {
	            String make = "Chrysler";
	            String model = carElement.select(".vehicle-name").text();
	            double price = Double.parseDouble(carElement.select(".vehicle-price").text().replace(",", "").substring(1));
	            String fuelEfficiency = carElement.select(".vehicle-mpg").text();
	            int seatingCapacity = Integer.parseInt(carElement.select(".vehicle-seating").text());

	            Car car = new Car(make, model, price, fuelEfficiency, seatingCapacity);
	            cars.add(car);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cars;
	}
}

class Car {
    private String make;
    private String model;
    private double price;
    private String fuelEfficiency;
    private int seatingCapacity;

    public Car(String make, String model, double price, String fuelEfficiency, int seatingCapacity) {
        this.make = make;
        this.model = model;
        this.price = price;
        this.fuelEfficiency = fuelEfficiency;
        this.seatingCapacity = seatingCapacity;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public String getFuelEfficiency() {
        return fuelEfficiency;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", fuelEfficiency='" + fuelEfficiency + '\'' +
                ", seatingCapacity=" + seatingCapacity +
                '}';
    }
}

