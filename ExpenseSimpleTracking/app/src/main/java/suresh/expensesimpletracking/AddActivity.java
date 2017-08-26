package suresh.expensesimpletracking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import Constants.FontConstants;
import Utils.FontUtility;

public class AddActivity extends Activity implements OnClickListener {

    EditText addTitle, addPrice;
    Button addButton;
    SQLiteDBUtil sqLiteDBUtil;
    private InterstitialAd mInterstitialAd;
    int expenseCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());

        addTitle = (EditText) findViewById(R.id.addexpensestext);
        addPrice = (EditText) findViewById(R.id.addprice);
        addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(this);
        setFontStyles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adMOb();
    }

    private void adMOb() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void invokeAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }


    private void setFontStyles() {
        FontUtility.setCustomFont(addTitle, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(addPrice, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(addButton, FontConstants.FONT_ROBOT_LIGHT, this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add:
                sqLiteDBUtil.OpenDB();
                expenseCount++;
                if (AllDataOK()) {

                    ArrayList<Object> data = new ArrayList<Object>();
                    data.add(addTitle.getText().toString());
                    data.add(addPrice.getText().toString());
                    String currentDate = getCurrDate();
                    data.add(currentDate);
                    data.add(getCurrentMonth(currentDate));

                    sqLiteDBUtil.insertIntoBudget(data);

                    Toast.makeText(AddActivity.this, "Submitted", Toast.LENGTH_LONG).show();

                    addTitle.setText("");
                    addPrice.setText("");
                }

                sqLiteDBUtil.CloseDB();
                if(expenseCount>3)
                    invokeAd();
                break;

            default:
                break;
        }
    }

    private Object getCurrentMonth(String currentDate) {
        String inputPattern = "MMM dd yyyy, HH:mm:ss a";
        String outputPattern = "MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(currentDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            String alternate = "MMM dd, yyyy HH:mm:ss a";
            SimpleDateFormat alternateFormat = new SimpleDateFormat(alternate);
            try {
                date = alternateFormat.parse(currentDate);
                str = outputFormat.format(date);
            }catch (Exception e2){


            }
            e.printStackTrace();
        }
        return str;
    }

    private boolean AllDataOK() {
        // TODO Auto-generated method stub
        if (addTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please add a Title!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String getCurrDate() {
        String dtStr;
        Date dt = Calendar.getInstance().getTime();
        String inputPattern = "MMM dd yyyy, HH:mm:ss a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        dtStr = inputFormat.format(dt);
        return dtStr;
    }
}
