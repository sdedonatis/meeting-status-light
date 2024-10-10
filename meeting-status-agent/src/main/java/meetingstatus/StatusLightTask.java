package meetingstatus;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;

import com.fazecast.jSerialComm.SerialPort;

/** TimerTask that updates the color of the external led */
public class StatusLightTask extends TimerTask {
	private static final Logger LOGGER = Logger.getLogger(StatusLightTask.class.getName());
	
	private Integer xPos;
	private Integer yPos;
	private String port; // arduino COM port
	private JComponent jComponent; //component to update
	private Robot robot;

	public StatusLightTask(Integer xPos, Integer yPos, String port, JComponent jComponent) throws AWTException {
		this.xPos = xPos;
		this.yPos = yPos;
		this.port = port;
		this.jComponent = jComponent;
		this.robot = new Robot();
	}
	
	@Override
	public void run() {
		// Get X, Y Color
		// If desktop is locked returns 0, 0, 0
		Color color = robot.getPixelColor(xPos, yPos);
		LOGGER.fine("Color readed at " + xPos + ", " + yPos + " color " + color);
         
		//component
		jComponent.setBackground(color);
		
		// Open serial Port
		SerialPort sp = SerialPort.getCommPort(port);
		sp.setComPortParameters(115200, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written
		
		if (sp.openPort()) {
			LOGGER.fine("Port is open :)");
		} else {
			LOGGER.fine("Failed to open port :(");
			return;
		}
         
		// Send color
		color = colorMatch(color);
		String message = color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "\r";
		try {
			sp.getOutputStream().write(message.getBytes());
			sp.getOutputStream().flush();
			LOGGER.fine("Sent color: " + message);
			Thread.sleep(1000);
			
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Error sending data", e);
			return;
		}

		// Close port
		if (sp.closePort()) {
			LOGGER.fine("Port is closed :)");
		} else {
			LOGGER.fine("Failed to close port :(");
		}
	}

	/** Matches the color of the led to the screen */
	private Color colorMatch(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		//color correction. Led has a blue tint
		red = Math.max(red - 60, 0);
		green = Math.max(green - 70, 0);
		blue = Math.max(blue - 70, 0);
	
		Color newColor = new Color(red, green, blue);
		return newColor;
	}
}