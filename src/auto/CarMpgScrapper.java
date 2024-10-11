package auto;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CarMpgScrapper {

	public void scrapCarMpgData(String carName, long mileage, String startingUrl) {
		String reset="\u001B[0m";
		String yellow= "\u001B[33m";
		try {
			List<Car> cars = new ArrayList<>();
			if(startingUrl.startsWith("https://www.kbb.com")) {
				String url = "https://www.kbb.com/car-finder/?" + (carName != null && !carName.isEmpty() ?
						"manufacturers=" + carName.replace(" ", "%20") + "&" : "") +
						"mpg=over" + mileage;

				// Connect to the website and get its HTML document
				Document doc = Jsoup.connect(url).get();

				// Select car elements from the HTML document
				Elements carElements = doc.select("div.css-ai263y.e1qqueke1 > div.ewtqiv33.css-jwnqcy.e11el9oi0");
// Select car elements from the HTML document from the second div
				Elements carElementsSecondDiv = doc.select("div.css-1b1a19r.e1qqueke0 > div.css-fyuinx.e1qqueke1 > div.ewtqiv33.css-jwnqcy.e11el9oi0");
				carElements.addAll(carElementsSecondDiv);
				// List to store scraped car objects


				// Loop through each car element and extract relevant information
				for (Element carElement : carElements) {

					// Extract car name
					String name = carElement.select("a.css-z66djy.ewtqiv30").text();

					String year = carElement.select("a.css-z66djy.ewtqiv30").text().split(" ")[0];
					String price = carElement.select("div.css-15j21fj.e19qstch15 > div.css-n59ln1.e181er9y2> div.css-1d3w5wq.e181er9y1 > div.css-15ums5i.e181er9y0 > div.css-fpbjth.e151py7u1").text();


					String mpg = carElement.select("div.css-14q4cew.e19qstch18 > div.css-n59ln1.e181er9y2  > div.css-1d3w5wq.e181er9y1 > div.css-15ums5i.e181er9y0 > div.css-fpbjth.e151py7u1").text();

					String carUrl = "https://www.kbb.com"+carElement.select("a.css-z66djy.ewtqiv30").attr("href");
					cars.add(new Car(name, year, price, mpg, carUrl)); // Add car to the list


				}
			}else if(startingUrl.startsWith("https://www.cars.com")) {
				// URL for the car search

				String url = "https://www.cars.com/shopping/results/?" + (carName != null && !carName.isEmpty() ?
						"makes=" + carName.replace(" ", "%20") + "&" : "") +
						"mileage_min=" + mileage ;

				// Connect to the website and get its HTML document
				Document doc = Jsoup.connect(url).get();

				// Select car elements from the HTML document
				Elements carElements = doc.select("div.sds-page-section__content > #vehicle-cards-container > div.vehicle-card.ep-theme-hubcap");
// Select car elements from the HTML document from the second div




				// Loop through each car element and extract relevant information
				for (Element carElement : carElements) {

					// Extract car name
					String name = carElement.select("a.vehicle-card-link > h2").text();

					String year = carElement.select("a.vehicle-card-link > h2").text().split(" ")[0];
					String price = carElement.select("div.vehicle-details > div.price-section.price-section-vehicle-card > span.primary-price").text();


					String mpg = carElement.select("div.mileage").text();
					String carUrl = "https://www.cars.com"+carElement.select("a.vehicle-card-link").attr("href");

					cars.add(new CarMpgScrapper.Car(name, year, price, mpg, carUrl)); // Add car to the list


				}
			}

			if (cars.isEmpty()) {
				System.out.println(yellow + "No cars found with the specified mileage." + reset);
				return; // Exit the method
			}
			// Sort the list of cars by price in ascending order (as string)
			cars.sort((car1, car2) -> car2.getMpg().compareTo(car1.getMpg()));

			// Write the data to a CSV file
			writeDataToCSV(cars);

			// Print the list of cars
			for (Car car : cars) {
				System.out.println( car.toString());
			}
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				System.out.println(yellow + "Error: The car model was not found." + reset);
			} else {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Method to check if the word corresponds to a model name

	private void writeDataToCSV(List<CarMpgScrapper.Car> cars) {
		String csvFile = "car_data.csv";
		try (FileWriter writer = new FileWriter(csvFile,true)) {
			// Get current date and time
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = now.format(formatter);

			// Write date and time as the first row
			writer.append("\n");
			writer.append("Date : " + formattedDateTime + "\n");
			// Write CSV header
			writer.append("Name,Year,Price,MPG,URL\n");

			// Write car data
			for (CarMpgScrapper.Car car : cars) {
				writer.append(String.join(",", car.getName(), car.getYear(), car.getPrice(), car.getMpg(), car.getCarUrl()));
				writer.append("\n");
			}
			System.out.println("CSV file has been created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class Car {
		String name;
		String year;
		String price;
		String mpg;
		String carUrl;
		public Car(String name, String year, String price, String mpg, String carUrl) {
			this.name = name;
			this.year = year;
			this.price = price;
			this.mpg = mpg;
			this.carUrl=carUrl;
		}

		public String getPrice() {
			return price.replace(",", "");
		}


		public String getName() {
			return name;
		}

		public String getYear() {
			return year;
		}

		public String getMpg() {
			return mpg.replace(",", "");
		}

		public String getCarUrl() {
			return carUrl;
		}


		@Override
		public String toString() {
			return "Name of the Model: " + name + " Year: " + year + ", Price: " + price + ", MPG: " + mpg + ", Car Url: "+carUrl;
		}
	}
}
