package suresh.expensesimpletracking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

public class ViewIndividualDetails extends Activity {
	
	ListView transactionList;
	SQLiteDBUtil sqLiteDBUtil; 
	Button totalprice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewexpensepage);

		totalprice = (Button)findViewById(R.id.total);
		
		sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
		
		transactionList = (ListView)findViewById(R.id.transactionlist);
		
		String memberId = this.getIntent().getStringExtra("member_id");
		
		sqLiteDBUtil.OpenDB();
		ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from trip_budget_table where member_id =\""+memberId+"\"");
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
		
		//data = getDemoData();
		TransactionAdaptor adaptor = new TransactionAdaptor(getApplicationContext(),dataAdaptor);
		transactionList.setAdapter(adaptor);
		
		totalprice.setText("Total = " + totalAmt);
		setFontStyles();
	}

	private void setFontStyles() {
		FontUtility.setCustomFont(totalprice, FontConstants.FONT_ROBOT_LIGHT, this);
	}


class TransactionAdaptor extends BaseAdapter{

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
		LayoutInflater vi = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.item_trip_list, null);
		
		TextView title = (TextView)convertView.findViewById(R.id.title);
		TextView description = (TextView)convertView.findViewById(R.id.description);
		TextView name = (TextView)convertView.findViewById(R.id.member);
		TextView dated = (TextView)convertView.findViewById(R.id.dated);
		Expense expense = mData.get(position);
		
		String titlString = expense.getTitle();
		int value = expense.getPrice();
		
		title.setText(titlString);
		description.setText(Integer.toString(value));
		dated.setText(expense.getDated());
		
		
		name.setText("");
	
		
		return convertView;
	}
}
}
