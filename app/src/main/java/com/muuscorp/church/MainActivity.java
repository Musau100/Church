package com.muuscorp.church;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kennyc.bottomsheet.BottomSheet;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.muuscorp.church.Config.SLIDER_IMAGE_URL;
import static com.muuscorp.church.Config.SLIDER_URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    ArrayList<ServiceCategory> categoriesData = new ArrayList<>();
    ArrayList<Slider> sliderImages = new ArrayList<>();
    InfiniteViewPager mViewPager;
    LinePageIndicator mLineIndicator;
    private SwipeRefreshLayout mainSwipeRefreshLayout;
    private CoordinatorLayout mainCoordinatorLayout;
    private RecyclerView categoriesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        assignViews();

//        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        categoriesRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        categoriesRecyclerView.addOnItemTouchListener(new CategoriesAdapter.RecyclerTouchListener(getBaseContext(), categoriesRecyclerView, this));
        loadData();

        mainSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadServicesCategories();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadData() {

        if (new InternetConnection(getBaseContext()).isInternetAvailable()) {

            loadSliderImages();
//            loadServicesCategories();
        } else {
//            SimpleToast.warning(getBaseContext(), "No Internet Available");
            showWirelessSettings();
        }
    }

    private void showWirelessSettings() {
        Snackbar snackbar = Snackbar
                .make(mainCoordinatorLayout, "Wifi & Data Disabled!", Snackbar.LENGTH_LONG)
                .setAction("Enable", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
// Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    private void loadSliderImages() {
        showRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(SLIDER_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray result) {
                showRefreshing(false);

                Log.i(Config.TAG, "onResponse: Slider Result= " + result.toString());
                parseSliderResult(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showRefreshing(false);

                Snackbar snackbar = Snackbar
                        .make(mainCoordinatorLayout, "Internet Connection Error", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                loadData();
                            }
                        });
// Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);

                snackbar.show();

//                SimpleToast.error(getBaseContext(), "Something Went Wrong While Fetching The Services");
                Log.e(Config.TAG, "onErrorResponse: Error= " + error.getMessage());
            }
        });

        App.getInstance().addToRequestQueue(request);

    }

    private void parseSliderResult(JSONArray result) {

        sliderImages.clear();

        String sliderImageUrl;
        try {
            for (int i = 0; i < result.length(); i++) {

//                caption = category.getString(CAPTION);
                sliderImageUrl = SLIDER_IMAGE_URL + result.getString(i);
//                sliderImageUrl = SLIDER_IMAGE_URL + category.getString(SLIDER_IMAGE).replace(IMAGE_REGEX, ANDROID_FORMAT);

                Log.i(Config.TAG, "parseSliderResult: SliderImageUrl= " + sliderImageUrl);

                sliderImages.add(new Slider(sliderImageUrl));
            }

            SliderAdapter pagerAdapter = new SliderAdapter(this);
            pagerAdapter.setDataList(sliderImages);
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setAutoScrollTime(5000);
            mViewPager.startAutoScroll();
            mLineIndicator.setViewPager(mViewPager);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Config.TAG, "parseCategoriesResult: Error converting json array" + e.getMessage());
        }

    }

    private void showRefreshing(boolean refreshing) {
        if (refreshing) {
            mainSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mainSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mainSwipeRefreshLayout.setRefreshing(false);
        }
    }



    private void assignViews() {

        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        mainSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);
        categoriesRecyclerView = (RecyclerView) findViewById(R.id.categoriesRecyclerView);
        mViewPager = (InfiniteViewPager) findViewById(R.id.viewpager);
        mLineIndicator = (LinePageIndicator) findViewById(R.id.indicator);

    }
    @Override
    public void onStart() {
        super.onStart();
        if (mViewPager != null)
            mViewPager.startAutoScroll();
    }

    @Override
    public void onStop() {
        if (mViewPager != null)
            mViewPager.stopAutoScroll();
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.action_contact_us:
                final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title("Contact Us")
                        .customView(R.layout.dialog_contact_us, true)
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .negativeColor(Color.RED)
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Toast.makeText(getBaseContext(), "Dialog Ready", Toast.LENGTH_LONG).show();
                            }
                        })
                        .canceledOnTouchOutside(false);

                MaterialDialog dialog = builder.build();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.nav_books:
                startActivity(new Intent(MainActivity.this, BooksActivity.class));
                break;
            case R.id.nav_verse:
                startActivity(new Intent(MainActivity.this, VerseActivity.class));
                break;
            case R.id.nav_devotions:
                startActivity(new Intent(MainActivity.this, DevotionActivity.class));
                break;
//            case R.id.nav_services:
//                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//                break;
            case R.id.nav_contact:
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title("Contact Us")
                        .customView(R.layout.dialog_contact_us, true)
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .negativeColor(Color.RED)
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Toast.makeText(getBaseContext(), "Dialog Ready", Toast.LENGTH_LONG).show();
                            }
                        })
                        .canceledOnTouchOutside(false);

                MaterialDialog dialog = builder.build();
                dialog.show();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi, Download and Install this great, Cleaning Request app, http://androidmastermind.blogspot.co.ke/");
// Pass the intent into the createShareBottomSheet method to generate the BottomSheet.
                BottomSheet share = BottomSheet.createShareBottomSheet(MainActivity.this, intent, "Tell A Friend About Jemmin", true);

// Make sure that it doesn't return null! If the system can not handle the intent, null will be returned.
                if (share != null) {
                    share.show();
                }
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
