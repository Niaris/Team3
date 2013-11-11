package com.team3.presentation;

import java.util.HashMap;
import java.util.List;

import com.team3.R;
import com.team3.business.ReviewBusiness;
import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.LocationVO;
import com.team3.entities.ReviewVO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewsActivity extends Activity {

	private MySQLConnection DBConnection;
	private LocationVO Location;
	private int UserID;
	private ReviewBusiness ReviewBUS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBConnection = new MySQLConnection();
		DBConnection.open();
		ReviewBUS = new ReviewBusiness(DBConnection);
		setContentView(R.layout.activity_reviews);
		getIntentContent();
		loadLocationAndReviews();
	}

	private void loadLocationAndReviews() {
		setTitle("Reviews of " + Location.getName());
		TextView locationAddressTV = (TextView) findViewById(R.id.locationAddress);
		ListView reviewsListView = (ListView) findViewById(R.id.reviewsList);
		locationAddressTV.setText(Location.getAddress());
		List<ReviewVO> reviews = ReviewBUS.retrieveReviewsList(Location.getID());
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
			        android.R.layout.simple_list_item_1, reviews);
	    reviewsListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reviews, menu);
		return true;
	}
	
	/** Gets the content passed by the previous activity
	 * 	@return void
	 */
	private void getIntentContent() {
		Intent intent = getIntent();
		Location = (LocationVO) intent.getSerializableExtra("LocationVO");
		UserID = intent.getIntExtra("UserID", 0);
	}
	
	private class StableArrayAdapter extends ArrayAdapter<ReviewVO> {

	    HashMap<ReviewVO, Integer> mIdMap = new HashMap<ReviewVO, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<ReviewVO> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      ReviewVO item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = (LayoutInflater) getBaseContext()
	    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	    View rowView = inflater.inflate(R.layout.review_item, parent, false);
	    	    TextView reviewUserTV = (TextView) rowView.findViewById(R.id.reviewUser);
	    	    reviewUserTV.setText(getItem(position).getUser().getFirstName() + getItem(position).getUser().getLastName());
	    	    TextView reviewDateTV = (TextView) rowView.findViewById(R.id.reviewDate);
	    	    reviewDateTV.setText(getItem(position).getDate());
	    	    TextView reviewCommentTV = (TextView) rowView.findViewById(R.id.reviewComment);
	    	    reviewCommentTV.setText(getItem(position).getComment());
	    	    return rowView;
	    }

	  }

}
