package meetingstatus;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;

public class SerialPortTest {
	private static final Logger LOGGER = Logger.getLogger(SerialPortTest.class.getName());
	
	public static void main(String[] args) {
		String port = "COM3";
		// Open Port
		SerialPort sp = SerialPort.getCommPort(port);
		sp.setComPortParameters(115200, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written
		
		if (sp.openPort()) {
			LOGGER.fine("Port is open :)");
		} else {
			LOGGER.warning("Failed to open port :(");
			return;
		}
        	
		//send command ?
		String message = "?";
		sendMessage(sp, message);
		LOGGER.fine("sent: " + message);
		String response = readResponse(sp);
		LOGGER.fine("response: " + response);

		message = "185,22,4";
		sendMessage(sp, message);
		LOGGER.fine("sent: " + message);
		response = readResponse(sp);
		LOGGER.fine("response: " + response);
		
		// Close port
		if (sp.closePort()) {
			LOGGER.fine("Port is closed :)");
		} else {
			LOGGER.warning("Failed to close port :(");
		}
	}
	
	private static void sendMessage(SerialPort sp, String message) {
		try {
			sp.getOutputStream().write(message.getBytes());
			sp.getOutputStream().write("\r".getBytes());
			sp.getOutputStream().flush();
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error sending message", e);
			return;
		}
	}
	
	private static String readResponse(SerialPort sp) {
		StringBuffer sb = new StringBuffer();
		//read response
		while (sp.bytesAvailable() > 0) {
			byte[] readBuffer = new byte[sp.bytesAvailable()];
			int numBytes = sp.readBytes(readBuffer, sp.bytesAvailable());
			LOGGER.fine("Read " + numBytes + " bytes.");
			sb.append(readBuffer.toString());
		}
		return sb.toString();
	}
}