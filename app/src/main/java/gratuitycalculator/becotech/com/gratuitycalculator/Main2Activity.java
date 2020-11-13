package gratuitycalculator.becotech.com.gratuitycalculator;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText editText, editText1, editText2;
    private Button button1;
    private Calendar myCalendar, myCalendar_st;
    private RadioButton radioButton1;
    private RadioGroup radioGroup2;
    TextView t;

    AdView mAdView;
  AppCompatActivity mActivity;
    DatePickerDialog.OnDateSetListener dat, date1;
    SimpleDateFormat start_df, end_df;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar too = (Toolbar) findViewById(R.id.toolbar);
        too.setTitle("Gratuity Calculator");
        setSupportActionBar(too);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, too, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        mActivity=this;
        editText=(EditText) findViewById(R.id.ed1);
        editText.addTextChangedListener(onTextChangedListener());
        editText1=(EditText) findViewById(R.id.ed2);
        editText2=(EditText) findViewById(R.id.ed3);

        button1=(Button) findViewById(R.id.p);

        myCalendar=Calendar.getInstance();
        myCalendar_st=Calendar.getInstance();
        radioGroup2=(RadioGroup) findViewById(R.id.radioterminate);
        t= (TextView) findViewById(R.id.res);

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Main2Activity.this,android.R.style.Theme_Holo_Light_Panel , dat, myCalendar_st
                        .get(Calendar.YEAR), myCalendar_st.get(Calendar.MONTH),
                        myCalendar_st.get(Calendar.DAY_OF_MONTH)).show();
                editText1.requestFocus();
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Main2Activity.this, android.R.style.Theme_Holo_Light_Panel, date1, myCalendar. get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                editText2.requestFocus();
            }
        });

        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int mon,
                                  int day) {


                // TODO Auto-generated method stub
                myCalendar_st.set(Calendar.YEAR, year);
                myCalendar_st.set(Calendar.MONTH, mon);
                myCalendar_st.set(Calendar.DAY_OF_MONTH, day);

                updateLabel(0);
            }
        };

        date1= new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mon, int day) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,mon);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(1);
            }
        };
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setVisibility(View.VISIBLE);

                if (editText.length() == 0) {
                    t.setText("Please Enter the Basic Salary");
                } else if (editText1.length() == 0) {
                    t.setText("Please Enter the Starting Date");
                } else if (editText2.length() == 0) {
                    t.setText("Please enter the Ending Date");
                } else {
                    double totalGratuity = 0.0;
                    double serviceYears = 0.0;
                    long i=0;
                    int selectedId = radioGroup2.getCheckedRadioButtonId();
                    radioButton1 = (RadioButton) findViewById(selectedId);

                    long totalDays = get_count_of_days(editText1.getText().toString(), editText2.getText().toString());
                    String s = editText.getText().toString();
                    double salary = Double.parseDouble(s.replaceAll(",", ""));
//            Toast.makeText(MainActivity.this, "days: " + totalDays, Toast.LENGTH_SHORT).show();
                    serviceYears = totalDays ;
//
                    if (serviceYears >= 5) {
                        totalGratuity = salary * 15 / 26 * serviceYears;
                    } else if (serviceYears < 5) {
                        t.setText("You are not eligible");
                    }
                    i = Math.round(totalGratuity);
                    // Toast.makeText(Main2Activity.this,"Result" + i, Toast.LENGTH_SHORT).show();;
                    DecimalFormat myFormatter = new DecimalFormat("#,###");
                    String output = myFormatter.format(i);
                    t.setText("Your Gratuity: " + output +" Rupee");
                }
            }


        });


    }
    private void updateLabel(int value) {



        String myFormat = "dd MMM, yyyy";

        if (value == 1) {
            end_df = new SimpleDateFormat(myFormat, Locale.US);
            editText2.setText(end_df.format(myCalendar.getTime()));
        } else {
            start_df = new SimpleDateFormat(myFormat, Locale.US);
            editText1.setText(start_df.format(myCalendar_st.getTime()));
        }
    }


    public long get_count_of_days(String Created_date_String, String Expire_date_String) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        float result = 0;
        long i = 0;
        float ye = 0;

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            long timeOne = Created_convertedDate.getTime();
            long timeTwo = Expire_CovertedDate.getTime();
            long oneDay = 1000 * 60 * 60 * 24;
//            Calendar c= new GregorianCalendar(TimeZone.getTimeZone(Created_date_String));
//            Calendar d= new GregorianCalendar(TimeZone.getTimeZone(Expire_date_String));
//         int yearsInBetween = Expire_date_String.get(Calendar.YEAR) - Created_date_String.get(Calendar.YEAR);
//         int monthsDiff = today.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
//            long ageInMonths = yearsInBetween*12 + monthsDiff;
//            long age = yearsInBetween;




            long delta = (timeTwo - timeOne) / oneDay;
            long delt = (timeTwo - timeOne) / 365;
            result = (float) delta;
//            Toast.makeText(MainActivity.this,"daysss: "+result,Toast.LENGTH_SHORT).show();

            ye = result/365;
            i = Math.round(ye);
//            Toast.makeText(MainActivity.this,"da: "+i,Toast.LENGTH_SHORT).show();
//            textView_result.setText("Your Gratuity: " + result + "AED");
//            DecimalFormat myFormatter = new DecimalFormat("#,###");
//
//            String output = myFormatter.format(result);
//
//            textView_result.setText("Your Gratuity: " + output + " AED");

        } catch (ParseException e) {
            e.printStackTrace();
        }
//Toast.makeText(mActivity,"days: "+result,Toast.LENGTH_SHORT).show();
        return i;
    }
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                editText.removeTextChangedListener(this);

                try {
                    Long l;
                    String os=editable.toString();
                    if(os.contains(","))
                    {
                        os=os.replaceAll(",","");

                    }
                    l=Long.parseLong(os);
                    DecimalFormat decimalFormat=(DecimalFormat) NumberFormat.getInstance(Locale.US);
                    decimalFormat.applyPattern("#,###,###,###");
                    String formattedString = decimalFormat.format(l);
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
        button1.setVisibility(View.INVISIBLE);
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
            button1.setVisibility(View.VISIBLE);

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
        getMenuInflater().inflate(R.menu.main2, menu);
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

            Intent i = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(i);
            finish();


        }

       else if (id == R.id.ind_camera) {

            Intent i = new Intent(Main2Activity.this, Main2Activity.class);
            startActivity(i);
            finish();


        }
        else if (id == R.id.nav_gallery) {

            Intent i = new Intent(Main2Activity.this, activity2.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
