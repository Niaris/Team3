/** 
 * MainActivity.java is used to show the user's current location on a map. 
 * @author Andreas Stavrou (Initial coding, application works but does not display marker)
 * @author Ellis De Vasconcelos Carvalho (Added Marker on the map)
 * @author Andreas Stavrou (Refactored and Commented)
 * @version 1.0 - 6 October 2013 | Refactored FINISHED on 16 October 2013 
 * 
 * !SOME EXTRA FUNCTIONALITY IS INCLUDED WHICH MAY COME IN HANDY ON THE LATER REQUIREMENTS!
 */

package com.example.team3;



import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The public class must extend and implements the following in order for it to be able to work. I.e. display the map, 
 * get coordinates and use the GooglePlayServicesClient on call backs and on connection failed listeners.
 */
public class MainActivity extends FragmentActivity implements 
  GooglePlayServicesClient.ConnectionCallbacks,
  GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	
	/**
	 * Public variable declarations.
	 */
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	@SuppressWarnings("unused")
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
	GoogleMap mMap;
	private static final float DEFAULTZOOM = 15;
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Maps";
	
	LocationClient mLocationClient;

	
	/**
	 * Method onCreate is used when the page first loads. 
	 * It will use the method servicesOK which will determine IF the
	 * user's device has up-to-date google services and if it has them at all, and
	 * if they are okay it will display the activity_map. If the initial Map - method initMap - is not
	 * available then a Toast Message will be displayed. If it is it will connect to LocationClient. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (servicesOK()) {
			setContentView(R.layout.activity_map);

			if (initMap()) {
				mLocationClient = new LocationClient(this, this, this);
				mLocationClient.connect();
			}
			else {
				Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			setContentView(R.layout.activity_main);
		}

	}//Ends onCreate
	
	/**
	 * Method onCreateOptionsMenu inflates the menu. This adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}//Ends onCreateOptionsMenu

	/**
	 * Method servicesOK as described on method onCreate, will check the user's device if Google Services are available. 
	 * If yes it will return true.
	 * If not but it is user recoverable error (like out of date) it will show the error dialog and try, for this instance 
	 * to update the services.
	 * If is not available it will show an error toast message.
	 * 
	 */
	public boolean servicesOK() {
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		}
		else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}
		else {
			Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
		}
		return false;
	}//Ends servicesOK

	/**
	 * Method initMap initialises the map fragment from the activity_map.xml. 
	 * In other words it fill the map fragment with a map.
	 */
	private boolean initMap() {
		if (mMap == null) {
			SupportMapFragment mapFrag =
					(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			mMap = mapFrag.getMap();
		}
		return (mMap != null);
	}//Ends initMap

/**
 * Method onOptionsItemSelected changes the map to 
 * Normal, Satellite, Terrain, Hybrid and None. 
 * Settings are embedded here for future use if required.
 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.mapTypeNone:
			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		case R.id.mapTypeNormal:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.mapTypeSatellite:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.mapTypeTerrain:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case R.id.mapTypeHybrid:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.gotoCurrentLocation:
			gotoCurrentLocation();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Method onStop saves the map when the application stops to a MapStateManager imported
	 * from MapStateManager.java
	 */
	@Override
	protected void onStop() {
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(mMap);
	}

	/**
	 * Method onResume resumes the map when the application re-opens to where it was left
	 * and updates the camera position. It also restores the map type that was previously selected 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if (position != null) {
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			mMap.moveCamera(update);

			mMap.setMapType(mgr.getSavedMapType());
		}
		
		if (mLocationClient.isConnected()) {
			requestLocationUpdates();
		}
	}//Ends onResume

 /*
  *  Method gotoCurrentLocation is used to take the user back to his/hers current location.
  *  Since the user may move away from the current location (For instance, move the map around), 
  *  to go back to the current location the user can press menu item defined as action under menu--> main.xml.
  *  This way the "button" is shown always left of the three dots and the user can go back to the current location.
  *  If the current location is not available a toast message will appear informing the user that it is not available. 
  *  If it is it will take him/her back and it will reset the zoom etc.
  */
   protected void gotoCurrentLocation() {
		Location currentLocation = mLocationClient.getLastLocation();
		if (currentLocation == null) {
			Toast.makeText(this, "Current location isn't available", Toast.LENGTH_SHORT).show();
		}
		else {
			LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			mMap.addMarker(new MarkerOptions().position(ll).title("You are here"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
			mMap.animateCamera(update);
		}
	}//Ends gotoCurrentLocation 
	

   /**
    * Method onConnectionFailed is used for when the connection fails to display
    * a toast message to inform the user.
    */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed. Please try again", Toast.LENGTH_SHORT).show();
		requestLocationUpdates();
		
	}//Ends onConnectionFailed

	/**
	 * Method onConnected is used for when the connection succeeds to display
    * a toast message to inform the user.
	 */
	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "Connected Successfully", Toast.LENGTH_SHORT).show();
		requestLocationUpdates();
	}//Ends onConnected

	@Override
	public void onDisconnected() {
		
	}//Ends onDisconnected

	/**
	 * Method requestLocationUpdates is used to update the location of the user 
	 * automatically ever 1 minute (60000 milliseconds in this)
	 * In other words it refreshes the location and shows the new coordinates.
	 */
	private void requestLocationUpdates() {
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(60000);
		request.setFastestInterval(1000);
		mLocationClient.requestLocationUpdates(request, this);
	}//Ends requestLocationUpdates

	/**
	 * Method onLocationChanged displays the latitude and longitude on a toast message.
	 */
	@Override
	public void onLocationChanged(Location loc) {
		Toast.makeText(this, "Location: " + loc.getLatitude() + "," + loc.getLongitude(),
				Toast.LENGTH_SHORT).show();
	}//Ends onLocationChanged
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationClient.removeLocationUpdates(this);
	}//Ends onPause
	
}//Ends MainActivity
