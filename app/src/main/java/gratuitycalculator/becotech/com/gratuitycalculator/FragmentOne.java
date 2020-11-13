package gratuitycalculator.becotech.com.gratuitycalculator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {

    Button btn;
    EditText editText;
    TextView textView_result;

    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view=   inflater.inflate(R.layout.fragment_fragment_one, container, false);

        textView_result = (TextView)view.findViewById(R.id.result);
        editText = (EditText)view.findViewById(R.id.editText_start);
        editText.addTextChangedListener(onTextChangedListener());
        btn = (Button) view.findViewById(R.id.button);
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


        return view;
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

}
