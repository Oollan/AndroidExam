package com.example.oollan.androidexam.utils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.oollan.androidexam.hero.Hero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.oollan.androidexam.utils.Base64Utils.decodeBase64;
import static com.example.oollan.androidexam.utils.Constants.*;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<Hero> fetchDataFromSharedPreferences(SharedPreferences preferences) {
        List<Hero> heroList = new ArrayList<>();
        for (int i = 0; i < preferences.getInt(SIZE_KEY, 0); i++) {
            String title = preferences.getString
                    (NAME_KEY + i, null);
            String abilities = preferences.getString
                    (ABILITIES_KEY + i, null);
            Bitmap thumbnail = decodeBase64(preferences.getString
                    (IMAGE_KEY + i, null));
            Hero hero = new Hero(title, abilities, thumbnail);
            heroList.add(hero);
        }
        return heroList;
    }

    public static List<Hero> fetchDataFromServer(String requestUrl)
            throws IOException, JSONException {
        String jsonResponse = makeHttpRequest(requestUrl);
        return extractFeatureFromJson(jsonResponse);
    }

    private static String makeHttpRequest(String src) throws IOException {
        String jsonResponse = "";
        URL url = new URL(src);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Bitmap getBitmapFromURL(String src) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        return BitmapFactory.decodeStream(inputStream);
    }

    private static List<Hero> extractFeatureFromJson(String JSON)
            throws JSONException, IOException {
        List<Hero> heroList = new ArrayList<>();
        JSONArray heroesArray = new JSONArray(JSON);
        for (int i = 0; i < heroesArray.length(); i++) {
            JSONObject results = heroesArray.getJSONObject(i);
            String title = results.optString("title");
            JSONArray abilitiesArray = results.getJSONArray("abilities");
            StringBuilder abilities = new StringBuilder();
            for (int j = 0; j < abilitiesArray.length(); j++) {
                if (j < abilitiesArray.length()-1) {
                    abilities.append(abilitiesArray.optString(j)).append(", ");
                } else {
                    abilities.append(abilitiesArray.optString(j));
                }
            }
            String thumbnailUrl = results.optString("image");
            Bitmap thumbnail = getBitmapFromURL(thumbnailUrl);
            Hero hero = new Hero(title, abilities.toString(), thumbnail);
            heroList.add(hero);
        }
        return heroList;
    }
}