package com.example.team3;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class UploadFiletoServer {
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
	private Object context;
	private Context Context;
public UploadFiletoServer(){
	}
public void upload(String Date, String Time, String deviceId){
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
		Toast.makeText(Context, "File Uploaded", Toast.LENGTH_SHORT).show();

		fileInputStream.close();
		outputStream.flush();
		outputStream.close();
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId
				+ ".xml");
		file.delete();
		Toast.makeText(Context, "File Deleted", Toast.LENGTH_SHORT).show();
	} catch (Exception ex) {
		Toast.makeText(Context, "An error has occured" + ex,
				Toast.LENGTH_SHORT).show();
		// Exception handling
	}
}
}
