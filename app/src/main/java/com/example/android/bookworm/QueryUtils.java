package com.example.android.bookworm;

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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    private QueryUtils() {

    }

    /**
     * Return a list of {@link Books} objects that has been built up from
     * parsing a JSON response.
     */

    public static List<Books> fetchBookData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: fetchBookData() called");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Books> books = extractFeaturesFromJSON(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    public static List<Books> extractFeaturesFromJSON(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Books> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject jsonObject = new JSONObject(bookJSON);
            JSONArray bookList = jsonObject.getJSONArray("items");

            for (int i = 0; i < bookList.length(); i++) {
                JSONObject currentBook = bookList.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                JSONObject JSONPrice = currentBook.getJSONObject("saleInfo");
                if (JSONPrice.has("retailPrice")) {
                    String title = volumeInfo.getString("title");
                    String publications;
                    String authors = "";
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++){
                        if (authorsArray.length() == 1){
                            authors = authorsArray.getString(j);
                            break;
                        }else {
                            authors = authors + ", " + authorsArray.getString(j);
                        }
                    }
                    if (volumeInfo.has("publisher")) {
                        publications = volumeInfo.getString("publisher");
                    } else {
                        publications = "Not Available";
                    }
                    double ratings;
                    if (volumeInfo.has("averageRating")) {
                        ratings = volumeInfo.getDouble("averageRating");
                    } else {
                        ratings = 0.0;
                    }
                    JSONObject RetailsPrice = JSONPrice.getJSONObject("retailPrice");
                    double price = RetailsPrice.getDouble("amount");

                    String url = JSONPrice.getString("buyLink");
                    books.add(new Books(title,authors, publications, ratings, price, url));

                } else {
                    continue;
                }

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.

            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        // Return the list of bookquakes
        return books;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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
}
