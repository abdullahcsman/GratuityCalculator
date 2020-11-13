package gratuitycalculator.becotech.com.gratuitycalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    AppCompatActivity mActivity;

    AdView mAdView;
    Button btn;
    EditText editText;
    TextView textView_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.share_btnq1);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//
//
//        getSupportActionBar().setLogo(R.drawable.share_btnq1);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mActivity = this;

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);



        textView_result = (TextView) findViewById(R.id.result);
        editText = (EditText) findViewById(R.id.editText_start);
        editText.addTextChangedListener(onTextChangedListener());
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_result.setVisibility(View.VISIBLE);
                if (editText.getText().length() == 0) {

                    textView_result.setText("Please enter your basic salary");
                }
                else {
//                    double result = 0.0;
                    double result = 0.0;
//                String content = editText.getText().toString();
//                int nIntFromET = Integer.parseInt(content);
                    String s = editText.getText().toString();
                    double salary = Double.parseDouble(s.replaceAll(",",""));
                    result = (salary * 12.0) / 365.0;
//                 result = nIntFromET*12/365;

                    textView_result.setText("Your Gratuity: " + Math.round(result));

                    long i = Math.round(result);

                    DecimalFormat myFormatter = new DecimalFormat("#,###");
                    String output = myFormatter.format(i);
                    textView_result.setText("Your per day salary: " + output +" AED");
                }
            }
        });



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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Share_result();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent i = new Intent(activity2.this, MainActivity.class);
            startActivity(i);
            finish();
//            getActionBar().setTitle("Gratuity Calculator");

        }

        else if (id == R.id.ind_camera) {

            Intent i = new Intent(activity2.this, Main2Activity.class);
            startActivity(i);
            finish();


        }
        else if (id == R.id.nav_gallery) {

//            Intent i = new Intent(activity2.this, activity2.class);
//            startActivity(i);
//            finish();

        }
//        else if (id == R.id.nav_slideshow) {
//
//        }
//        else if (id == R.id.nav_manage) {
//
//        }
        else if (id == R.id.nav_share) {

            Uri uri = Uri.parse("market://details?id=" + "com.beaconites.gratuitycalculator");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" +  "com.beaconites.gratuitycalculator")));
            }
        }
//        else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editText.addTextChangedListener(this);
            }
        };
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));
    }

    public void Share_result() {
        String uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();

        Bitmap bitmap;

        View v = findViewById(R.id.snapview);
        mAdView.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        v.setDrawingCacheEnabled(true);
        bitmap = v.getDrawingCache();


        try {
            File file = new File(getApplicationContext().getExternalCacheDir(), uniqueId + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_TEXT, "Here is my Per day salary. To Check yours, Download this App: \n https://play.google.com/store/apps/details?id=com.beaconites.gratuitycalculator ");
            intent.putExtra(Intent.EXTRA_TITLE, "UAE Gratuity Calculator");
            intent.setType("image/*");
//            startActivity(Intent.createChooser(intent, "Share Screenshot"));

            startActivity(Intent.createChooser(intent, "choose one"));
            mAdView.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);

            //                   intent.setType("*/*");
            // startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        v.setDrawingCacheEnabled(false);
    }
}
