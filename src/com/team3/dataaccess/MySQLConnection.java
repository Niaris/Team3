package com.team3.dataaccess;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.team3.entities.LocationVO;
import com.team3.entities.ReviewVO;
import com.team3.presentation.Login;

public class MySQLConnection {

	@SuppressWarnings("unused")
	private String DBConnection;
	@SuppressWarnings("unused")
	private int serverResponseCode;
	JSONParser jsonParser;
	private static String SERVER_URL = "http://54.246.220.68/";
	private static String ADD_USER_PHP = "AddUsers.php";

	public MySQLConnection() {
		try {
			jsonParser = new JSONParser();
			connectToServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectToServer() throws Exception {
		HttpURLConnection connection = null;
		String DBConnection = "http://54.246.220.68/config.inc.php";
		String boundary = "*****";
		try {
			URL url = new URL(DBConnection);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			serverResponseCode = connection.getResponseCode();
		} catch (Exception e) {
			Log.e("Upload File", " " + e.getMessage().toString());
			// Exception handling
		}
	}

	public void registerLocation(LocationVO location) {
		// TODO Auto-generated method stub

	}

	public List<LocationVO> retrieveLocationsByUserPosition(double latitude,
			double longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addReview(ReviewVO review) {
		// TODO Auto-generated method stub

	}

	public void AddUser(Login UserDetails) {

	}
	
	public void RegisterUser(String email, String name) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("emailAddress", email));

		JSONObject json = jsonParser.makeHttpRequest(SERVER_URL + ADD_USER_PHP, "POST",
				params);

		Log.d("Create Response", json.toString());

		try {
			int success = json.getInt("success");
			String message = json.getString("message");
			Log.d("USER", message);
			if (success == 1) {
				Log.d("USER", "YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEY");
			} else {
				// failed to create product
			}
		} catch (JSONException e) {
			Log.e("USER", e.getMessage());
		}
	}

	public List<ReviewVO> retrieveReviewsList(int locationID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}
}