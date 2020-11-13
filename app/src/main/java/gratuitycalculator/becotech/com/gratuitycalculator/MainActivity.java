package gratuitycalculator.becotech.com.gratuitycalculator;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imageView_start, imageView_end;

    private EditText editText_start;
    private EditText editText_end;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private Button button;
    private View view;
    private RelativeLayout relative_layout_image;
    private Calendar myCalendar, myCalendar_st;
    DatePickerDialog.OnDateSetListener date, date1;
    SimpleDateFormat start_df, end_df;
    TextView textView_result;
    EditText editText_salary;
    AppCompatActivity mActivity;
    int value;
    int progress;
    double totalGratuity = 0.0;
    double perdaySalary = 0.0;
    double serviceYears = 0.0;
    double salary;
    int totalDays;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

//        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        editText_salary = (EditText) findViewById(R.id.editText_salary);
        // editText_salary =0;
        editText_salary.addTextChangedListener(onTextChangedListener());
        editText_start = (EditText) findViewById(R.id.editText_start);

        editText_end = (EditText) findViewById(R.id.editText_end);
        button = (Button) findViewById(R.id.button);
        radioGroup = (RadioGroup) findViewById(R.id.radioterminate);
        myCalendar_st = Calendar.getInstance();

        myCalendar = Calendar.getInstance();
        textView_result = (TextView) findViewById(R.id.result);



        editText_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Panel , date, myCalendar_st
                        .get(Calendar.YEAR), myCalendar_st.get(Calendar.MONTH),
                        myCalendar_st.get(Calendar.DAY_OF_MONTH)).show();
                editText_end.requestFocus();
            }
        });


        editText_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Panel, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


          editText_salary.requestFocus();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                // TODO Auto-generated method stub
                myCalendar_st.set(Calendar.YEAR, year);
                myCalendar_st.set(Calendar.MONTH, monthOfYear);
                myCalendar_st.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(0);
            }
        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(1);
            }

        };



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                textView_result.setVisibility(View.VISIBLE);
                if (editText_start.getText().length() == 0) {

                    textView_result.setText("Please enter date of joining");
                } else if (editText_end.getText().length() == 0) {
                    textView_result.setText("Please enter date of Ending");
                } else if (editText_salary.getText().length() == 0) {

                    textView_result.setText("Please enter your salary");
                } else {
                    {

                        double totalGratuity = 0.0;
                        double perdaySalary = 0.0;
                        double serviceYears = 0.0;
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        radioButton = (RadioButton) findViewById(selectedId);
                        double totalDays = (double)get_count_of_days(editText_start.getText().toString(), editText_end.getText().toString());
                        //textView_result.setText("Your Gratuity: "+totalDays);

                        String s = editText_salary.getText().toString();

                        double salary = Double.parseDouble(s.replaceAll(",",""));

                        perdaySalary = (salary * 12.0) / 365.0;
                        serviceYears = totalDays / 365.0;
                        if (radioButton.getText().toString().equalsIgnoreCase("Fired")) {
                            if (serviceYears >= 5) {
                                totalGratuity = (perdaySalary * serviceYears) * 30.0;
                            } else if (serviceYears < 5 && serviceYears >= 3) {
                                totalGratuity = (perdaySalary * serviceYears) * 21.0;
                            } else if (serviceYears < 3 && serviceYears >= 1) {
                                totalGratuity = (perdaySalary * serviceYears) * 21.0;
                            }
                        } else {
                            if (serviceYears >= 5) {
                                totalGratuity = (perdaySalary * serviceYears) * 30.0;
                            } else if (serviceYears < 5 && serviceYears >= 3) {
                                totalGratuity = (perdaySalary * serviceYears) * 14.0;
                            } else if (serviceYears < 3 && serviceYears >= 1) {
                                totalGratuity = (perdaySalary * serviceYears) * 7.0;
                            }
                        }


                        textView_result.setText("Your Gratuity: " + Math.round(totalGratuity));
//Changing
                        long i = Math.round(totalGratuity);

                        DecimalFormat myFormatter = new DecimalFormat("#,###");
                        String output = myFormatter.format(i);
                        textView_result.setText("Your Gratuity: " + output +" AED");
//changing End
                    }

                }


            }


        });


    }

    private void updateLabel(int value) {




        //===========================
        //===========================

//        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String myFormat = "dd MMM, yyyy";

        if (value == 1) {
            end_df = new SimpleDateFormat(myFormat, Locale.US);
            editText_end.setText(end_df.format(myCalendar.getTime()));
        } else {
            start_df = new SimpleDateFormat(myFormat, Locale.US);
            editText_start.setText(start_df.format(myCalendar_st.getTime()));
        }
    }

    public int get_count_of_days(String Created_date_String, String Expire_date_String) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        int result = 0;

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            long timeOne = Created_convertedDate.getTime();
            long timeTwo = Expire_CovertedDate.getTime();
            long oneDay = 1000 * 60 * 60 * 24;

            long delta = (timeTwo - timeOne) / oneDay;
            result = (int) delta;
            textView_result.setText("Your Gratuity: " + result + "AED");
            DecimalFormat myFormatter = new DecimalFormat("#,###");

                      String output = myFormatter.format(result);

                       textView_result.setText("Your Gratuity: " + output + " AED");

        } catch (ParseException e) {
            e.printStackTrace();
        }
//Toast.makeText(mActivity,"days: "+result,Toast.LENGTH_SHORT).show();
        return (int) result;
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
        button.setVisibility(View.INVISIBLE);
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
            intent.putExtra(Intent.EXTRA_TEXT, "Here is my Gratuity Calculation. To Check yours, Download this App: \n https://play.google.com/store/apps/details?id=com.beaconites.gratuitycalculator ");
            intent.putExtra(Intent.EXTRA_TITLE, "UAE Gratuity Calculator");
            intent.setType("image/*");
//            startActivity(Intent.createChooser(intent, "Share Screenshot"));

            startActivity(Intent.createChooser(intent, "choose one"));
            mAdView.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            //                   intent.setType("*/*");
            // startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        v.setDrawingCacheEnabled(false);
    }

    public void shareResult(View view) {
        Share_result();
    }

     ////////////////////////////////////
    //funtion text watch is used////////
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
                editText_salary.removeTextChangedListener(this);

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
                    editText_salary.setText(formattedString);
                    editText_salary.setSelection(editText_salary.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editText_salary.addTextChangedListener(this);
            }
        };
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

            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();


        }

        else if (id == R.id.ind_camera) {

            Intent i = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(i);
            finish();


        }
        else if (id == R.id.nav_gallery) {

            Intent i = new Intent(MainActivity.this, activity2.class);
            startActivity(i);
            finish();

        }
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


}
