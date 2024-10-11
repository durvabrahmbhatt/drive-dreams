package auto;

import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PriceComparison {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the URL of the comparison page on cars.com:");
        String url = scanner.nextLine().trim();

        // Fetch website
        Document doc = Jsoup.connect(url).get();

        // Select car data for each car
        Elements cars = doc.select(".vehicle-cards");
        Element car1Data = cars.get(0);
        Element car2Data = cars.get(1);

        // Get car names, MPG, and prices
        String car1Name = car1Data.selectFirst(".vehicle-card-details__title").text().trim();
        String car2Name = car2Data.selectFirst(".vehicle-card-details__title").text().trim();
        double car1MPG = Double.parseDouble(car1Data.selectFirst(".vehicle-card-details__mpg").text().trim().split(" ")[0]);
        double car2MPG = Double.parseDouble(car2Data.selectFirst(".vehicle-card-details__mpg").text().trim().split(" ")[0]);
        double car1Price = Double.parseDouble(car1Data.selectFirst(".vehicle-card-pricing__price").text().trim().substring(1).replaceAll(",", ""));
        double car2Price = Double.parseDouble(car2Data.selectFirst(".vehicle-card-pricing__price").text().trim().substring(1).replaceAll(",", ""));

        // Compare car details
        if (car1MPG > car2MPG) {
            System.out.println(car1Name + " has better MPG than " + car2Name);
        } else if (car1MPG < car2MPG) {
            System.out.println(car2Name + " has better MPG than " + car1Name);
        } else {
            System.out.println(car1Name + " and " + car2Name + " have the same MPG");
        }
        
        if (car1Price < car2Price) {
            System.out.println(car1Name + " is cheaper than " + car2Name);
        } else if (car1Price > car2Price) {
            System.out.println(car2Name + " is cheaper than " + car1Name);
        } else {
            System.out.println(car1Name + " and " + car2Name + " have the same price");
        }
    }
}
