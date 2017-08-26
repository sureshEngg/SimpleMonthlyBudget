package suresh.expensesimpletracking;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import Constants.FontConstants;
import Utils.FontUtility;

public class RequiredActivity extends Activity { 

	EditText enterRequired;
	Button addbtn;
	SQLiteDBUtil sqLiteDBUtil;
	ListView ReuiredListView;
	Button clearRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.required);

		clearRecords = (Button)findViewById(R.id.clearrequired);
		
		ReuiredListView = (ListView)findViewById(R.id.requiredlist);
		
		enterRequired = (EditText) findViewById(R.id.enterrequired);

		sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
		
		addbtn = (Button) findViewById(R.id.add);

		addbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (allOK()) {
					sqLiteDBUtil.OpenDB();
					ArrayList<String> data = new ArrayList<String>();
					String requiredThings = enterRequired.getText().toString();
					data.add(requiredThings);
					sqLiteDBUtil.insertIntoRequireTable(data);
					
					String []values = getData();
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequiredActivity.this, android.R.layout.simple_list_item_1, values);
					adapter.notifyDataSetChanged();
					ReuiredListView.setAdapter(adapter);
					
					sqLiteDBUtil.CloseDB();
				}
			}
		});
		
		String []values = getData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequiredActivity.this, android.R.layout.simple_list_item_1, values);
		ReuiredListView.setAdapter(adapter);
		
		
		clearRecords.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(RequiredActivity.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle("Alert");

		// Setting Dialog Message
		alertDialog.setMessage("Do you want to erase all earlier required things details?");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.alert_icon);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed

				sqLiteDBUtil.OpenDB();
				sqLiteDBUtil.getDataOnDemand(getApplicationContext(),
						"DELETE from required_table");
				sqLiteDBUtil.CloseDB();

				
				String []values = getData();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequiredActivity.this, android.R.layout.simple_list_item_1, values);
				adapter.notifyDataSetChanged();
				ReuiredListView.setAdapter(adapter);
				
				Toast.makeText(getApplicationContext(),
						"Cleared all data successfully!", Toast.LENGTH_LONG)
						.show();
				
				
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
			}
		});
		setFontStyles();
	}

	private void setFontStyles() {
		FontUtility.setCustomFont(enterRequired, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(addbtn, FontConstants.FONT_ROBOT_LIGHT, this);
	}



	private boolean allOK() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String[] getData()
	{
		sqLiteDBUtil.OpenDB();
		ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from required_table");
		sqLiteDBUtil.CloseDB();
		
		String[] values  = new String[data.size()];

		for (int i = 0; i < data.size(); i++) {
			values[i] = data.get(i)[1];
		}
		return values;
		
	}
}






/*
class RequiredAdaptor extends BaseAdapter{

	Context m_Context;
	ArrayList<String> mData;
	
	public RequiredAdaptor(Context context, ArrayList<String> data) {
		// TODO Auto-generated constructor stub
		m_Context = context;
		mData = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
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
		LayoutInflater vi = (LayoutInflater)m_Context.getSystemService(m_Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.requiredListIte, root)
		return null;
	}
	
}*/
