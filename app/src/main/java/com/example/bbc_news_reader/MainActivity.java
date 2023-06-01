package com.example.bbc_news_reader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.BuildConfig;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ArrayList<String> headlines;
    private ArrayList<String> links;
    private ArrayList<String> descriptions;
    private ArrayList<String> pubDates;
    private ListView listView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        headlines = new ArrayList<>();
        links = new ArrayList<>();
        descriptions = new ArrayList<>();
        pubDates = new ArrayList<>();

        new FetchRSSFeed().execute();

        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetails = new Intent(getApplicationContext(), NewsDetailsActivity.class);
                showDetails.putExtra("title", headlines.get(position));
                showDetails.putExtra("description", descriptions.get(position));
                showDetails.putExtra("link", links.get(position));
                showDetails.putExtra("pubDate", pubDates.get(position));
                startActivity(showDetails);
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private class FetchRSSFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem)
                                headlines.add(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                links.add(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem)
                                descriptions.add(xpp.nextText());
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem)
                                pubDates.add(xpp.nextText());
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }

                    eventType = xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, headlines);
            listView.setAdapter(adapter);
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getHelpMessage() {
        return "This window is for browsing the most recent articles";
    }

}
