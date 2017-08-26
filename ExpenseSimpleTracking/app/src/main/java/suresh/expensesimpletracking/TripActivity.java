package suresh.expensesimpletracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import Constants.FontConstants;
import Utils.FontUtility;

public class TripActivity extends Activity implements OnClickListener {
    Button add_member, add_expense, view_expense, remove_trip;
    SQLiteDBUtil sqLiteDBUtil;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_page);
        add_member = (Button) findViewById(R.id.ad_member);
        add_expense = (Button) findViewById(R.id.ad_expense);
        view_expense = (Button) findViewById(R.id.view_expense);
        remove_trip = (Button) findViewById(R.id.remove_trip_data);

        add_expense.setOnClickListener(this);
        add_member.setOnClickListener(this);
        view_expense.setOnClickListener(this);
        remove_trip.setOnClickListener(this);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
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

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                openDiaolog();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                openDiaolog();
            }
        });
    }

    private void invokeAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void setFontStyles() {
        FontUtility.setCustomFont(add_member, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(add_expense, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(view_expense, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(remove_trip, FontConstants.FONT_ROBOT_LIGHT, this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ad_member:
                Intent addMember = new Intent(this, AddMemberActivity.class);
                startActivity(addMember);
                break;
            case R.id.ad_expense:
                Intent addExpense = new Intent(this, AddExpenseActivity.class);
                startActivity(addExpense);
                break;
            case R.id.view_expense:
                invokeAd();
                break;
            case R.id.remove_trip_data:

                AlertDialog alertDialog = new AlertDialog.Builder(TripActivity.this)
                        .create();

                // Setting Dialog Title
                alertDialog.setTitle("Alert");

                // Setting Dialog Message
                alertDialog.setMessage("Do you want to erase all earlier Trip records?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.alert_icon);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed

                        sqLiteDBUtil.OpenDB();
                        sqLiteDBUtil.getDataOnDemand(getApplicationContext(),
                                "DELETE from trip_budget_table");

                        sqLiteDBUtil.CloseDB();

                        //To open again and close
                        sqLiteDBUtil.OpenDB();

                        sqLiteDBUtil.getDataOnDemand(getApplicationContext(),
                                "DELETE from member_table");

                        sqLiteDBUtil.CloseDB();

                        Toast.makeText(getApplicationContext(),
                                "Cleared all data successfully!", Toast.LENGTH_LONG)
                                .show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                break;
            default:
                break;
        }
    }


    //To open dialog for Gallery and Camera
    Dialog dialog;

    public void openDiaolog() {
        dialog = new Dialog(TripActivity.this);
        dialog.setContentView(R.layout.mediadialog);
        dialog.setTitle("Select an option");
        dialog.setCancelable(true);

        Button gallery = (Button) dialog.findViewById(R.id.view_all);
        Button camera = (Button) dialog.findViewById(R.id.view_individual);

        gallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ViewExpense = new Intent(TripActivity.this, ViewExpenseActivity.class);
                startActivity(ViewExpense);

            }
        });

        camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ViewExpense = new Intent(TripActivity.this, ViewExpenseIndividualActivity.class);
                startActivity(ViewExpense);
            }
        });

        dialog.show();
    }

}
