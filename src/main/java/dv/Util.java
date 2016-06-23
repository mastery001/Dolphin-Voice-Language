package dv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Util {

	private static Vector<String> msgFiles = new Vector<String>();
	private static Properties messages = null;
	private static String defaultKey = "default";

	static {
		msgFiles.addElement("dv/compiles.properties");
	}

	private static void readMessages() {
		messages = new Properties();
		Enumeration<String> fileList = msgFiles.elements();
		InputStream stream;
		while (fileList.hasMoreElements())
			try {
				stream = Util.class.getClassLoader().getResourceAsStream(fileList.nextElement());
				messages.load(stream);
			} catch (IOException e) {
			}
		if (messages.size() == 0)
			messages.put(defaultKey, "Error reading Messages File.");
	} // readMessages

	public static String getMessage(String key) {
		if (messages == null)
			readMessages();
		String message = messages.getProperty(key);
		if (message == null)
			message = getDefaultMessage(key);
		return message;
	}

	public static String getMessage(String key, String fill) {
		if (messages == null)
			readMessages();
		String message = messages.getProperty(key);
		if (message == null)
			message = getDefaultMessage(key);
		else {
			int index = message.indexOf("%0");
			if (index >= 0)
				message = message.substring(0, index) + fill + message.substring(index + 2);
		}
		return message;
	} // getMessage

	public static String getMessage(String key, String[] fill) {
		if (messages == null)
			readMessages();
		String message = messages.getProperty(key);
		if (message == null)
			message = getDefaultMessage(key);
		else
			for (int i = 0; i < fill.length; ++i) {
				int index = message.indexOf("%" + i);
				if (index >= 0)
					message = message.substring(0, index) + fill[i] + message.substring(index + 2);
			}
		return message;
	}

	private static String getDefaultMessage(String keyNotFound) {
		String message = messages.getProperty(defaultKey);
		int index = message.indexOf("%0");
		if (index > 0)
			message = message.substring(0, index) + keyNotFound;
		return message;
	} // getDefaultMessage

}
