package com.chijobs.ChiJobs;

import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.chijobs.model.JobScraper;
import com.chijobs.model.FileManager;
import com.chijobs.model.IndeedScrapper;
import com.chijobs.database.MySQLDatabase;


@SpringBootApplication
public class ChiJobsApplication {


    /*
     * NOTES: database searching only for non-zipcode local searches. 
     * Zipcode local searches requires realtime everytime.
     */

     /*
      * TODO: 
      check db for query first, if it exists and time is not past 24 hours
      return the result from db, else, make a scrape.
      */
	public static void main(String[] args) {
		SpringApplication.run(ChiJobsApplication.class, args);

        System.out.println("Running ChiJobsApplication!");
        runIndeedScraper("Concept Artist", "60616");
	
	}


    // this will be the main scraper ChiJob app will use
    // run scraper and save output to /output
    public static void runIndeedScraper(String keywords, String zipCode) {
        IndeedScrapper scraper = new IndeedScrapper();
        FileManager fm = new FileManager();

        try {
            Map<String, Map<String, String>> jobList = scraper.getJobList(keywords, zipCode);
            
            MySQLDatabase.handleJobSQL(keywords, jobList);
            String filename = fm.generateFilename(keywords, zipCode, null) + ".csv";
            
            fm.saveToCSV(jobList, filename); // uncomment to save to /output
        } catch (Exception e) {
            System.out.println("Something went wrong when running runIndeedScraper: ");
            System.out.println(e);
        }
    }


    // linkedin scraping
    public static void runLinkedinScraper() {
        JobScraper scraper = new JobScraper();
        FileManager fm = new FileManager();
        String keywords = "Software Engineer";
        String locationName = "Chicago";
        String position = "Internship";
        try {
            List<Map<String, String>> jobList = scraper.getJobList(keywords, locationName, position);
            String filename = fm.generateFilename(keywords, locationName, position) + ".txt";
            fm.writeToFile(jobList, filename);
        } catch (Exception e) {
            System.out.println("Something went wrong: ");
            e.printStackTrace();
        }
    }
    

}
