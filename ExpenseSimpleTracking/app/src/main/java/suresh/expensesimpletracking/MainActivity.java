package suresh.expensesimpletracking;

import android.app.Activity;
import android.app.AlertDialog;
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

public class MainActivity extends Activity implements OnClickListener{

	Button addtransaction, viewtransaction, clearRecords, whatisrequired;
	SQLiteDBUtil sqLiteDBUtil;
	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sqLiteDBUtil  = new SQLiteDBUtil(getApplicationContext());
		//sqLiteDBUtil.CreateDB();
		
		addtransaction = (Button)findViewById(R.id.addtransaction);
		viewtransaction = (Button)findViewById(R.id.viewtransaction);
		clearRecords = (Button)findViewById(R.id.clearearlierrecords);
		whatisrequired = (Button)findViewById(R.id.whatisrequired);
		
		addtransaction.setOnClickListener(this);
		viewtransaction.setOnClickListener(this);
		clearRecords.setOnClickListener(this);
		whatisrequired.setOnClickListener(this);
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
               // openViewActivity();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                openViewActivity();
            }
        });

    }

	private void setFontStyles() {
		FontUtility.setCustomFont(addtransaction, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(viewtransaction, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(clearRecords, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(whatisrequired, FontConstants.FONT_ROBOT_LIGHT, this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addtransaction:
			Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
			MainActivity.this.startActivity(addIntent);
			break;
			
		case R.id.viewtransaction:
            invokeAd();
			break;
			
		case R.id.clearearlierrecords:
			
			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
					.create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert");

			// Setting Dialog Message
			alertDialog.setMessage("Do you want to erase all earlier records?");

			// Setting Icon to Dialog
			alertDialog.setIcon(R.drawable.alert_icon);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed

					sqLiteDBUtil.OpenDB();
					sqLiteDBUtil.getDataOnDemand(getApplicationContext(),
							"DELETE from budget_table");
					sqLiteDBUtil.CloseDB();

					Toast.makeText(getApplicationContext(),
							"Cleared all data successfully!", Toast.LENGTH_LONG)
							.show();
				}
			});

			// Showing Alert Message
			alertDialog.show();

		break;
		case R.id.whatisrequired:
			Intent requiredIntent = new Intent(MainActivity.this, RequiredActivity.class);
			startActivity(requiredIntent);
			break;
		default:
			break;
		}
	}

    private void openViewActivity() {
        Intent viewIntent = new Intent(MainActivity.this, ViewActivity.class);
        MainActivity.this.startActivity(viewIntent);
    }

    private void invokeAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
			openViewActivity();
        }

    }
}
