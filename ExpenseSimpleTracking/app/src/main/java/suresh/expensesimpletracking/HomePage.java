package suresh.expensesimpletracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Constants.AppConstants;
import Constants.FontConstants;
import Utils.FontUtility;
import Utils.NetworkUtility;
import Utils.SDKSettings;

public class HomePage extends Activity implements OnClickListener {
    Button daily_expense, trip_expense;
    SQLiteDBUtil sqLiteDBUtil;
    Dialog mAdInternet, mAdUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        daily_expense = (Button) findViewById(R.id.daily_expense);
        trip_expense = (Button) findViewById(R.id.trip_expense);

        setFontStyles();

        daily_expense.setOnClickListener(this);
        trip_expense.setOnClickListener(this);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
        sqLiteDBUtil.CreateDB();

        PreferenceManager.setDefaultValues(this, R.xml.mypreference, false);
        checkForAPKUpdates();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setFontStyles() {
        FontUtility.setCustomFont(daily_expense, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(trip_expense, FontConstants.FONT_ROBOT_LIGHT, this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.daily_expense:
                Intent DailyIntent = new Intent(HomePage.this, MainActivity.class);
                startActivity(DailyIntent);
                break;
            case R.id.trip_expense:
                Intent TripIntent = new Intent(HomePage.this, TripActivity.class);
                startActivity(TripIntent);
                break;
            default:
                break;
        }
    }

    //To force update the user
    String latestVersion;

    private String getCurrentVersion() {
        String currentVersion;
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        return currentVersion;
    }

    private void checkForAPKUpdates() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            new GetLatestVersion().execute();
        }
    }

    private class GetLatestVersion extends AsyncTask<String, String, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HomePage.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=suresh.expensesimpletracking&hl=en";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                if (doc != null) {
                    latestVersion = doc.getElementsByAttributeValue
                            ("itemprop", "softwareVersion").first().text();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean jsonObject) {
            super.onPostExecute(jsonObject);
            if (progressDialog != null)
                progressDialog.dismiss();
            if (jsonObject) {
                String currentVersion = getCurrentVersion();
                Toast.makeText(HomePage.this, currentVersion + ", " + latestVersion, Toast.LENGTH_LONG).show();
                if (latestVersion != null && currentVersion != null) {
                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        SDKSettings.setSharedPreferenceString(HomePage.this, AppConstants.KEY_PREF_VERSION, latestVersion);
                        showUpdateDialog();
                    }
                }
            }
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=suresh.expensesimpletracking")));
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        mAdInternet = builder.show();
    }
}
