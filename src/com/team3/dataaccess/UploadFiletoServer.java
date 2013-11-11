package com.team3.dataaccess;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

public class UploadFiletoServer {
	public int serverResponseCode;

	public UploadFiletoServer() {
	}

	public void upload(String Date, String Time, String deviceId) throws Exception{
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
			
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId
					+ ".xml");
			file.delete();
		} catch (Exception e) {
			Log.e("Upload File", e.getMessage());
			throw e;
			// Exception handling
		}
	}
}
