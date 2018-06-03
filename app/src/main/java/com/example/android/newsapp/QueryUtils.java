package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtils {

    //Log tag for the error messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //The request URL. This should be called outside of this class.
    public static final String REQUEST_URL =
            "http://content.guardianapis.com/search?show-fields=thumbnail&show-tags=contributor&q=future&order-by=newest&from-date=2017-01-01&api-key=324ed141-8ecd-4f25-be0a-872bd02c6a8a";

    //Create the a private constructor for the QueryUtils class.
    //It is private because no one should be able to create an instance of this class because
    //it is designed to hold static variables and methods
    private QueryUtils() {
    }

    //Perform a request to The Guardian API, process the response and create a
    //list of news items from the response
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "An error occurred while trying to make the Http request", e);
        }

        // Extract the relevant information from the response
        return extractFeatureFromJson(jsonResponse);
    }

    //Create an URL object from a string of URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //Make the a request to the given URL and return the response into a String object
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Convert the inputStream into a String which then can be read and processed into a jsonObject
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Build up the List of news from the JsonObject.
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONArray associated with the key called "results"
            JSONArray newsArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentNews.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String sectionName = currentNews.getString("sectionName");

                //"Tags" element
                JSONArray tags = currentNews.getJSONArray("tags");

                //First, we initialize the authorName variable to hold an empty string.
                //This way we can combat null pointer exceptions but we have to prepare
                //our adapter that the authorName can either be valid data or an empty string
                String authorName = "";

                //If "tags" array is not null
                if (!tags.isNull(0)) {
                    JSONObject currentTag = tags.getJSONObject(0);

                    //Get the Author's name
                    authorName = currentTag.getString("webTitle");
                }

                // Extract the value for the key called "webPublicationDate"
                String rawPublicationDate = currentNews.getString("webPublicationDate");

                //First, we initialize the publicationDate variable to hold null.
                //This way we can combat null pointer exceptions but we have to prepare
                //our adapter that the authorName can either be valid data or null.
                Date publicationDate = null;

                //Format publication date
                try {
                    publicationDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(rawPublicationDate);
                } catch (Exception e) {
                    // If an error is thrown when executing the above statement in the "try" block,
                    // catch the exception here, so the app doesn't crash. Print a log message
                    // with the message from the exception.
                    Log.e("QueryUtils", "Problem parsing the news date", e);
                }

                // Extract the value for the key called "webUrl"
                String url = currentNews.getString("webUrl");

                // Create a new News object with all the data we have just fetched right now.
                News news = new News(title, sectionName, authorName, publicationDate, url);

                // Add this News object to the list
                newsList.add(news);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return newsList;
    }
}