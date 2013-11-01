package com.example.team3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class XMLGenerator {
	private Context context;
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
	public XMLGenerator()
	{}
	public void generate(String Date, String Time, String deviceId, Location loc, String Address)
	{
		
	File xmlfile = new File(Environment.getExternalStorageDirectory()
			+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId + ".xml");
	Log.d("FILE PATH", "path=" + xmlfile);
	try {
		xmlfile.createNewFile();
	} catch (IOException e) {
		Toast.makeText(context,
				"An error has occured. Restart the Application.",
				Toast.LENGTH_SHORT).show();
		Log.e("IOException", "exception in createNewFile() method");
	}
	// we have to bind the new file with a FileOutputStream
	FileOutputStream fileOutStr = null;
	try {
		fileOutStr = new FileOutputStream(xmlfile);
	} catch (FileNotFoundException e) {
		Toast.makeText(context,
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
		Toast.makeText(context, "File has been created.", Toast.LENGTH_SHORT)
				.show();

	} catch (Exception e) {
		Toast.makeText(context, "An error has occured. Restart the app.",
				Toast.LENGTH_SHORT).show();
		Log.e("Exception", "error occurred while creating xml file");
	}
	}
}
