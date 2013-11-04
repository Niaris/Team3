package com.team3.presentation;

import com.example.team3.R;
import com.team3.business.ReviewBusiness;
import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.LocationVO;
import com.team3.entities.ReviewVO;
import com.team3.utils.DateTimeManipulator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class CheckInActivity extends Activity {

	private MySQLConnection DBConnection;
	private LocationVO Location;
	private ReviewBusiness ReviewBUS;
	private int UserID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBConnection = new MySQLConnection();
		DBConnection.open();
		ReviewBUS = new ReviewBusiness(DBConnection);

		setContentView(R.layout.activity_check_in);
		getIntentContent();
	}

	private void getIntentContent() {
		Intent intent = getIntent();
		Location = (LocationVO) intent.getSerializableExtra("LocationVO");
		UserID = intent.getIntExtra("UserID", 0);
		TextView addressTV = (TextView) this.findViewById(R.id.Address);
		addressTV.setText(Location.getAddress());
		
		// TODO GET USER ID
	}

	public void saveReview(View view) {
		ReviewBUS.addReviewToLocation(createReviewVO(), Location);
	}
	
	private ReviewVO createReviewVO(){
		RatingBar ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);
		int rating = Math.round(ratingBar.getRating());
		
		String date = DateTimeManipulator.getCurrentDate();
		String time = DateTimeManipulator.getCurrentTime();
		
		TextView commentArea = (TextView) this.findViewById(R.id.commentArea);
		String comment = commentArea.getText().toString();
		TextView imagePathArea = (TextView) this.findViewById(R.id.imagePathArea);
		String imagePath = imagePathArea.getText().toString();
		
		return new ReviewVO(UserID, Location.getID(), rating, date, time, comment, imagePath);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_in, menu);
		return true;
	}

}
