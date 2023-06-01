package com.example.bbc_news_reader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class NewsDetailsActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button addToFavourites;
    private Button removeFromFavourites;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");
        String pubDate = intent.getStringExtra("pubDate");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        TextView titleTextView = findViewById(R.id.title);
        TextView descriptionTextView = findViewById(R.id.description);
        TextView linkTextView = findViewById(R.id.link);
        TextView pubDateTextView = findViewById(R.id.pubDate);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        linkTextView.setText(link);
        pubDateTextView.setText(pubDate);

        linkTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });

        addToFavourites = findViewById(R.id.add_to_favourites);
        removeFromFavourites = findViewById(R.id.remove_from_favourites);

        addToFavourites.setOnClickListener(v -> {
            String articleDetails = title + "||" + description + "||" + link + "||" + pubDate;
            editor.putString(link, articleDetails);
            editor.apply();
            Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
            updateFavouritesButtonVisibility();
        });

        removeFromFavourites.setOnClickListener(v -> {
            if (sharedPreferences.contains(link)) {
                editor.remove(link);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                updateFavouritesButtonVisibility();
            } else {
                Toast.makeText(getApplicationContext(), "Article not in favourites", Toast.LENGTH_SHORT).show();
            }
        });

        updateFavouritesButtonVisibility();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_news_details;
    }

    private void updateFavouritesButtonVisibility() {
        if (sharedPreferences.contains(link)) {
            addToFavourites.setVisibility(View.GONE);
            removeFromFavourites.setVisibility(View.VISIBLE);
        } else {
            addToFavourites.setVisibility(View.VISIBLE);
            removeFromFavourites.setVisibility(View.GONE);
        }
    }

    @Override
    protected String getHelpMessage() {
        return "This window is for viewing more details about an article such as it's link & publish date. It also gives you the ability to add / remove this article from your favourites";
    }
}
