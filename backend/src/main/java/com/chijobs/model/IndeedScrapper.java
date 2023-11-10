package com.chijobs.model;

import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Proxy;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class IndeedScrapper {

    // main function to get list of jobs
    public Map<String, Map<String, String>> getJobList(String keywords, String zipCode) throws IOException {
        String query = URLEncoder.encode(keywords, "UTF-8");
        String url = String.format("https://www.indeed.com/jobs?q=%s&l=%s&radius=5", query, zipCode);
        
        String html = getHtmlFromUrl(url); // uses selenium to convert page into html file
        return scrapeJobDetails(html);
    }


    // saves html output of a given url to a html file
    public void checkHtml(String url) throws IOException{
        FileManager fm = new FileManager();
        fm.saveHtmlToFile(url, "output/htmlout.html");
    }


    // given html, parses the html page for tags and return job desc
    public Map<String, Map<String, String>> scrapeJobDetails(String html) {
        Document document = Jsoup.parse(html);

        // every job listing is separated by this tag:
        Elements jobItems = document.select("li.css-5lfssm.eu4oa1w0");
        Map<String, Map<String, String>> jobs = new HashMap<>();

        // tag parsing logic
        for (Element jobItem : jobItems) {
            Element linkElement = jobItem.select("h2.jobTitle > a").first();
            if (linkElement != null) {

                String jobId = linkElement.attr("data-jk");
                
                Map<String, String> jobDetails = new HashMap<>();

                if (jobId != null) {
                    jobDetails.put("jobId", jobId);
                }

                jobDetails.put("title", linkElement.text()); 

                Element companyElement = jobItem.select("[data-testid=company-name]").first();
                if (companyElement != null) {
                    jobDetails.put("company", companyElement.text());
                }

                Element locationElement = jobItem.select("[data-testid=text-location]").first();
                if (locationElement != null) {
                    jobDetails.put("location", locationElement.text());
                }

                Element descriptionElement = jobItem.select("div.job-snippet > ul").first();
                if (descriptionElement != null) {
                    jobDetails.put("description", descriptionElement.text());
                }

                jobs.put(String.format("https://www.indeed.com/viewjob?jk=%s", jobId), jobDetails);
            }
        }
        return jobs;
    }
    

    // takes in indeed url and returns webpage html content
    public String getHtmlFromUrl(String url) throws IOException{
        // Set the path to the chromedriver executable
        Dotenv dotenv = Dotenv.load();
        String chromeDriverPath = dotenv.get("WEBDRIVER_CHROME_DRIVER");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        String proxyHost = dotenv.get("PROXY_HOST");
        String proxyPort = dotenv.get("PROXY_PORT");
        
        // use proxie if defined in .env
        ChromeOptions options = new ChromeOptions();
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyHost + ":" + proxyPort);
            proxy.setSslProxy(proxyHost + ":" + proxyPort);
            options.setCapability("proxy", proxy);
        }

        // Configure Chrome to run in headless mode
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

        // Initialize the WebDriver with Chrome options
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the URL
            driver.get(url);

            // Get the page source
            String pageSource = driver.getPageSource();

            return (pageSource);
        } catch (Exception e) {
            System.out.println(String.format("Selenium Error: %s", e));
            return null;
        } finally {
            // Close the browser
            // driver.quit();
        }
    }


}