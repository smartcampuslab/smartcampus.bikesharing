package eu.trentorise.smartcampus.bikesharing.log;

import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;

//@Component
public class Log
{
//	@Value("${logPath}")
	private String logPath;
	
	private int requestNumber = 0;
	
	private Semaphore s = new Semaphore(1, true);
	
	public void printRequest(HttpServletRequest request, String errorMessage)
	{
		System.out.println("IP: " + request.getRemoteAddr());
		System.out.println("PORT: " + request.getRemotePort());
		System.out.println("URI: " + request.getRequestURI());
		System.out.println();

		Calendar date = GregorianCalendar.getInstance();
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);
		String dayHalf = date.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
		
		String logFileName = "" + year + "" + (month > 10 ? month : ("0" + month))+ "" + (day > 10 ? day : ("0" + day)) + ".txt";
		try
		{
			s.acquire();
			
			FileWriter fw = new FileWriter(logPath + logFileName, true);
			
			fw.write((++requestNumber) + ", ");
			fw.write("" + hour + ":" + minute + ":" + second + " " + dayHalf + ", ");
			fw.write("Ip: " + request.getRemoteAddr() + ", ");
			fw.write("Host: " + request.getRemoteHost() + ", ");
			fw.write("Port: " + request.getRemotePort() + ", ");
			fw.write("User: " + request.getRemoteUser() + ", ");
			fw.write("Uri: " + request.getRequestURI() + ", ");
			fw.write("Json: " + request.getAttribute("body") + ", ");
			fw.write("Error:" + errorMessage);
			fw.write("\r\n");
			fw.close();
			
			s.release();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			s.release();
		}
	}
}
