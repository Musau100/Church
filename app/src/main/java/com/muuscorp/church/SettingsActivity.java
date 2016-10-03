package com.muuscorp.church;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.net.URISyntaxException;

import static com.muuscorp.church.Config.RINGTONE_PATH;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int RQS_OPEN_AUDIO_MP3 = 1;
    ImageView btnSelectRingtone;
    private TextView txtAboutJemminApp, ringtonePath;


    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor != null ? cursor.getColumnIndexOrThrow("_data") : 0;
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        assignViews();
        btnSelectRingtone.setOnClickListener(this);
        txtAboutJemminApp.setOnClickListener(this);
    }

    private void assignViews() {

        btnSelectRingtone = (ImageView) findViewById(R.id.btnSelectRingtone);
        txtAboutJemminApp = (TextView) findViewById(R.id.txtAboutJemminApp);
        ringtonePath = (TextView) findViewById(R.id.ringtonePath);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                close this activity and return to previous one if any
                finish();
                break;
            case R.id.txtAboutJemminApp:
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectRingtone:
//                show file explorer
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a Ringtone"), RQS_OPEN_AUDIO_MP3);

//                SimpleToast.info(getBaseContext(), "Ready to roll");
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_OPEN_AUDIO_MP3:
                    Uri uri = data.getData();
                    Log.d(Config.TAG, "onActivityResult: File Uri = " + uri.toString());
                    try {
                        String filePath = getPath(getBaseContext(), uri);
                        Prefs.putString(RINGTONE_PATH, filePath);
                        ringtonePath.setText(filePath);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
