package suresh.expensesimpletracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import Constants.FontConstants;
import Utils.FontUtility;

public class AddExpenseActivity extends Activity implements OnClickListener{
	
	EditText addTitle, addPrice;
	Spinner member;
	Button addButton;
	SQLiteDBUtil sqLiteDBUtil;
	private InterstitialAd mInterstitialAd;
	boolean expenseCount = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtripexpensepage);
		sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());

		addTitle = (EditText) findViewById(R.id.addexpensestext);
		addPrice = (EditText) findViewById(R.id.addprice);
		addButton = (Button) findViewById(R.id.add);

		member = (Spinner)findViewById(R.id.member_name);
		
		sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
		sqLiteDBUtil.OpenDB();
		ArrayList<String[]> data = sqLiteDBUtil.getDataOnDemand(getApplicationContext(), "Select * from member_table");
		sqLiteDBUtil.CloseDB();

		if(data==null || data.size()==0){
			showAlert();
		}
		memberType[] memberTypes = new memberType[data.size()];
		for (int i = 0; i < data.size(); i++) {
			String[] memberData = data.get(i);
			memberType m = new memberType(Integer.parseInt(memberData[0]), memberData[1]);
			memberTypes[i] = m;
		}

		ArrayAdapter spinnerAdaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, memberTypes );
		
		member.setAdapter(spinnerAdaptor);
		
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void showAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Please add a member in this trip from previous screen!")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
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
			
			if(AllDataOK())
			{
				ArrayList<Object> data = new ArrayList<Object>();
				
				data.add(Integer.toString(((memberType)member.getItemAtPosition((int) member.getSelectedItemId())).getId()));
				data.add(member.getSelectedItem().toString());
				
				data.add(addTitle.getText().toString());
				data.add(addPrice.getText().toString());
				data.add(getCurrDate());
				
				sqLiteDBUtil.insertIntoTripBudget(data);
				
				
				Toast.makeText(AddExpenseActivity.this, "Submitted", Toast.LENGTH_LONG).show();
				
				addTitle.setText("");
				addPrice.setText("");
			}
			
			sqLiteDBUtil.CloseDB();
			if(expenseCount) {
				invokeAd();
				expenseCount = false;
			}else{
				expenseCount = true;
			}

			break;

		default:
			break;
		}
	}

	private boolean AllDataOK() {
		// TODO Auto-generated method stub
		if(member.getSelectedItem().toString().equals("Select Member"))
		{
			Toast.makeText(this, "Please select a member", Toast.LENGTH_LONG).show();
			return false;
		}

		if (addTitle.getText().toString().trim().length()==0){
			Toast.makeText(this, "Please enter a expense-title", Toast.LENGTH_LONG).show();
			return false;
		}

		if (addPrice.getText().toString().trim().length()==0){
			Toast.makeText(this, "Please enter expense-price", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	public String getCurrDate()
	{
	    String dt;
	    Date cal = Calendar.getInstance().getTime();
	    dt = cal.toLocaleString();
	    return dt;
	}
}
