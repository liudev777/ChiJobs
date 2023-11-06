package com.chijobs.ChiJobs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chijobs.model.JobScraper;

@SpringBootApplication
public class ChiJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChiJobsApplication.class, args);

		System.out.println("Hello World");
		JobScraper scraper = new JobScraper();
        String keywords = "Software Engineer";
        String locationName = "Chicago";
        String position = "internship";
        try {
            List<Map<String, String>> jobList = scraper.getJobList(keywords, locationName, position);
            String filename = generateFilename(keywords, locationName, position) + ".txt";
            writeToFile(jobList, filename);
        } catch (Exception e) {
            System.out.println("Something went wrong: ");
            e.printStackTrace();
        }

	}

    private static void writeToFile(List<Map<String, String>> jobList, String filename) {
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

	private static String generateFilename(String keywords, String locationName, String position) {
        return sanitizeFilename(keywords + "_" + locationName + "_" + position);
    }

    private static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9\\-_\\.]+", "_");
    }

}
