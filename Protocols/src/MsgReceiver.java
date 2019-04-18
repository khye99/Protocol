package assignment6;
import java.nio.charset.StandardCharsets;
import jssc.*;

public class MsgReceiver {
	private SerialComm port;
	public MsgReceiver(String portname) throws SerialPortException {
		port = new SerialComm(portname);
	}
	public static int unsigned(int b) {
		if(b < 0) {
			return b & 0xFF;
		}
		return b;
	}
	public void run() {
		double[] filter = new double[10];
		int counter = 0;
		while(true) { //Loop
			if (port.available() && port.readByte(false) == 0x21) {
			byte key = port.readByte(false);
				switch(key) {
				case 0x30:
					//Debug (String)
					byte size = port.readByte(false);
					byte[] debug = new byte[size];
					int position = 0;
					while(position < size) {
						if(port.available()) { // filling array with chars
							debug[position] = port.readByte(false);
							position ++;
						}
					}                                       
					String s = new String(debug, StandardCharsets.UTF_8);
					System.out.println("Debug Message: " + s);
					break;
				case 0x31:
					//Error Message (String)
					size = 16;
					byte[] error = new byte[size];
					position = 0;
					while(position < size) {
						if(port.available()) { // filling array with chars
							error[position] = port.readByte(false);
							position ++;
						}
					}                                       
					String q = new String(error, StandardCharsets.UTF_8);
					System.out.println("Error Message: " + q);
					break; 
				case 0x32:
					//Timestamp (4 Byte Unsigned Long in Arduino, 4 Byte Int in Java)
					size = 4;
					byte[] time = new byte[size];
					position = 0;
					while(position < size) {
						if(port.available()) { // filling array with chars
							time[position] = port.readByte(false);
							position ++;
						}
					}                                       
					int t1 = unsigned((int)(time[0]<<24));
					int t2 = unsigned((int)(time[1]<<16));
					int t3 = unsigned((int)(time[2]<<8));
					int t4 = unsigned((int)(time[3]));
					long millis = t1 + t2 + t3 + t4;
					System.out.println("Timestamp: " + millis);
					break;
				case 0x33:
					//Potentiometer Reading (2 Byte Integer)
					size = 2;
					byte[] pot = new byte[size];
					position = 0;
					while(position < size) {
						if(port.available()) { // filling array with chars
							pot[position] = port.readByte(false);
							position ++;
						}
					}                                       
					int p1 = unsigned((int)(pot[0]<<8));
					int p2 = unsigned((int)(pot[1]));
					int potValue = p1 + p2;
					System.out.println("Potentiometer: " + potValue);
					break;
				case 0x34:
					//Raw Temperature (2 Byte Integer)
					size = 2;
					byte[] temp = new byte[size];
					position = 0;
					while(position < size) {
						if(port.available()) { // filling array with chars
							temp[position] = port.readByte(false);
							position ++;
						}
					}                                       
					int r1 = unsigned((int)(temp[0]<<8));
					int r2 = unsigned((int)(temp[1]));
					int rawTemp = r1 + r2;
					double cTemp = Math.round(((rawTemp * (5.0/1023) - 0.75)*100 + 25)*100)/100.0;
					//Rolling Average Filter
					filter[counter % 10] = cTemp;
					counter++;
					double sum = 0;
					double avgTemp;
					if(counter < 10) {
						for(int i = 0; i < counter; i++) {
							sum += filter[i];
						}
						avgTemp = sum/counter;
					} else {
						for(int i = 0; i < 10; i++) {
							sum += filter[i];
						}
						avgTemp = sum/10.0;
					}
					avgTemp = Math.round(avgTemp*100)/100.0;
					//Printing out temp values
					System.out.println("Raw Temp: " + rawTemp);
					System.out.println("Celsius Temp: " + cTemp);
					System.out.println("Average Temp: " + avgTemp);
					System.out.println("");
					break;
				default:
					System.out.println("    !!!!ERROR!!!!");
				}
			}
		}           
	}
	public static void main(String[] args) throws SerialPortException {
		MsgReceiver msgr = new MsgReceiver("/dev/cu.usbserial-DN03EPK9"); // Adjust this to be the right port for your machine
		msgr.run();
	}
}