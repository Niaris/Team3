package com.team3.presentation;

import com.example.team3.R;
import com.team3.business.ReviewBusiness;
import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.LocationVO;
import com.team3.entities.ReviewVO;
import com.team3.utils.DateTimeManipulator;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/** Activity that allows the user to add a review to an location
 * @author Ellis Carvalho
 * @version 1.0
 */
public class CheckInActivity extends Activity {

	private MySQLConnection DBConnection;
	private LocationVO Location;
	private ReviewBusiness ReviewBUS;
	private int UserID;
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBConnection = new MySQLConnection();
		DBConnection.open();
		ReviewBUS = new ReviewBusiness(DBConnection);

		setContentView(R.layout.activity_check_in);
		getIntentContent();
	}

	/** Gets the content passed by the previous activity
	 * 	@return void
	 */
	private void getIntentContent() {
		Intent intent = getIntent();
		Location = (LocationVO) intent.getSerializableExtra("LocationVO");
		UserID = intent.getIntExtra("UserID", 0);
		TextView addressTV = (TextView) this.findViewById(R.id.Address);
		addressTV.setText(Location.getAddress());
	}

	/** Onclick method for the 'Save Review' button.
	 *  Adds a review to a location
	 * @param view
	 * @return void
	 */
	public void saveReview(View view) {
		try {
			ReviewBUS.addReviewToLocation(createReviewVO(), Location);
		} catch (Exception e) {
			toastExceptionMessage(e);
		}
	}
	
	/** Onclick method for the 'Browse' button
	 * 	Takes the user to the phone gallery and allows him to choose an image 
	 *  and upload it.
	 * 
	 * @param view
	 * @return void
	 */
	public void browseImage(View view) {
		Intent i = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) this.findViewById(R.id.imageView1);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            
            TextView imagePath = (TextView) this.findViewById(R.id.imagePathArea);
            imagePath.setText(picturePath);
        }
     
    }
	
	/** Creates a ReviewVO based on the fields filled by the user and
	 *  on the other information from the application.
	 * 
	 * @return created ReviewVO
	 */
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

	/** Shows the Exception message as an Android Toast message.
	 * 
	 * @param exception
	 */
	private void toastExceptionMessage(Exception exception) {
		Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
}
