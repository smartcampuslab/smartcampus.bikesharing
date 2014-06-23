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

import android.content.Context;

public class NotificationBlock implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Calendar calendar;
	String stationID;

	public NotificationBlock(Calendar calendar, String stationID)
	{
		this.calendar = calendar;
		this.stationID = stationID;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<NotificationBlock> readArrayListFromFile(String fileName, Context context)
	{

		ArrayList<NotificationBlock> recoveredNotificationBlocks = null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recoveredNotificationBlocks;
	}

	public Calendar getCalendar()
	{
		return calendar;
	}
	public String getID()
	{
		return stationID;
	}

	public static ArrayList<Calendar> getReminderForID(String id, Context context) {
		ArrayList<NotificationBlock> list = readArrayListFromFile("notificationBlockDB", context);
		if (list==null) return new ArrayList<Calendar>();
		ArrayList<Calendar> timesList = new ArrayList<Calendar>();
		for (NotificationBlock nb : list)
		{
			if (nb.getID().equals(id))
			{
				timesList.add(nb.getCalendar());
			}
		}
		return timesList;
	}
}
