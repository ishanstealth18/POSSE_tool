import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.xml.bind.DatatypeConverter;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.util.BitSet;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import gnu.io.UnsupportedCommOperationException;

public class Serial{
	
	//Declaring variables
	SerialPort serialPort; 
	SerialPort serialPort2;
	CommPortIdentifier portIdentifier;
	CommPortIdentifier portIdentifier2;
	CommPort commPort;
	CommPort commPort2;
	
	
	
	public Serial()
	{
		super();
	}
	
	/*
	 * Function to connect to a port and opening it for communication
	 */
	public void connectPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException
	{
		portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		portIdentifier2 = CommPortIdentifier.getPortIdentifier("COM4");
		if(portIdentifier.isCurrentlyOwned() || portIdentifier2.isCurrentlyOwned())
		{
			System.out.println("Error: Port Busy!!!!");
		}
		else
		{
			//System.out.println("Port name: "+portIdentifier.getName() +" " +"Port owner: "+portIdentifier.getCurrentOwner());
			commPort = portIdentifier.open(this.getClass().getName(), 5000);
			commPort2 = portIdentifier2.open(this.getClass().getName(), 5000);
			if(commPort instanceof SerialPort)
			{
				serialPort = (SerialPort) commPort;
				serialPort2 = (SerialPort) commPort2;
				
				
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1 , SerialPort.PARITY_NONE);
				serialPort2.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1 , SerialPort.PARITY_NONE);
				
				InputStream in = serialPort.getInputStream();
				InputStream in2 = serialPort2.getInputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialReader(in2))).start();
                
			}
		}
	}
	
	//Serial reader class 
	public static class SerialReader implements Runnable
	{
		InputStream in;
		//BitSet set = new BitSet();
		public SerialReader(InputStream in)
		{
			//System.out.println("Inside serial reader constructor");
			this.in = in;
		}

		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("Inside run method");
			 byte[] buffer = new byte[1024];
	         int len = -1;
	         //Logic to read the streaming and printing in HEX
	         try
	         {
	        	 while((len = this.in.read(buffer)) > -1)
	        	 {
	        		 System.out.println();
	        		 for(int i = 0; i<len; i++)
	        		 {
	        			 System.out.printf("%02X ", buffer[i]);
	        		 }
	        		 System.out.println();
	        		 Thread.sleep(500);
	        		 
	        	 }
	         }
	         catch(IOException e)
	         {
	        	 e.printStackTrace();
	         } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static void main(String[] args) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		// TODO Auto-generated method stub	
		//System.out.println("Hello");
		try
		{
			(new Serial()).connectPort("COM15");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}	
}
