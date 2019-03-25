package nk.mobileapps.currencyconverter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;

public class ConverterActivity extends Activity implements OnClickListener {

    public static final int FROM_REQUEST_CODE = 11, TO_REQUEST_CODE = 22;
    public ProgressDialog pd;
    LinearLayout LINEARLAYOUT_CURRENCY_FROM, LINEARLAYOUT_CURRENCY_TO;
    TextView TXT_FROM_CURRENCY_CODE, TXT_FROM_CURRENCY_NAME;
    TextView TXT_TO_CURRENCY_CODE, TXT_TO_CURRENCY_NAME;
    ImageView IMG_CURRENCYARROW;
    EditText et_currencyvalue;
    TextView tv_currencyresultv, tv_lastupdate;
    Button BTN_CONVERTER;
    String TAG = "ConverterActivity";
    Animation animRotate, animSlideLeft, animSlideRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.round);
        animSlideLeft = AnimationUtils
                .loadAnimation(this, R.anim.slide_in_left);
        animSlideRight = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right);
        findViews();
    }

    private void findViews() {

        LINEARLAYOUT_CURRENCY_FROM = (LinearLayout) findViewById(R.id.LINEARLAYOUT_CURRENCY_FROM);
        LINEARLAYOUT_CURRENCY_TO = (LinearLayout) findViewById(R.id.LINEARLAYOUT_CURRENCY_TO);
        TXT_FROM_CURRENCY_CODE = (TextView) findViewById(R.id.TXT_FROM_CURRENCY_CODE);
        TXT_FROM_CURRENCY_NAME = (TextView) findViewById(R.id.TXT_FROM_CURRENCY_NAME);
        TXT_TO_CURRENCY_CODE = (TextView) findViewById(R.id.TXT_TO_CURRENCY_CODE);
        TXT_TO_CURRENCY_NAME = (TextView) findViewById(R.id.TXT_TO_CURRENCY_NAME);
        IMG_CURRENCYARROW = (ImageView) findViewById(R.id.IMG_CURRENCYARROW);

        et_currencyvalue = (EditText) findViewById(R.id.et_currencyvalue);
        tv_currencyresultv = (TextView) findViewById(R.id.tv_currencyresultv);
        tv_lastupdate = (TextView) findViewById(R.id.tv_lastupdate);
        BTN_CONVERTER = (Button) findViewById(R.id.BTN_CONVERTER);


        et_currencyvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reset();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LINEARLAYOUT_CURRENCY_FROM.setOnClickListener(this);
        LINEARLAYOUT_CURRENCY_TO.setOnClickListener(this);
        IMG_CURRENCYARROW.setOnClickListener(this);
        BTN_CONVERTER.setOnClickListener(this);
        reset();

    }

    private void reset() {
        tv_currencyresultv.setText("");
        tv_lastupdate.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case FROM_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    TXT_FROM_CURRENCY_CODE.setText(data.getExtras()
                            .getString("CCODE").toString().trim());
                    TXT_FROM_CURRENCY_NAME.setText(data.getExtras()
                            .getString("CNAME").toString().trim());
                }
                break;
            case TO_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    TXT_TO_CURRENCY_CODE.setText(data.getExtras()
                            .getString("CCODE").toString().trim());
                    TXT_TO_CURRENCY_NAME.setText(data.getExtras()
                            .getString("CNAME").toString().trim());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        Intent i;
        if (v == (View) LINEARLAYOUT_CURRENCY_FROM) {
            reset();
            i = new Intent(ConverterActivity.this, CurrencyListActivity.class);
            i.putExtra("requestcode", FROM_REQUEST_CODE + "");
            startActivityForResult(i, FROM_REQUEST_CODE);
        } else if (v == (View) LINEARLAYOUT_CURRENCY_TO) {
            reset();
            i = new Intent(ConverterActivity.this, CurrencyListActivity.class);
            i.putExtra("requestcode", TO_REQUEST_CODE + "");
            startActivityForResult(i, TO_REQUEST_CODE);
        } else if (v == (View) IMG_CURRENCYARROW) {
            reset();
            // round animation
            runAnimation(R.anim.round, IMG_CURRENCYARROW);
            String from_code = TXT_FROM_CURRENCY_CODE.getText().toString()
                    .trim();
            String from_name = TXT_FROM_CURRENCY_NAME.getText().toString()
                    .trim();
            String to_code = TXT_TO_CURRENCY_CODE.getText().toString().trim();
            String to_name = TXT_TO_CURRENCY_NAME.getText().toString().trim();

            // From -to
            TXT_FROM_CURRENCY_CODE.setText(to_code);
            TXT_FROM_CURRENCY_NAME.setText(to_name);
            TXT_FROM_CURRENCY_CODE.startAnimation(animSlideRight);
            TXT_FROM_CURRENCY_NAME.startAnimation(animSlideRight);
            // To-Form
            TXT_TO_CURRENCY_CODE.setText(from_code);
            TXT_TO_CURRENCY_NAME.setText(from_name);
            TXT_TO_CURRENCY_CODE.startAnimation(animSlideLeft);
            TXT_TO_CURRENCY_NAME.startAnimation(animSlideLeft);
            stopAnimation(IMG_CURRENCYARROW);
        } else if (v == (View) BTN_CONVERTER) {
            setCurrencyValue();
        }
    }

    private void getOnlineMoneyData(final Context context, final String fromCurrencyCode, final String toCurrencyCode) {
        //http://data.fixer.io/api/latest?access_key=a7089eae57c587163218926f7d7990b1&symbols=USD,AUD,CAD,PLN,MXN&format=1
        //"http://data.fixer.io/api/lastest?access_key=a7089eae57c587163218926f7d7990b1&symbols=fromCurrencyCode,toCurrencyCode&format=1";
        String url = "http://data.fixer.io/api/latest?access_key=a7089eae57c587163218926f7d7990b1";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                closeProgressDialog();
                Log.d("Tag", response.toString());
                try {
                    String date = response.getString("date");
                    JSONObject rates = response.getJSONObject("rates");

                    double baseRate = Double.valueOf(rates.getString("USD"));
                    double initRate = Double.valueOf(rates.getString(fromCurrencyCode));
                    double targetRate = Double.valueOf(rates.getString(toCurrencyCode));
                    double first_input = Double.valueOf(et_currencyvalue.getText().toString());
                    String resultFinal = String.valueOf(String.format("%.3f", ((targetRate * first_input) / initRate)));
                    tv_currencyresultv.setText(resultFinal);
                    Timestamp ts = new Timestamp(response.getLong("timestamp"));
                    Date time = new Date(ts.getTime());
                    tv_lastupdate.setText("Last updated on " + date );
                    runAnimation(android.R.anim.fade_in, tv_lastupdate);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgressDialog();
                reset();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        // Adding request to request queue
        queue.add(jsonObjReq);

    }

    private void runAnimation(int rID, View textView) {
        Animation a = AnimationUtils.loadAnimation(this, rID);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
    }

    private void stopAnimation(final View textView) {

        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.clearAnimation();
            }
        }, 1000);

    }

    private void setCurrencyValue() {
        final String fromCurrencyCode = TXT_FROM_CURRENCY_CODE.getText()
                .toString().trim();
        final String toCurrencyCode = TXT_TO_CURRENCY_CODE.getText().toString()
                .trim();
        if ((et_currencyvalue.getText().toString().length() > 0)) {
            if (fromCurrencyCode.equals(toCurrencyCode)) {
                tv_currencyresultv.setText(et_currencyvalue.getText()
                        .toString().trim());
            } else {
                try {
                    final double amount = Double.parseDouble(et_currencyvalue
                            .getText().toString());
                    showProgressDialog("Please Wait...");
                    getOnlineMoneyData(ConverterActivity.this, fromCurrencyCode, toCurrencyCode);
                } catch (NumberFormatException e) {
                    Toast.makeText(ConverterActivity.this,
                            "Number Format Exception", Toast.LENGTH_SHORT)
                            .show();
                    reset();
                }
            }
        } else {
            Toast.makeText(this, "Please Enter Currency Value",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void showProgressDialog(String msg) {
        try {
            pd = new ProgressDialog(this);
            // pd = CustomProgressDialog.ctor(this, msg);
            // pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            pd.setMessage(msg);
            pd.setCancelable(true);
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void closeProgressDialog() {
        try {
            if (pd != null)
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
