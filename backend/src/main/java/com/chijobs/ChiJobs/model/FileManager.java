package com.chijobs.ChiJobs.model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FileManager {

    /*
     * Helper functions to write output to files
     */

    public void writeToFile(List<Map<String, String>> jobList, String filename) {
        String outputDirectory = "output/";
        String fullPath = outputDirectory + filename;
    
        // Create the directory if it doesn't exist
        new File(outputDirectory).mkdirs();
        
        try (FileWriter writer = new FileWriter(fullPath)) {
            for (Map<String, String> job : jobList) {
                writer.write(job.toString() + System.lineSeparator());
            }
            System.out.println("Output has been written to " + fullPath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: ");
            e.printStackTrace();
        }
    }
    
    public String generateFilename(String keywords, String locationName, String position) {
        return sanitizeFilename(keywords + "_" + locationName + "_" + position);
    }

    private static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9\\-_\\.]+", "_");
    }

    // Uses selenium to get dynamic webpage and save html file
    public void saveHtmlToFile(String url, String filePath) throws IOException{
        // Set the path to the chromedriver executable
        Dotenv dotenv = Dotenv.load();
        String chromeDriverPath = dotenv.get("WEBDRIVER_CHROME_DRIVER");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        // Configure Chrome to run in headless mode
        ChromeOptions options = new ChromeOptions();
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

            // Save the page source to a file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(pageSource);
            }

            System.out.println("Page source saved to " + filePath);
        } finally {
            // Close the browser
            driver.quit();
        }
    }
    

    public void saveToCSV(Map<String, Map<String, String>> joblist, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/" + filename + ".csv"))) {
            // Writing header
            writer.write("Link, jobId, Title, Company, Location, Description\n");

            // Writing each job to the CSV
            for (Map.Entry<String, Map<String, String>> entry : joblist.entrySet()) {
                String href = entry.getKey();
                Map<String, String> details = entry.getValue();
                String job_id = details.getOrDefault("jobId", "");
                String title = details.getOrDefault("title", "");
                String company = details.getOrDefault("company", "");
                String location = details.getOrDefault("location", "");
                String description = details.getOrDefault("description", "").replace("\n", " ").replace(",", ";");

                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", href, job_id, title, company, location, description));
            }
        }
        System.out.println("CSV file created successfully: " + filename);
    }

    public String convertToJson(Map<String, Map<String, String>> joblist) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(joblist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


}
