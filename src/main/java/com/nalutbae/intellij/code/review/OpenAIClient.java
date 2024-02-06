package com.nalutbae.intellij.code.review;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OpenAIClient {

    private static final String OPENAI_API_KEY = "Your OpenAI API Key";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    public static String request(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        JSONObject data = new JSONObject()
                .put("model", "gpt-3.5-turbo-instruct")
                .put("prompt", prompt)
                .put("temperature", 0.5)
                .put("max_tokens", 1024)
                .put("n", 1);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseString = response.body().string();
            JSONObject responseObject = new JSONObject(responseString);
            JSONArray choicesArray = responseObject.getJSONArray("choices");
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            return firstChoice.getString("text").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Sorry";
    }

}
