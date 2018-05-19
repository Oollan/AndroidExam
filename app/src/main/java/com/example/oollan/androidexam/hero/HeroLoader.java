package com.example.oollan.androidexam.hero;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.example.oollan.androidexam.utils.Constants.NAME_KEY;
import static com.example.oollan.androidexam.utils.QueryUtils.*;

public class HeroLoader extends AsyncTaskLoader<List<Hero>> {

    private String url;

    public HeroLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Hero> loadInBackground() {
        List<Hero> heroList = null;
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).contains(NAME_KEY + 0)) {
            heroList = fetchDataFromSharedPreferences
                    (PreferenceManager.getDefaultSharedPreferences(getContext()));
        } else {
            try {
                heroList = fetchDataFromServer(url);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return heroList;
    }
}