package suresh.expensesimpletracking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Constants.FontConstants;
import Utils.FontUtility;

public class AddMemberActivity extends Activity implements OnClickListener{
	EditText name, contact;
	Button add;
	SQLiteDBUtil sqLiteDBUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_member);
		
		name = (EditText)findViewById(R.id.member_name);
		contact = (EditText)findViewById(R.id.member_contact);
		
		add = (Button)findViewById(R.id.add);
		add.setOnClickListener(this);
		
		sqLiteDBUtil = new SQLiteDBUtil(getApplicationContext());
		setFontStyles();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add:
			if(allOk())
			{
				ArrayList<Object> data = new ArrayList<Object>();
				
				Calendar c = Calendar.getInstance();
				
				 
				String timeStamp = Long.toString(c.getTimeInMillis());
				
				sqLiteDBUtil.OpenDB();
				
				data.add(timeStamp.substring(8, timeStamp.length()-1));
				data.add(name.getText().toString());
				data.add(contact.getText().toString());
				data.add("");
				
				sqLiteDBUtil.insertIntoMemberTable(data);
				sqLiteDBUtil.CloseDB();
				
				Toast.makeText(AddMemberActivity.this, "Member Added successfully", Toast.LENGTH_LONG).show();
				name.setText("");
				contact.setText("");
			}
			
			break;

		default:
			break;
		}
	}

	private void setFontStyles() {
		FontUtility.setCustomFont(name, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(contact, FontConstants.FONT_ROBOT_LIGHT, this);
		FontUtility.setCustomFont(add, FontConstants.FONT_ROBOT_LIGHT, this);
	}

	private boolean allOk() {
		// TODO Auto-generated method stub
		if(name.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, "Please enter member-name!", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}
