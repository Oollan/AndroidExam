package com.example.oollan.androidexam;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oollan.androidexam.hero.Hero;
import com.example.oollan.androidexam.hero.HeroAdapter;
import com.example.oollan.androidexam.hero.HeroLoader;
import com.example.oollan.androidexam.hero.HeroInterface;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.oollan.androidexam.utils.AnimationUtils.fadeInHeader;
import static com.example.oollan.androidexam.utils.Base64Utils.decodeBase64;
import static com.example.oollan.androidexam.utils.Constants.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<Hero>>, HeroInterface {

    private HeroAdapter adapter = new HeroAdapter();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty_view)
    TextView emptyStateTextView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        adapter.setHeroAdapter(new ArrayList<Hero>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        networkBinder(networkInitializer());
        adapter.setHeroInterface(this);
        bindHeader();
    }

    private void bindHeader() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(NAME_KEY)) {
            collapsingToolbarLayout.setTitle(preferences.getString(NAME_KEY, null));
            headerImage.setImageBitmap(decodeBase64(preferences.getString
                    (IMAGE_KEY, null)));
        } else {
            collapsingToolbarLayout.setTitle(getString(R.string.default_header));
        }
    }

    private NetworkInfo networkInitializer() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
    }

    private void networkBinder(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(HERO_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.VISIBLE);
        networkBinder(networkInitializer());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(HERO_LOADER_ID);
    }

    @Override
    public Loader<List<Hero>> onCreateLoader(int i, Bundle bundle) {
        return new HeroLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Hero>> loader, List<Hero> heroList) {
        adapter.clearData();
        loadingIndicator.setVisibility(View.GONE);
        if (heroList != null && !heroList.isEmpty()) {
            adapter.setHeroAdapter(heroList);
            emptyStateTextView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(R.string.no_heroes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Hero>> loader) {
        adapter.clearData();
    }

    @Override
    public void setHeader(Bitmap image, String title) {
        headerImage.setImageBitmap(image);
        fadeInHeader(headerImage);
        appBarLayout.setExpanded(true);
        collapsingToolbarLayout.setTitle(title);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.scrollTo(0, 0);
    }
}