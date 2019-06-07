package com.example.squad.atg;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InternetAvailabilityChecker.init(this);

        InternetAvailabilityChecker mInternet = InternetAvailabilityChecker.getInstance();
        mInternet.addInternetConnectivityListener(this);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = PhotosPage.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = Search.newInstance();
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        if (selectedFragment != null) {
                            transaction.replace(R.id.frame_layout, selectedFragment);
                        }
                        transaction.commit();
                        return true;
                    }
                });

        isNetworkConnected();
    }

    private void isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new PhotosPage());
            transaction.commit();
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).hide();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new noInternet());
            transaction.commit();
            Snackbar.make(bottomNavigationView, "No Internet Connectivity", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isNetworkConnected();
                }
            }).show();
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            bottomNavigationView.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).hide();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new noInternet());
            transaction.commit();
            Snackbar.make(bottomNavigationView, "No Internet Connectivity", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isNetworkConnected();
                }
            }).show();
        }
    }
}
