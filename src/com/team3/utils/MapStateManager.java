/**
 * FR1
 * @author Andreas Stavrou (Initial coding and Refactoring)
 * @version 1.0 - 6 October 2013 | Refactored FINISHED on 16 October 2013 
 */

package com.team3.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * MapStateManager.java is used to save the map state, camera position and map type on state manager so
 * when the user exits (pause the application), it can resume the position of the map etc. 
 */
public class MapStateManager {
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String ZOOM = "zoom";
	private static final String BEARING = "bearing";
	private static final String TILT = "tilt";
	private static final String MAPTYPE = "MAPTYPE";

	private static final String PREFS_NAME = "mapCameraState";

	private SharedPreferences mapStatePrefs;

	public MapStateManager(Context context) {
		mapStatePrefs = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Method saveMapState saves the current maps state for when the user leaves
	 * the application e.g. presses the home button.
	 */
	public void saveMapState(GoogleMap map) {
		SharedPreferences.Editor editor = mapStatePrefs.edit();
		CameraPosition position = map.getCameraPosition();

		editor.putFloat(LATITUDE, (float) position.target.latitude);
		editor.putFloat(LONGITUDE, (float) position.target.longitude);
		editor.putFloat(ZOOM, position.zoom);
		editor.putFloat(TILT, position.tilt);
		editor.putFloat(BEARING, position.bearing);
		editor.putInt(MAPTYPE, map.getMapType());

		editor.commit();
	}// Ends saveMapState

	/**
	 * Method saveMapState saves the current maps state for when the user leaves
	 * the application e.g. presses the home button.
	 * 
	 * @param GoogleMap map, an object which has the google map on it.
	 * @return void, returns a void object.
	 */
	public CameraPosition getSavedCameraPosition() {
		double latitude = mapStatePrefs.getFloat(LATITUDE, 0);
		if (latitude == 0) {
			return null;
		}
		double longitude = mapStatePrefs.getFloat(LONGITUDE, 0);
		LatLng target = new LatLng(latitude, longitude);

		float zoom = mapStatePrefs.getFloat(ZOOM, 0);
		float bearing = mapStatePrefs.getFloat(BEARING, 0);
		float tilt = mapStatePrefs.getFloat(TILT, 0);

		CameraPosition position = new CameraPosition(target, zoom, tilt,
				bearing);
		return position;
	}// Ends getSavedCameraPosition

	/**
	 * Gets the saved map type and returns it to normal.
	 * 
	 * @return int, returns interger value of the map type.
	 */
	
	public int getSavedMapType() {
		return mapStatePrefs.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL);
	}// Ends getSavedMapType

}// Ends MapStateManager
