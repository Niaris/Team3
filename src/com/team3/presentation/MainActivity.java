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

package com.team3.presentation;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team3.R;
import com.team3.business.LocationBusiness;
import com.team3.dataaccess.MySQLConnection;
import com.team3.dataaccess.UploadFiletoServer;
import com.team3.dataaccess.XMLGenerator;
import com.team3.entities.LocationVO;
import com.team3.utils.AddressConversion;
import com.team3.utils.DateTimeManipulator;
import com.team3.utils.MapStateManager;

/**
 * The public class must extend and implement the following in order for it to
 * be able to work. I.e. display the map, get coordinates and use the
 * GooglePlayServicesClient on call backs and on connection failed listeners.
 */
public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private static final int GPS_ERRORDIALOG_REQUEST = 9000;
	public GoogleMap Team3Map;
	private static final float DEFAULTZOOM = 15;
	public LocationClient mLocationClient;
	public Marker marker;
	private XMLGenerator xmlGenerator;
	private UploadFiletoServer fileUploader;
	private MySQLConnection DBConnection;
	private LocationVO CurrentLocation;
	private LocationBusiness locationBUS;
	private HashMap<Marker, LocationVO> markerLocationMap;
	private int UserID = 1; // TODO get UserID from logged user

	/**
	 * Method onCreate is used when the page first loads. It will use the method
	 * servicesOK which will determine IF the user's device has up-to-date
	 * google services and if it has them at all, and if they are okay it will
	 * display the activity_map. If the initial Map - method initMap - is not
	 * available then a Toast Message will be displayed. If it is it will
	 * connect to LocationClient.
	 * 
	 * @param Bundle
	 *            savedInstanceState Helps to retrieve all values and data of
	 *            that associated activity when it gets paused.
	 * @return void Returns a void object.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBConnection = new MySQLConnection();
		locationBUS = new LocationBusiness(DBConnection);
		markerLocationMap = new HashMap<Marker, LocationVO>();

		xmlGenerator = new XMLGenerator();
		fileUploader = new UploadFiletoServer();
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
		
		Intent intent = getIntent();
		String userEmail = intent.getStringExtra("UserEmail");
		Toast.makeText(this, "Logged user email is " + userEmail, Toast.LENGTH_LONG).show();
	}// Ends onCreate

	private void loadRegisteredLocations() {
		List<LocationVO> locList = locationBUS.retrieveLocationsByUserPosition(
				CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
		markerLocationMap.clear();
		for (LocationVO loc : locList) {
			Marker tempMarker = addRedMarker(loc.getLatitude(),
					loc.getLongitude(), loc.getAddress());
			markerLocationMap.put(tempMarker, loc);
		}
	}

	private Marker addRedMarker(double latitude, double longitude,
			String address) {
		LatLng ll = new LatLng(latitude, longitude);
		return Team3Map.addMarker(new MarkerOptions()
				.position(ll)
				.title(address)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	}

	/**
	 * Method onCreateOptionsMenu inflates the menu. This adds items to the
	 * action bar if it is present.
	 * 
	 * @param Menu
	 *            menu Object which holds actions and options.
	 * @return boolean Returns True to display menu, False to not display menu.
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
	 * @return boolean Return True to indicate Google Service is available and
	 *         updated, False to indicate Google Service neither available nor
	 *         updated.
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
	 * Method initMap initializes the map fragment from the activity_map.xml. In
	 * other words it fill the map fragment with a map.
	 * 
	 * @return boolean Returns True if Map is initialized, False if it is not.
	 */
	private boolean initMap() {
		if (Team3Map == null) {
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			Team3Map = mapFrag.getMap();
			Team3Map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					LocationVO loc = markerLocationMap.get(marker);
					if (loc != null) {
						Toast.makeText(getBaseContext(), loc.getAddress(),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getBaseContext(),
								ReviewsActivity.class);
						intent.putExtra("LocationVO", loc);
						intent.putExtra("UserID", UserID);
						startActivity(intent);
					}
					// TODO pass to the new screen to show the reviews!
					return false;
				}
			});

		}
		return (Team3Map != null);
	}// Ends initMap

	/**
	 * Method onOptionsItemSelected changes the map to Normal, Satellite,
	 * Terrain, Hybrid and None. Settings are embedded here for future use if
	 * required.
	 * 
	 * @param MeunItem
	 *            item The selected item from the menu.
	 * @return boolean Returns
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

	/**
	 * Method setTextViewColor changes the text' color of the Address'
	 * description according to the current type of map.
	 * 
	 * @param int color Holds the color' integer value.
	 * @return void Returns a void object.
	 */
	private void setTextViewColor(int color) {
		TextView tvLat = (TextView) this.findViewById(R.id.txLat);
		TextView tvLon = (TextView) this.findViewById(R.id.txLon);
		TextView tvTime = (TextView) this.findViewById(R.id.txTime);
		TextView tvAddress = (TextView) this.findViewById(R.id.txAddress);
		tvLon.setTextColor(color);
		tvLat.setTextColor(color);
		tvTime.setTextColor(color);
		tvAddress.setTextColor(color);
	}// END setTextViewColor

	/**
	 * Method onStop saves the map when the application stops to a
	 * MapStateManager imported from MapStateManager.java
	 * 
	 * @return void Return a void object.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(Team3Map);
		DBConnection.close();
	}// End onStop

	/**
	 * Method onResume resumes the map when the application re-opens to where it
	 * was left and updates the camera position. It also restores the map type
	 * that was previously selected.
	 * 
	 * @return void Return a void object.
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

	/**
	 * Method gotoCurrentLocation is used to take the user back to his/hers
	 * current location. Since the user may move away from the current location
	 * (For instance, move the map around), to go back to the current location
	 * the user can press menu item defined as action under menu--> main.xml.
	 * This way the "button" is shown always left of the three dots and the user
	 * can go back to the current location. If the current location is not
	 * available a toast message will appear informing the user that it is not
	 * available. If it is it will take him/her back and it will reset the zoom
	 * etc.
	 * 
	 * @return void Return a void object.
	 */
	protected void gotoCurrentLocation() {
		Location currentLocation = mLocationClient.getLastLocation();
		if (currentLocation == null) {
			Toast.makeText(this, "Current location isn't available",
					Toast.LENGTH_SHORT).show();
		} else {
			updateCurrentLocationMarker(currentLocation.getLatitude(),
					currentLocation.getLongitude());
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
	 * 
	 * @param Bundle
	 *            arg0 Bundle of data provided to clients by Google Play
	 *            services. May be null if no content is provided by the
	 *            service.
	 * @return void Return a void object.
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

	/**
	 * Method onLocationChanged responsible for updating Address Description of
	 * the updated location.
	 * 
	 * @param Location
	 *            loc hold the value of the updated location.
	 * 
	 * @return Returns a void object.
	 */
	@Override
	public void onLocationChanged(Location loc) {

		String Date = DateTimeManipulator.getCurrentDate();
		String Time = DateTimeManipulator.getCurrentTime();

		double LAT = loc.getLatitude();
		double LONG = loc.getLongitude();

		updateCurrentLocationMarker(LAT, LONG);

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
		JSONObject ret = AddressConversion.getLocationInfo(LAT, LONG);
		JSONObject location;
		String location_string;
		try {
			location = ret.getJSONArray("results").getJSONObject(0);
			location_string = location.getString("formatted_address");
			Log.d("test", "formattted address:" + location_string);

			tvAddress.setText(location_string);

			CurrentLocation = new LocationVO(location_string, LAT, LONG, "");
			loadRegisteredLocations();
			

			// This gives the device a UNIQUE ID (NOTE: Try to add this to
			// another Class).
			String deviceId = Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);

			/*
			 * IMPORTANT!!! Left this part of the code commented for now,
			 * because we have limited requests to the server
			 */
			// Calls generateXML method in order to save the details to an XML
			// file

			/*
			 * try { xmlGenerator.generate(Date, Time, deviceId, loc,
			 * location_string); Toast.makeText(this, "File has been created.",
			 * Toast.LENGTH_SHORT).show(); } catch (Exception e) {
			 * Toast.makeText(this, "An error has occured. Restart the app.",
			 * Toast.LENGTH_SHORT).show(); }
			 * 
			 * // Calls uploadToServer in order to upload the xml file to the
			 * //server. try { fileUploader.upload(Date, Time, deviceId); }
			 * catch (Exception e) { Toast.makeText(this, "An error has occured"
			 * + e.getMessage(), Toast.LENGTH_SHORT).show(); }
			 * Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show();
			 */} catch (JSONException e1) {
			Toast.makeText(this, "Error: " + e1.getMessage(),
					Toast.LENGTH_SHORT).show();
			e1.printStackTrace();

		}

	}// Ends onLocationChanged

	/**
	 * update the marker for the Current Location of the user on the map
	 * according to the updated latitude and longitude of the user.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param title
	 */
	private void updateCurrentLocationMarker(double latitude, double longitude) {
		if (marker != null) {
			marker.remove();
		}
		LatLng ll = new LatLng(latitude, longitude);
		marker = Team3Map.addMarker(new MarkerOptions()
				.position(ll)
				.title("You are here")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(ll, DEFAULTZOOM);
		Team3Map.animateCamera(update);
	}

	/**
	 * onPause method, used as part of the activity lifecycle when an activity
	 * is going in the background and it has not yet being killed.
	 * 
	 * @return void Returns a void object.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mLocationClient.removeLocationUpdates(this);
	}// Ends onPause

	/**
	 * checkIn method, used when the user clicks on the "Check in" button. It
	 * takes the user to another screen called "Check in"
	 * 
	 * @return void
	 */
	public void checkIn(View view) {
		Intent intent = new Intent(this, CheckInActivity.class);
		if (!CurrentLocation.getAddress().isEmpty()
				&& CurrentLocation.getLatitude() != 0
				&& CurrentLocation.getLongitude() != 0) {
			intent.putExtra("LocationVO", CurrentLocation);
			intent.putExtra("UserID", UserID);
			startActivity(intent);
		}
	}

}// Ends MainActivity
