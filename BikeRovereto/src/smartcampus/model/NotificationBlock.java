package smartcampus.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import smartcampus.notifications.NotificationReceiver;
import android.content.Context;
import android.util.Log;

public class NotificationBlock implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GregorianCalendar calendar;
	private String stationID;
	private int uniqueID;

	public NotificationBlock(GregorianCalendar calendar, String stationID, Context context)
	{
		this.calendar = calendar;
		upDateCalendar();
		this.stationID = stationID;
		NotificationReceiver mr = new NotificationReceiver();
		uniqueID = (int) (calendar.getTimeInMillis() & 0xfffffff);
		mr.registerAlarm(context, calendar, uniqueID, stationID);
	}

	public static void saveArrayListToFile(ArrayList<NotificationBlock> arrayList, String fileName, Context context)
	{
		
		try
		{
			OutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try
			{
				output.writeObject(arrayList);
			}
			finally
			{
				output.close();
			}

		}
		catch (FileNotFoundException e)
		{
			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static ArrayList<NotificationBlock> readArrayListFromFile(String fileName, Context context)
	{

		ArrayList<NotificationBlock> recoveredNotificationBlocks = new ArrayList<NotificationBlock>();
		// use buffering
		try
		{
			InputStream file = context.openFileInput(fileName);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			try
			{
				// deserialize the List
				recoveredNotificationBlocks = (ArrayList<NotificationBlock>) input.readObject();
			}
			finally
			{
				input.close();
			}
		}
		catch (StreamCorruptedException e)
		{			
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		for(NotificationBlock nb : recoveredNotificationBlocks)
		{
			nb.upDateCalendar();
		}
		return recoveredNotificationBlocks;
	}

	public GregorianCalendar getCalendar()
	{
		return calendar;
	}
	
	public void setCalendar(GregorianCalendar calendar)
	{
		this.calendar = calendar;
	}

	public String getID()
	{
		return stationID;
	}
	
	public int getUniqueID()
	{
		return uniqueID;
	}

	public static ArrayList<NotificationBlock> getReminderForID(String id, Context context)
	{
		ArrayList<NotificationBlock> list = readArrayListFromFile("notificationBlockDB", context);
		Log.d("remindersListSize", list.size() + "");		
		ArrayList<NotificationBlock> timesList = new ArrayList<NotificationBlock>();
		for (NotificationBlock nb : list)
		{
			if (nb.getID().equals(id))
			{
				timesList.add(nb);
			}
		}
		return timesList;
	}
	
	private void upDateCalendar()
	{
		Calendar now = Calendar.getInstance();
		if (this.calendar.before(now))
		{// if its in the past increment
			this.calendar.add(GregorianCalendar.DATE, 1);
		}
	}
	
}
