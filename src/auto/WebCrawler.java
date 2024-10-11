package auto;

import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private Set<String> visitedUrls;
    private Queue<String> urlsToVisit;
    private int maxUrlsToVisit;
    private String saveDir;

    public WebCrawler(int maxUrlsToVisit, String saveDir) {
        visitedUrls = new HashSet<>();
        urlsToVisit = new LinkedList<>();
        this.maxUrlsToVisit = maxUrlsToVisit;
        this.saveDir = saveDir;
    }

    public void clear() {
        visitedUrls.clear();
        urlsToVisit.clear();
    }

    public void crawl(String startingUrl) throws IOException {
        String reset = "\u001B[0m";
        String yellow = "\u001B[33m";
        Scanner sc = new Scanner(System.in);

        // Check if the directory containing the crawled pages is already populated
        File pagesDirectory = new File(saveDir);
        if (pagesDirectory.exists() && pagesDirectory.isDirectory() && pagesDirectory.list().length > 0) {
            System.out.println("Website is already crawled!");
            return;
        }

        while (!UrlValidator.validate(startingUrl)) {
            System.out.println(yellow + "Invalid URL: " + startingUrl + reset);
            System.out.println("Please enter a correct url");
            startingUrl = sc.nextLine();
            System.out.println(startingUrl);
        }
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        urlsToVisit.add(startingUrl);
        while (!urlsToVisit.isEmpty() && visitedUrls.size() < maxUrlsToVisit) {
            String url = urlsToVisit.poll();
            if (!visitedUrls.contains(url)) {
                visitedUrls.add(url);
                System.out.println("Visiting the page: " + url);
                try {
                    String links = HTMLParser.parse(url, saveDir);
                    for (String nextUrl : links.split(" ")) {
                        if (!visitedUrls.contains(nextUrl)) {
                            urlsToVisit.add(nextUrl);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error visiting URL: " + url);
                    e.printStackTrace();
                    // Handle the error gracefully, such as logging it and continuing to the next URL.
                }
            }
        }
        System.out.println("Website is crawled!");
    }


    public static void main(String[] args) throws IOException {
        int maxUrlsToVisit = 15;
        String saveDir = "pages";
        int option = 0;
        boolean continueRunning = true;
        CarPriceScrapper priceScrapper = new CarPriceScrapper();
        HashMap<String, Integer> wordFreqMap = new HashMap<>();

        WebCrawler crawler = new WebCrawler(maxUrlsToVisit, saveDir);
        Scanner scanner = new Scanner(System.in);

        // Check if the directory containing the crawled pages is already populated
        File pagesDirectory = new File(saveDir);
//        if (pagesDirectory.exists() && pagesDirectory.isDirectory() && pagesDirectory.list().length > 0) {
//            System.out.println("Website is already crawled!");
//            continueRunning = false;
//        }

        while (continueRunning) {
            System.out.println("\n1: Crawl website");
            System.out.println("0: Terminate\n");

            System.out.println("Enter Value: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            switch (option) {
                case 1:
                    if (!pagesDirectory.exists() || !pagesDirectory.isDirectory() || pagesDirectory.list().length == 0) {
                        String startingUrl;
                        do {
                            System.out.print("Enter a starting URL: ");
                            startingUrl = scanner.nextLine();

                            if (!UrlValidator.validate(startingUrl)) {
                                System.out.println("Invalid URL. Please try again.");
                            }
                        } while (!UrlValidator.validate(startingUrl));
                        crawler.clear();
                        crawler.crawl(startingUrl);
                    } else {
                        System.out.println("Website is already crawled!");
                    }
                    continueRunning = false;
                    break;
                case 2:

                    Scanner priceScanner = new Scanner(System.in);

                    System.out.print("Enter car name: ");
                    String carNamePrice = priceScanner.nextLine();

                    System.out.print("Enter min. price: ");

                    int minPrice = priceScanner.nextInt();
                    System.out.print("Enter max. price: ");
                    int maxPrice = priceScanner.nextInt();
                    System.out.println();

                    // Scrap car price data
                    priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice,"https://www.cars.com/");
                    continueRunning = false;
                    break;
                case 0:
                    System.out.println("Thank you!");
                    continueRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 0 and 1.");
                    break;
            }
        }
    }

}
