package meetingstatus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/** Reads and persists the properties of the agent in a file */
public class MeetingStatusProperties {
	private static final String FILE_NAME = "meeting-status-agent.properties";
	private static final String PORT_NAME = "port_name";
	private static final String INTERVAL = "interval";
	private static final String X_POS = "x_pos";
	private static final String Y_POS = "y_pos";
	
	private Properties properties;
	
	public MeetingStatusProperties() {
		properties = new Properties();
	}
	
	public void load() throws IOException {
		//load a properties file
		properties.load(new FileInputStream(FILE_NAME));
	}
	
	public void save() throws IOException {
		properties.store(new FileOutputStream(FILE_NAME), "Meeting status indicator config");
	}

	public String getPortName() {
		return properties.getProperty(PORT_NAME);
	}

	public void setPortName(String value) {
		properties.setProperty(PORT_NAME, value);
	}
	
	public Integer getInterval() {
		return Integer.valueOf(properties.getProperty(INTERVAL, "0"));
	}
	
	public void setInterval(Integer value) {
		properties.setProperty(INTERVAL, String.valueOf(value));
	}
	
	public Integer getXPos() {
		return Integer.valueOf(properties.getProperty(X_POS, "0"));
	}
	
	public void setXPos(Integer value) {
		properties.setProperty(X_POS, String.valueOf(value));
	}
	
	public Integer getYPos() {
		return Integer.valueOf(properties.getProperty(Y_POS, "0"));
	}
	
	public void setYPos(Integer value) {
		properties.setProperty(Y_POS, String.valueOf(value));
	}
}
