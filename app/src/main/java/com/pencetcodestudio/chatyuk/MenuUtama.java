package com.pencetcodestudio.chatyuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MenuUtama extends AppCompatActivity {
    private static final String APP_ID = "ca-app-pub-5279612574272912~5071754081";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, APP_ID);
        AdView adview = (AdView) findViewById(R.id.adView);
        AdRequest  adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }

    public void about(View view) {
        Intent about = new Intent(getApplicationContext(),About.class);
        startActivity(about);
    }

    public void chat(View view) {
        Intent chat = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(chat);
    }
}
