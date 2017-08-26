package suresh.expensesimpletracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class ViewExpenseIndividualActivity extends Activity {

    ListView transactionList;
    SQLiteDBUtil sqLiteDBUtil;
    Button totalprice;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewindividualexpense);


        totalprice = (Button) findViewById(R.id.total);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());

        transactionList = (ListView) findViewById(R.id.transactionlist);

        sqLiteDBUtil.OpenDB();
        ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from trip_budget_table");
        sqLiteDBUtil.CloseDB();

        ArrayList<Expense> dataAdaptor = new ArrayList<Expense>();
        int totalAmt = 0;
        for (int i = 0; i < data.size(); i++) {
            String[] arrData = data.get(i);

            Expense expense = new Expense();

            expense.setMemberId(arrData[1]);
            expense.setMemberName(arrData[2]);

            expense.setTitle(arrData[3]);
            expense.setPrice(Integer.parseInt(arrData[4]));
            totalAmt = totalAmt + Integer.parseInt(arrData[4]);
            expense.setDated(arrData[5]);
            dataAdaptor.add(expense);
        }

        HashMap<String, String[]> membersData = getOrderBymembers(dataAdaptor);

        ArrayList<Member> members = new ArrayList<Member>();
        @SuppressWarnings("rawtypes")
        Iterator it = membersData.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            Member member = new Member();
            member.setId((String) pairs.getKey());

            String dataArray[] = (String[]) pairs.getValue();
            member.setName(dataArray[0]);
            member.setAmount(dataArray[1]);

            members.add(member);
            it.remove(); // avoids a ConcurrentModificationException
        }


        //data = getDemoData();
        TransactionAdaptor adaptor = new TransactionAdaptor(getApplicationContext(), members);
        transactionList.setAdapter(adaptor);

        totalprice.setText("Total = " + totalAmt);
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
        invokeAd();
    }

    private void invokeAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void setFontStyles() {
        FontUtility.setCustomFont(totalprice, FontConstants.FONT_ROBOT_LIGHT, this);
    }

    private HashMap<String, String[]> getOrderBymembers(ArrayList<Expense> dataAdaptor) {
        // TODO Auto-generated method stub
        HashMap<String, Integer> members = new HashMap<String, Integer>();
        HashMap<String, String[]> membersData = new HashMap<String, String[]>();

        for (int i = 0; i < dataAdaptor.size(); i++) {
            Expense expense = dataAdaptor.get(i);
            //HashMap<String, Integer> map = new HashMap<String, Integer>();

            if (members.containsKey(expense.getMemberId())) {
                int total = members.get(expense.getMemberId());
                total = total + expense.getPrice();
                members.put(expense.getMemberId(), total);

                String[] data = new String[2];
                data[0] = expense.getMemberName();
                data[1] = Integer.toString(total);
                membersData.put(expense.getMemberId(), data);
            } else {
                members.put(expense.getMemberId(), expense.getPrice());
                String[] data = new String[2];
                data[0] = expense.getMemberName();
                data[1] = Integer.toString(expense.getPrice());
                membersData.put(expense.getMemberId(), data);
            }

            //map.put(expense.getMemberId(), expense.getPrice());
        }

        return membersData;
    }


    class TransactionAdaptor extends BaseAdapter {

        Context mContext;
        ArrayList<Member> mData;

        public TransactionAdaptor(Context context, ArrayList<Member> data) {
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
            convertView = vi.inflate(R.layout.item_trip_list, null);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView description = (TextView) convertView.findViewById(R.id.description);


            final Member member = mData.get(position);

            String titlString = member.getName();
            String value = member.getAmount();

            title.setText(titlString);
            description.setText(value);

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   openViewIndividualActivity(member);
                }
            });

            return convertView;
        }

        private void openViewIndividualActivity(Member member) {
            Intent individualDetail = new Intent(ViewExpenseIndividualActivity.this, ViewIndividualDetails.class);
            individualDetail.putExtra("member_id", member.getId());
            ViewExpenseIndividualActivity.this.startActivity(individualDetail);
        }
    }
}
