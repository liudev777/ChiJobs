package com.chijobs.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobScraper {

    private final OkHttpClient client = new OkHttpClient();

    public List<Map<String, String>> getJobList(String keywords, String locationName, String position) throws IOException {
        String encodedKeywords = URLEncoder.encode(keywords, StandardCharsets.UTF_8.toString());
        String f_PP = fetch_f_PP(locationName);
        String f_E = fetch_f_E(position);
        String url = String.format(
                "https://www.linkedin.com/jobs-guest/jobs/api/seeMoreJobPostings/search?keywords=%s&location=United%%2BStates&locationId=&geoId=103644278&f_TPR=&f_PP=%s&f_E=%s&start=0",
                encodedKeywords, f_PP, f_E);
        List<Map<String, String>> jobList = new ArrayList<>();
        extendJobs(url, jobList, f_PP);
        System.out.println("JobScraper.java: getJobList: " + url);
        return jobList;
    }

    private String fetch_f_PP(String locationName) throws IOException {
        if (locationName == null) return null;
        String url = String.format("https://www.linkedin.com/jobs-guest/api/typeaheadHits?origin=jserp&typeaheadType=GEO&geoTypes=POPULATED_PLACE&query=%s", locationName);
        Request request = new Request.Builder().url(url).build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            
            String responseBody = response.body().string();
            JsonElement jsonElement = JsonParser.parseString(responseBody);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                if (jsonArray.size() > 0) {
                    JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
                    return firstObject.get("id").getAsString();
                }
            }
        }
        
        return null;  // Return null if the ID could not be extracted
    }

    private String fetch_f_E(String position) {
        if (position == null) return null;
        Map<String, String> positions = new HashMap<>();
        positions.put("internship", "1");
        positions.put("junior", "2,3,4");
        positions.put("senior", "5");
        return positions.get(position);
    }

    private void extendJobs(String url, List<Map<String, String>> jobList, String f_PP) throws IOException {
        for (int n = 0; n <= 1; n += 25) {
            String currentUrl = url + n;
            Request request = new Request.Builder().url(currentUrl).build();
            try (Response response = client.newCall(request).execute()) {
                String html = response.body().string();
                jobList.addAll(parseHtml(html));
            } catch (Exception e) {
                System.err.println("Something went wrong: " + e.getMessage());
            }
        }
    }

    private List<Map<String, String>> parseHtml(String html) {
        Document document = Jsoup.parse(html);
        List<Map<String, String>> jobs = new ArrayList<>();
        Elements cards = document.select("div.base-search-card");
        for (Element card : cards) {
            Map<String, String> job = new HashMap<>();
            Element jobLinkTag = card.selectFirst("a.base-card__full-link");
            job.put("job_link", jobLinkTag != null ? jobLinkTag.attr("href") : null);
            Element jobTitleTag = card.selectFirst("h3.base-search-card__title");
            job.put("job_title", jobTitleTag != null ? jobTitleTag.text() : null);
            Element aTag = card.selectFirst("a.hidden-nested-link");
            job.put("company_name", aTag != null ? aTag.text() : null);
            Element jobImageTag = card.selectFirst("img.artdeco-entity-image");
            job.put("job_image_url", jobImageTag != null ? jobImageTag.attr("data-delayed-url") : null);
            jobs.add(job);
        }
        return jobs;
    }
}
