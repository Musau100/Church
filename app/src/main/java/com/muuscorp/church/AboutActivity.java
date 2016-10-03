package com.muuscorp.church;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.muuscorp.church.Config.ABOUT_URL;
import static com.muuscorp.church.Config.AREAS_WE_SERVE;
import static com.muuscorp.church.Config.EMAIL;
import static com.muuscorp.church.Config.MOBILE;
import static com.muuscorp.church.Config.POSTAL_ADDRESS;
import static com.muuscorp.church.Config.STREET_ADDRESS;
import static com.muuscorp.church.Config.TELEPHONE;

public class AboutActivity extends AppCompatActivity {
    private CoordinatorLayout aboutCoordinatorLayout;
    private SwipeRefreshLayout aboutSwipeRefreshLayout;
    private TextView streetAddress, postalAddress, telephone, mobile, aboutEmail, areasWeServe;
    private ImageView aboutImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        assignViews();

        loadAboutDetails();

        aboutSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAboutDetails();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void loadAboutDetails() {

        if (new InternetConnection(getBaseContext()).isInternetAvailable()) {
            showRefreshing(true);

            JsonObjectRequest request = new JsonObjectRequest(ABOUT_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject result) {
                    showRefreshing(false);

                    Log.i(Config.TAG, "onResponse: About Result= " + result);

                    try {
                        //                    pass the info to ui
//                        Glide.with(getBaseContext())
//                                .load(IMAGES_URL+result.getString(LOGO).replace(IMAGE_REGEX,ANDROID_FORMAT))
//                                .asBitmap()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .placeholder(R.drawable.logo_cropped)
//                                .error(R.drawable.ic_error)
//                                .into(aboutImage);
                        streetAddress.setText(result.getString(STREET_ADDRESS));
                        postalAddress.setText(result.getString(POSTAL_ADDRESS));
                        telephone.setText(result.getString(TELEPHONE));
                        mobile.setText(result.getString(MOBILE));
                        aboutEmail.setText(result.getString(EMAIL));
                        areasWeServe.setText(result.getString(AREAS_WE_SERVE).replaceAll(", ", "\n"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showRefreshing(false);

                    Snackbar snackbar = Snackbar
                            .make(aboutCoordinatorLayout, "Internet Connection Error", Snackbar.LENGTH_LONG)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadAboutDetails();
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

//                    SimpleToast.error(getBaseContext(), "Something Went Wrong While Loading About Info");
                    Log.e(Config.TAG, "onErrorResponse: Error= " + error.getMessage());
                }
            });

            App.getInstance().addToRequestQueue(request);
        } else {
//            SimpleToast.warning(getBaseContext(), "No Internet Available");
            showWirelessSettings();
        }

    }

    private void showRefreshing(boolean refreshing) {

        if (refreshing) {
            aboutSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    aboutSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            aboutSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void showWirelessSettings() {

        Snackbar snackbar = Snackbar
                .make(aboutCoordinatorLayout, "Wifi & Data Disabled!", Snackbar.LENGTH_LONG)
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

    private void assignViews() {
        aboutCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.aboutCoordinatorLayout);
        aboutSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.aboutSwipeRefreshLayout);
        streetAddress = (TextView) findViewById(R.id.streetAddress);
        postalAddress = (TextView) findViewById(R.id.postalAddress);
        telephone = (TextView) findViewById(R.id.telephone);
        mobile = (TextView) findViewById(R.id.mobile);
        aboutEmail = (TextView) findViewById(R.id.aboutEmail);
        areasWeServe = (TextView) findViewById(R.id.areasWeServe);
        aboutImage = (ImageView) findViewById(R.id.aboutImage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                close this activity and return to previous one if any
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
