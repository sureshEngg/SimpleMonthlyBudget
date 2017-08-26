
package suresh.expensesimpletracking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import Constants.FontConstants;
import Utils.FontUtility;

public class ViewMonthExpenseActivity extends Activity {

    ListView transactionList;
    SQLiteDBUtil sqLiteDBUtil;
    TextView totalprice;
    TextView monthlyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlayout);

        totalprice = (TextView) findViewById(R.id.total);

        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());

        transactionList = (ListView) findViewById(R.id.transactionlist);

        monthlyView = (TextView) findViewById(R.id.monthly);
        monthlyView.setVisibility(View.GONE);

        String monthName = getIntent().getStringExtra("monthName");
        sqLiteDBUtil.OpenDB();
        ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from budget_table where month = '" + monthName + "'");
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
        MonthTransactionAdaptor adaptor = new MonthTransactionAdaptor(getApplicationContext(), dataAdaptor);
        if (dataAdaptor.size() > 0)
            findViewById(R.id.no_items).setVisibility(View.GONE);

        transactionList.setAdapter(adaptor);

        totalprice.setText("Total = " + totalAmt);


    }
}

class MonthTransactionAdaptor extends BaseAdapter {

    Context mContext;
    ArrayList<Expense> mData;

    public MonthTransactionAdaptor(Context context, ArrayList<Expense> data) {
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
        Expense expense = mData.get(position);

        String titlString = expense.getTitle();
        int value = expense.getPrice();

        title.setText(titlString);
        description.setText(Integer.toString(value));
        dated.setText(expense.getDated());

        FontUtility.setCustomFont(title, FontConstants.FONT_ROBOT_MEDIUM, mContext);
        FontUtility.setCustomFont(description, FontConstants.FONT_ROBOT_LIGHT, mContext);
        FontUtility.setCustomFont(dated, FontConstants.FONT_ROBOT_LIGHT, mContext);

        return convertView;
    }

}