package com.chijobs.ChiJobs;

import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.chijobs.model.JobScraper;
import com.chijobs.model.FileManager;
import com.chijobs.model.IndeedScrapper;


@SpringBootApplication
public class ChiJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChiJobsApplication.class, args);

        // runLinkedinScraper();
        runIndeedScraper("Software Engineer", "60666");

        // try {
        //     IndeedScrapper is = new IndeedScrapper();
        //     is.checkHtml("https://www.indeed.com/jobs?q=software+developer&l=60616&radius=5");
        // } catch (Exception e) {
        //     System.out.println(e);
        // }


	
	}

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


    public static void runIndeedScraper(String keywords, String zipCode) {
        IndeedScrapper scraper = new IndeedScrapper();
        FileManager fm = new FileManager();
        try {
            Map<String, Map<String, String>> jobList = scraper.getJobList(keywords, zipCode);
            String filename = fm.generateFilename(keywords, zipCode, null) + ".csv";
            fm.saveToCSV(jobList, filename);
        } catch (Exception e) {

        }
    }

}
