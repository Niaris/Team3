package com.team3.dataaccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class XMLGenerator {

	public XMLGenerator() {
	}

	public void generate(String Date, String Time, String deviceId,
			Location loc, String Address) throws Exception {

		File xmlfile = new File(Environment.getExternalStorageDirectory()
				+ "/Coords" + "-" + Date + "-" + Time + "-" + deviceId + ".xml");
		Log.d("FILE PATH", "path=" + xmlfile);
		try {
			xmlfile.createNewFile();
		} catch (IOException e) {
			Log.e("IOException", "exception in createNewFile() method");
			throw e;
		}
		// we have to bind the new file with a FileOutputStream
		FileOutputStream fileOutStr = null;
		try {
			fileOutStr = new FileOutputStream(xmlfile);
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
			throw e;
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
			
			createTag(serializer, "Latitute", Double.toString(loc.getLatitude()));
			createTag(serializer, "Longitude", Double.toString(loc.getLongitude()));
			
			serializer.endTag(null, "Coordinates");

			serializer.startTag(null, "DateTime");// Child 2

			createTag(serializer, "Date", Date);
			createTag(serializer, "Time", Time);
			
			serializer.endTag(null, "DateTime"); // Child 3

			createTag(serializer, "Address", Address);
			
			serializer.endTag(null, "Location");
			serializer.endDocument();
			// write xml data into the FileOutputStream
			serializer.flush();
			// finally we close the file stream
			fileOutStr.close();

		} catch (Exception e) {
			// Toast.makeText(context, "An error has occured. Restart the app.",
			// Toast.LENGTH_SHORT).show();
			Log.e("Exception", "error occurred while creating xml file");
			throw e;
		}
	}

	private void createTag(XmlSerializer serializer, String tagName,
			String tagText) throws IllegalArgumentException,
			IllegalStateException, IOException {
		serializer.startTag(null, tagName);
		serializer.text(tagText);
		serializer.endTag(null, tagName);
	}
}
