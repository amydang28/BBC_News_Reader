package com.example.bbc_news_reader;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            displayHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage(getHelpMessage())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String version = "Version: 1.0";
        toolbar.setSubtitle(version);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                    break;
                case R.id.nav_favourites:
                    startActivity(new Intent(BaseActivity.this, FavouritesActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    protected abstract int getLayoutResourceId();

    protected abstract String getHelpMessage();
}
