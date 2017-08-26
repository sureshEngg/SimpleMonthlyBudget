package suresh.expensesimpletracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import Constants.FontConstants;
import Utils.FontUtility;

/**
 * Created by ${Sureshsharma} on 8/3/2016.
 */
public class MonthlyViewActivity extends Activity {
    private ListView monthlyListView;
    private ArrayList<MonthView> monthList;
    SQLiteDBUtil sqLiteDBUtil;
    ;
    private static final String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_view);
        monthlyListView = (ListView) findViewById(R.id.monthly_listView);
        sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
        monthList = getMonthWiseExpenseView();

        MonthlyViewAdaptor monthlyViewAdaptor = new MonthlyViewAdaptor(this, monthList);
        monthlyListView.setAdapter(monthlyViewAdaptor);

        monthlyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MonthView monthView = monthList.get(i);
                Intent viewMonthExpense = new Intent(MonthlyViewActivity.this, ViewMonthExpenseActivity.class);
                viewMonthExpense.putExtra("monthName", monthView.getMonthValue());
                MonthlyViewActivity.this.startActivity(viewMonthExpense);
            }
        });
    }

    public ArrayList<MonthView> getMonthWiseExpenseView() {
        sqLiteDBUtil.OpenDB();
        ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select sum(cast(price as integer)), month from budget_table group by month");
        sqLiteDBUtil.CloseDB();

        ArrayList<MonthView> monthWiseExpenseView = new ArrayList<MonthView>();
        for (int i = 0; i < data.size(); i++) {
            String[] arrData = data.get(i);

            MonthView month = new MonthView();
            month.setTotal(arrData[0]);
            month.setMonthTitle(arrData[1] == null || arrData[1].equals("null")?"Older":arrData[1]);
            month.setMonthValue(arrData[1]);
            monthWiseExpenseView.add(month);
        }
        return monthWiseExpenseView;
    }

    class MonthlyViewAdaptor extends BaseAdapter {
        Context mContext;
        LayoutInflater li;
        private ArrayList<MonthView> months;

        MonthlyViewAdaptor(Context context, ArrayList<MonthView> monthList) {
            mContext = context;
            months = monthList;
            li = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return months.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = li.inflate(R.layout.item_month_view, null);
                TextView monthTitle;
                TextView monthTotal;
                monthTitle = (TextView) view.findViewById(R.id.month_text_title);
                monthTotal = (TextView) view.findViewById(R.id.month_text_total);
                MonthView monthView = months.get(i);
                monthTitle.setText(monthView.getMonthTitle());
                monthTotal.setText("Total: " + monthView.getTotal());

                FontUtility.setCustomFont(monthTitle, FontConstants.FONT_ROBOT_MEDIUM, mContext);
                FontUtility.setCustomFont(monthTotal, FontConstants.FONT_ROBOT_LIGHT, mContext);
            }

            return view;
        }
    }
}
