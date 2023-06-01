package com.example.bbc_news_reader;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Map;

public class FavouritesActivity extends BaseActivity {
    private ArrayList<String> headlines;
    private ArrayList<String> links;
    private ArrayList<String> descriptions;
    private ArrayList<String> pubDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        headlines = new ArrayList<>();
        links = new ArrayList<>();
        descriptions = new ArrayList<>();
        pubDates = new ArrayList<>();

        // Retrieve the favourite articles from SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // The article details are retrieved as a concatenated string and split into separate details
            String[] articleDetails = entry.getValue().toString().split("\\|\\|");
            headlines.add(articleDetails[0]);
            descriptions.add(articleDetails[1]);
            links.add(articleDetails[2]);
            pubDates.add(articleDetails[3]);
        }

        if (headlines.isEmpty()) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), "No favourites to display", Snackbar.LENGTH_LONG).show();
        }

        // Binding data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, headlines);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent showDetails = new Intent(getApplicationContext(), NewsDetailsActivity.class);
            showDetails.putExtra("title", headlines.get(position));
            showDetails.putExtra("description", descriptions.get(position));
            showDetails.putExtra("link", links.get(position));
            showDetails.putExtra("pubDate", pubDates.get(position));
            startActivity(showDetails);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_favourites;
    }

    @Override
    protected String getHelpMessage() {
        return "This window is for displaying favourite articles you have saved";
    }
}

