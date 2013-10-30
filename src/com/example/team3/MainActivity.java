/** 
 * FR1.
 * MainActivity.java is used to show the user's current location on a map. 
 * @author Andreas Stavrou (Initial coding, application works but does not display marker)
 * @author Ellis De Vasconcelos Carvalho (Added Marker on the map)
 * @author Andreas Stavrou (Refactored and Commented)
 * @version 1.0 - Finished 6 October 2013 | Refactored FINISHED on 16 October 2013 
 */
/** 
 * FR2.
 * MainActivity.java now saves the user's location and also uploads it to the web server.
 * @author Andreas Stavrou
 * @author Sijia Liu
 * @author Ahmad Rufai
 * @author Salman Alghmdi
 * @version 1.1 - Finished 28 October 2013 | Refactored FINISHED on 29 October 2013
 * @author Refactor made by the whole team.
 */

package com.example.team3;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The public class must extend and implement the following in order for it to
 * be able to work. I.e. display the map, get coordinates and use the
 * GooglePlayServicesClient on call backs and on connection failed listeners.
 */
public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	public AddressConversion Addr;
	private static final int GPS_ERRORDIALOG_REQUEST = 9000;
	@SuppressWarnings("unused")
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
	public GoogleMap Team3Map;
	private static final float DEFAULTZOOM = 15;
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Maps";
	public LocationClient mLocationClient;
	public Marker marker;
	public double LONGITUDE;
	public double LATITUDE;
	public String deviceId;
	public String upLoadServerUri = null;
	public int serverResponseCode = 0;

	/**
	 * Method onCreate is used when the page first loads. It will use the method
	 * servicesOK which will determine IF the user's device has up-to-date
	 * google services and if it has them at all, and if they are okay it will
	 * display the activity_map. If the initial Map - method initMap - is not
	 * available then a Toast Message will be displayed. If it is it will
	 * connect to LocationClient.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Addr = new AddressConversion();
		if (servicesOK()) {
			setContentView(R.layout.activity_map);

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			if (initMap()) {
				mLocationClient = new LocationClient(this, this, this);
				mLocationClient.connect();
			} else {
				Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			setContentView(R.layout.activity_main);
		}

	}// Ends onCreate

	/**
	 * Method onCreateOptionsMenu inflates the menu. This adds items to the
	 * action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}// Ends onCreateOptionsMenu

	/**
	 * Method servicesOK as described on method onCreate, will check the user's
	 * device if Google Services are available. If yes it will return true. If
	 * not but it is user recoverable error (like out of date) it will show the
	 * error dialog and try, for this instance to update the services. If is not
	 * available it will show an error toast message.
	 * 
	 */
	public boolean servicesOK() {
		int isAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
					this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "Can't connect to Google Play services",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}// Ends servicesOK

	/**
	 * Method initMap initialises the map fragment from the activity_map.xml. In
	 * other words it fill the map fragment with a map.
	 */
	private boolean initMap() {
		if (Team3Map == null) {
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			Team3Map = mapFrag.getMap();

		}
		return (Team3Map != null);
	}// Ends initMap

	/**
	 * Method onOptionsItemSelected changes the map to Normal, Satellite,
	 * Terrain, Hybrid and None. Settings are embedded here for future use if
	 * required.
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LinearLayout linearLayout = (LinearLayout) this
				.findViewById(R.id.LinearLayout1);
		switch (item.getItemId()) {
		case R.id.mapTypeNone:
			Team3Map.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		case R.id.mapTypeNormal:
			Team3Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			setTextViewColor(Color.BLACK);
			linearLayout.setBackgroundColor(Color.parseColor("#22000000"));
			break;
		case R.id.mapTypeSatellite:
			Team3Map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			setTextViewColor(Color.WHITE);
			linearLayout.setBackgroundColor(Color.parseColor("#22FFFFFF"));
			break;
		case R.id.mapTypeTerrain:
			Team3Map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			setTextViewColor(Color.BLACK);
			linearLayout.setBackgroundColor(Color.parseColor("#22000000"));
			break;
		case R.id.mapTypeHybrid:
			Team3Map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			setTextViewColor(Color.WHITE);
			linearLayout.setBackgroundColor(Color.parseColor("#22FFFFFF"));
			break;
		case R.id.gotoCurrentLocation:
			gotoCurrentLocation();
			break;
		default:
			break;
		}// Ends Switch

		return super.onOptionsItemSelected(item);
	}// End onOptionsItemSelected

	private void setTextViewColor(int color) {
		TextView tvLat = (TextView) this.findViewById(R.id.txLat);
		TextView tvLon = (TextView) this.findViewById(R.id.txLon);
		TextView tvTime = (TextView) this.findViewById(R.id.txTime);
		TextView tvAddress = (TextView) this.findViewById(R.id.txAddress);
		tvLon.setTextColor(color);
		tvLat.setTextColor(color);
		tvTime.setTextColor(color);
		tvAddress.setTextColor(color);
	}

	/**
	 * Method onStop saves the map when the application stops to a
	 * MapStateManager imported from MapStateManager.java
	 */
	@Override
	protected void onStop() {
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(Team3Map);
	}// End onStop

	/**
	 * Method onResume resumes the map when the application re-opens to where it
	 * was left and updates the camera position. It also restores the map type
	 * that was previously selected
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if (position != null) {
			CameraUpdate update = CameraUpdateFactory
					.newCameraPosition(position);
			Team3Map.moveCamera(update);

			Team3Map.setMapType(mgr.getSavedMapType());
		}

		if (mLocationClient.isConnected()) {
			requestLocationUpdates();
		}
	}// Ends onResume

	/*
	 * Method gotoCurrentLocation is used to take the user back to his/hers
	 * current location. Since the user may move away from the current location
	 * (For instance, move the map around), to go back to the current location
	 * the user can press menu item defined as action under menu--> main.xml.
	 * This way the "button" is shown always left of the three dots and the user
	 * can go back to the current location. If the current location is not
	 * available a toast message will appear informing the user that it is not
	 * available. If it is it will take him/her back and it will reset the zoom
	 * etc.
	 */
	protected void gotoCurrentLocation() {
		Location currentLocation = mLocationClient.getLastLocation();
		if (currentLocation == null) {
			Toast.makeText(this, "Current location isn't available",
					Toast.LENGTH_SHORT).show();
		} else {
			if (marker != null) {
				marker.remove();
			}

			LatLng ll = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			marker = Team3Map.addMarker(new MarkerOptions().position(ll).title(
					"You are here"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,
					DEFAULTZOOM);
			Team3Map.animateCamera(update);
		}
	}// Ends gotoCurrentLocation

	/**
	 * Method onConnectionFailed is used for when the connection fails to
	 * display a toast message to inform the user.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed. Please try again",
				Toast.LENGTH_SHORT).show();
		requestLocationUpdates();

	}// Ends onConnectionFailed

	/**
	 * Method onConnected is used for when the connection succeeds to display a
	 * toast message to inform the user.
	 */
	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "Connected Successfully", Toast.LENGTH_SHORT)
				.show();
		requestLocationUpdates();
	}// Ends onConnected

	@Override
	public void onDisconnected() {

	}// Ends onDisconnected

	/**
	 * Method requestLocationUpdates is used to update the location of the user
	 * automatically ever 1 minute (60000 milliseconds in this) In other words
	 * it refreshes the location and shows the new coordinates.
	 */
	private void requestLocationUpdates() {
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(900000);
		request.setFastestInterval(900000);
		mLocationClient.requestLocationUpdates(request, this);
	}// Ends requestLocationUpdates

	@Override
	public void onLocationChanged(Location loc) {
		// Toast.makeText(this, "Location: " + loc.getLatitude() + "," +
		// loc.getLongitude(),
		// Toast.LENGTH_SHORT).show();

		// SHOWS THE DATE/TIME (CALENDAR)
		Calendar team3Calendar = Calendar.getInstance();
		System.out.println("Current time => " + team3Calendar.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String Date = dateFormat.format(team3Calendar.getTime());
		String Time = timeFormat.format(team3Calendar.getTime());
		// int serverResponseCode = 0;
		// Declared the Latitude and Longitude
		double LAT = loc.getLatitude();
		double LONG = loc.getLongitude();

		// Remove and Add Map Marker
		LatLng ll = new LatLng(LAT, LONG);
		if (marker == null) {
			marker = Team3Map.addMarker(new MarkerOptions().position(ll).title(
					"You are here"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,
					DEFAULTZOOM);
			Team3Map.animateCamera(update);
		} else {
			marker.remove();
			marker = Team3Map.addMarker(new MarkerOptions().position(ll).title(
					"You are here"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,
					DEFAULTZOOM);
			Team3Map.animateCamera(update);
		}// End Remove and Add Marker

		// Text View for showing Latitude
		TextView tvLat = (TextView) this.findViewById(R.id.txLat); // NEW ONE
		tvLat.setText("Latitude: " + String.valueOf(LAT));

		// Text View for showing Longitude
		TextView tvLot = (TextView) this.findViewById(R.id.txLon);
		tvLot.setText("Longitude: " + String.valueOf(LONG));

		// Text View for showing Date and Time
		TextView tvDateTime = (TextView) this.findViewById(R.id.txTime);
		tvDateTime.setText("Date/Time: " + Date + "," + " " + Time);

		// Text View for showing the Address
		TextView tvAddress = (TextView) this.findViewById(R.id.txAddress);

		// This gets the Address of the current location through
		// AddressConversion.java using Json.
		JSONObject ret = Addr.getLocationInfo(LAT, LONG);
		JSONObject location;
		String location_string;
		try {
			location = ret.getJSONArray("results").getJSONObject(0);
			location_string = location.getString("formatted_address");
			Log.d("test", "formattted address:" + location_string);

			tvAddress.setText(location_string);

			String Address = String.valueOf(location_string);

			// This gives the device a UNIQUE ID (NOTE: Try to add this to
			// another Class).
			String deviceId = Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);

			// Calls generateXML method in order to save the details to an XML
			// file
			generateXML(loc, Date, Time, Address, deviceId);

			// Calls uploadToServer in order to upload the xml file to the
			// server.
			uploadToServer(Date, Time, deviceId);

		} catch (JSONException e1) {
			Toast.makeText(this, "Error: " + e1, Toast.LENGTH_SHORT).show();
			e1.printStackTrace();

		}

	}// Ends onLocationChanged

	public void generateXML(Location loc, String Date, String Time,
			String Address, String deviceId) {
		File xmlfile = new File(Environment.getExternalStorageDirectory()
				+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId + ".xml");
		Log.d("FILE PATH", "path=" + xmlfile);
		try {
			xmlfile.createNewFile();
		} catch (IOException e) {
			Toast.makeText(this,
					"An error has occured. Restart the Application.",
					Toast.LENGTH_SHORT).show();
			Log.e("IOException", "exception in createNewFile() method");
		}
		// we have to bind the new file with a FileOutputStream
		FileOutputStream fileOutStr = null;
		try {
			fileOutStr = new FileOutputStream(xmlfile);
		} catch (FileNotFoundException e) {
			Toast.makeText(this,
					"An error has occured. Restart the Application.",
					Toast.LENGTH_SHORT).show();
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}
		// we create a XmlSerializer in order to write xml data
		XmlSerializer serializer = Xml.newSerializer();
		try {
			// we set the FileOutputStream as output for the serializer, using
			// UTF-8 encoding
			serializer.setOutput(fileOutStr, "UTF-8");
			// Write <?xml declaration with encoding (if encoding not null) and
			// standalone flag (if standalone not null)
			serializer.startDocument(null, Boolean.valueOf(true));
			// set indentation option
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);

			serializer.startTag(null, "Location");// ROOT

			serializer.startTag(null, "Coordinates");// Child 1

			serializer.startTag(null, "Latitute");
			serializer.text("" + loc.getLatitude());
			serializer.endTag(null, "Latitute");

			serializer.startTag(null, "Longitude");
			serializer.text("" + loc.getLongitude());
			serializer.endTag(null, "Longitude");

			serializer.endTag(null, "Coordinates");

			serializer.startTag(null, "DateTime");// Child 2

			serializer.startTag(null, "Date");
			serializer.text(Date);
			serializer.endTag(null, "Date");

			serializer.startTag(null, "Time");
			serializer.text(Time);
			serializer.endTag(null, "Time");

			serializer.endTag(null, "DateTime"); // Child 3

			serializer.startTag(null, "Address");
			serializer.text(Address);
			serializer.endTag(null, "Address");

			serializer.endTag(null, "Location");
			serializer.endDocument();
			// write xml data into the FileOutputStream
			serializer.flush();
			// finally we close the file stream
			fileOutStr.close();
			Toast.makeText(this, "File has been created.", Toast.LENGTH_SHORT)
					.show();

		} catch (Exception e) {
			Toast.makeText(this, "An error has occured. Restart the app.",
					Toast.LENGTH_SHORT).show();
			Log.e("Exception", "error occurred while creating xml file");
		}
	}// Ends generateXML

	public void uploadToServer(String Date, String Time, String deviceId) {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String pathToOurFile = Environment.getExternalStorageDirectory()
				+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId + ".xml";
		String urlServer = "http://54.246.220.68/upload1.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		// String serverResponseMessage = null;

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = connection.getResponseCode();
			// serverResponseMessage = connection.getResponseMessage();
			Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show();

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId
					+ ".xml");
			file.delete();
			Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {
			Toast.makeText(this, "An error has occured" + ex,
					Toast.LENGTH_SHORT).show();
			// Exception handling
		}
	}// Ends uploadToServer

	@Override
	protected void onPause() {
		super.onPause();
		mLocationClient.removeLocationUpdates(this);
	}// Ends onPause

}// Ends MainActivity
