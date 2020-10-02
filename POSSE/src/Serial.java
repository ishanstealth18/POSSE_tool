import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.Enumeration;

import javax.xml.bind.DatatypeConverter;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class Serial {


	public Serial()
	{
		super();
	}
	
	public void connectPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if(portIdentifier.isCurrentlyOwned())
		{
			System.out.println("Error: Port Busy!!!!");
		}
		else
		{
			System.out.println("Port name: "+portIdentifier.getName() +" " +"Port owner: "+portIdentifier.getCurrentOwner());
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 5000);
			if(commPort instanceof SerialPort)
			{
				System.out.println("Commport is instance of serial port");
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				
				InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                
			}
		}
	}
	
	public static class SerialReader implements Runnable
	{
		InputStream in;
		public SerialReader(InputStream in)
		{
			System.out.println("Inside serial reader constructor");
			this.in = in;
		}

		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Inside run method");
			StringBuffer sBuffer = new StringBuffer();
			String inputStr;
			 byte[] buffer = new byte[1024];
	         int len = -1;
	         try
	         {
	        	 while((len = this.in.read(buffer)) > -1)
	        	 {
	        		 
	        		 System.out.println(DatatypeConverter.printHexBinary(buffer));
	        		 //System.out.print(new String(buffer,0,len));
	        		 
	        		 Thread.sleep(1000);
	        		 
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
		System.out.println("Hello");
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
