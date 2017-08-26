package suresh.expensesimpletracking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import Constants.FontConstants;
import Utils.FontUtility;

public class ViewActivity extends Activity {

    ListView transactionList;
    SQLiteDBUtil sqLiteDBUtil;
    TextView totalprice;
    TextView monthlyView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlayout);

        totalprice = (TextView) findViewById(R.id.total);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());

        transactionList = (ListView) findViewById(R.id.transactionlist);

        monthlyView = (TextView) findViewById(R.id.monthly);

        sqLiteDBUtil.OpenDB();
        ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from budget_table");
        sqLiteDBUtil.CloseDB();

        ArrayList<Expense> dataAdaptor = new ArrayList<Expense>();
        int totalAmt = 0;
        for (int i = 0; i < data.size(); i++) {
            String[] arrData = data.get(i);

            Expense expense = new Expense();
            expense.setTitle(arrData[1]);
            expense.setPrice(Integer.parseInt(arrData[2]));
            totalAmt = totalAmt + Integer.parseInt(arrData[2]);
            expense.setDated(arrData[3]);
            dataAdaptor.add(expense);
        }

        //data = getDemoData();
        TransactionAdaptor adaptor = new TransactionAdaptor(getApplicationContext(), dataAdaptor);
        if (dataAdaptor.size() > 0)
            findViewById(R.id.no_items).setVisibility(View.GONE);

        transactionList.setAdapter(adaptor);

        totalprice.setText("Total = " + totalAmt);

        monthlyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeAd();
            }
        });
        setFontStyles();
        adMOb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adMOb();
    }

    private void setFontStyles() {
        FontUtility.setCustomFont(monthlyView, FontConstants.FONT_ROBOT_LIGHT, this);
        FontUtility.setCustomFont(totalprice, FontConstants.FONT_ROBOT_LIGHT, this);
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
                //openMonthlyActivity();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                openMonthlyActivity();
            }
        });
    }

    private void invokeAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            openMonthlyActivity();
        }
    }

    private void openMonthlyActivity() {
        Intent monthlyVeiwIntent = new Intent(ViewActivity.this, MonthlyViewActivity.class);
        ViewActivity.this.startActivity(monthlyVeiwIntent);
    }

    class TransactionAdaptor extends BaseAdapter {

        Context mContext;
        ArrayList<Expense> mData;

        public TransactionAdaptor(Context context, ArrayList<Expense> data) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_list, null);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            TextView dated = (TextView) convertView.findViewById(R.id.dated);

            FontUtility.setCustomFont(title, FontConstants.FONT_ROBOT_MEDIUM, mContext);
            FontUtility.setCustomFont(description, FontConstants.FONT_ROBOT_LIGHT, mContext);
            FontUtility.setCustomFont(dated, FontConstants.FONT_ROBOT_LIGHT, mContext);

            Expense expense = mData.get(position);

            String titlString = expense.getTitle();
            int value = expense.getPrice();

            title.setText(titlString);
            description.setText(Integer.toString(value));
            dated.setText(expense.getDated());
            return convertView;
        }
    }
}