package com.chijobs.ChiJobs.model;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class Recommender {

    private static Dotenv dotenv = Dotenv.load();

    private static OkHttpClient httpClient;
    private static final String apiKey = dotenv.get("OPENAI_TOKEN");

    public static List<String> sendChatRequest(String referenceJobs, String poolOfJobs) throws Exception {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Set the connect timeout
                .writeTimeout(30, TimeUnit.SECONDS)   // Set the write timeout
                .readTimeout(30, TimeUnit.SECONDS)    // Set the read timeout
                .callTimeout(60, TimeUnit.SECONDS)    // Set the overall call timeout
                .build();

        System.out.println(referenceJobs);
        System.out.println(poolOfJobs);
        // Create JSON payload
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("model", "gpt-4");

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "You are a job recommender, expert in taking a list of jobs and using it to pick 5 jobs from a pool of job options."));
        messages.put(new JSONObject().put("role", "user").put("content", "Do not provide any extra commentary. Return a list of 5 (or less if there isn't anything else that is highly recommended) job ID from" + poolOfJobs + "corresponding to the job based on recommendations using the following list: " + referenceJobs + "separated with nothing but space and comma in the format: id1, id2, id3, id4 ... Do not return the job titles, only the corresponding ID"));
        
        jsonPayload.put("messages", messages);

        // Create request body
        RequestBody body = RequestBody.create(jsonPayload.toString(), MediaType.get("application/json"));

        // Build request
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        // Execute request and handle response
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return parseResponse(response.body().string());
        }
    }

    public static List<String> parseResponse(String jsonResponse) {
        // Parse the JSON response
        JSONObject responseObject = new JSONObject(jsonResponse);

        // Navigate to the 'choices' array and then to the 'content' of the first choice
        JSONArray choicesArray = responseObject.getJSONArray("choices");
        String content = choicesArray.getJSONObject(0).getJSONObject("message").getString("content");

        // Split the content into individual job titles and create a list
        List<String> jobTitles = new ArrayList<>(Arrays.asList(content.split(", ")));

        return jobTitles;
    }

}
