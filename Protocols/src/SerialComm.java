package assignment6;

import jssc.*;

public class SerialComm {

	SerialPort port;

	private boolean debug;  // Indicator of "debugging mode"
	
	// This function can be called to enable or disable "debugging mode"
	void setDebug(boolean mode) {
		debug = mode;
	}	
	

	
	
	// Constructor for the SerialComm class
	public SerialComm(String name) throws SerialPortException {
		port = new SerialPort(name);		
		port.openPort();
		port.setParams(SerialPort.BAUDRATE_9600,
			SerialPort.DATABITS_8,
			SerialPort.STOPBITS_1,
			SerialPort.PARITY_NONE);
		
		debug = true; // Default is to NOT be in debug mode
	}
	
	
	
		
		void writeByte(byte b, boolean debug) {
				if (debug) {
					System.out.println(String.format("<%02X>", b)); // correct debug format?
				}
				try {
					port.writeByte(b);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		
		
		
		
		 	// TODO: Add available() method
			// Will return TRUE if there is at least one byte available (being sent) to be read from the serial port
			boolean available() {
				try {
					if (this.port.getInputBufferBytesCount() > 0) {
						return true;
					}
					else {
						return false;
					}
				}
				catch (SerialPortException e) {
					e.printStackTrace();
					return false;
				}
			}
		 	
			
			
			
			
			
		 	// TODO: Add readByte() method	
			// This will read a single byte from the serial port and return a byte that contains the value read
			byte readByte(boolean debug) {
				try {
					byte b = this.port.readBytes(1)[0];
					if (debug) {
						System.out.print(String.format("[0x%02x]", b));
					}
					return b;
				}
				catch (SerialPortException e) {
					e.printStackTrace();
					return 1; // error code?
				}
			}
		 	
			
			
			
			
			
		 	// TODO: Add a main() method
//			public static void main(String[] args) {
//				try {
//					SerialComm port = new SerialComm("/dev/cu.usbserial-DN03EPK9");
//					while(true) {
//						if (port.available()) {
//							char c = (char) port.readByte(false);
//							System.out.print(c);
//						}
//					}
//				}
//				
//				
//				catch (SerialPortException e) {
//					e.printStackTrace();
//				}
//				
//				
//			}
} 