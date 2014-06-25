package smartcampus.notifications;

import java.util.ArrayList;

import smartcampus.activity.MainActivity;
import smartcampus.model.NotificationBlock;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{	
	@Override
	public void onReceive(Context context, Intent arg1)
	{
		ArrayList<NotificationBlock> notificationBlocks = NotificationBlock.readArrayListFromFile(MainActivity.FILENOTIFICATIONDB, context);
		
		for(NotificationBlock nb : notificationBlocks)
		{
			new NotificationReceiver().registerAlarm(context, nb.getCalendar(), nb.getUniqueID(), nb.getID());
		}
		
	}
}