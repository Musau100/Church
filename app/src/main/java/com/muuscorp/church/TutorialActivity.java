package com.muuscorp.church;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.pixplicity.easyprefs.library.Prefs;

import static com.muuscorp.church.Config.FIRST_RUN;

public class TutorialActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutorial);
        if (!Prefs.getBoolean(FIRST_RUN, true)) {
//            show tutorial
            startActivity(new Intent(TutorialActivity.this, SplashScreenActivity.class));
            finish();
        }


        //setContentView(R.layout.activity_custom_typeface);
        addSlide(AppIntroFragment.newInstance("Welcome to Jemmin Cleaners.", "JEMMIN CLEANING COMPANY is the best currently, a platform that empowers youth by giving them an opportunity to utilize the many opportunities in our society. ", R.drawable.jemmin_logo, Color.parseColor("#27ae60")));
        addSlide(AppIntroFragment.newInstance("We do professional Cleaning.", "Our Staff are experienced and professionally trained in appropriate cleaning technologies, procedures, health and safety.", R.drawable.logo_cropped, Color.parseColor("#34495e")));
        addSlide(AppIntroFragment.newInstance("Modern Cleaning Detergents and Quality Cleaning Tools.", "We use appropriate cleaning technologies and tools that offer safety to both the cleaners and the clients . ", R.drawable.cleaning_tools, Color.parseColor("#c0392b")));
        addSlide(AppIntroFragment.newInstance("Request Cleaning Service.", "We provide fast, effective and effcient services that go beyond our customers expectations thus satisying their wants.", R.drawable.house_cleaning, Color.parseColor("#27ae60")));
//        setWizardMode(true);
        //show back with done button
        //setBackButtonVisibilityWithDone(false);
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

    private void loadMainActivity() {
        //        Initialize Tutorial Status
        Prefs.putBoolean(FIRST_RUN, false);

        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        finish();
    }
}
