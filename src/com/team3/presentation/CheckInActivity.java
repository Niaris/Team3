package com.team3.presentation;

import com.example.team3.R;
import com.team3.dataaccess.MySQLConnection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CheckInActivity extends Activity {

	private MySQLConnection DBConnection;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBConnection = new MySQLConnection();
		DBConnection.open();
		setContentView(R.layout.activity_check_in);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_in, menu);
		return true;
	}

}
