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
	public void onReceive(Context arg0, Intent arg1)
	{
		ArrayList<NotificationBlock> notificationBlocks = NotificationBlock.readArrayListFromFile(MainActivity.FILENOTIFICATIONDB, arg0);
		
		for(NotificationBlock nb : notificationBlocks)
		{
			AlarmManager alarmMgr;
			PendingIntent alarmIntent;
			alarmMgr = (AlarmManager) arg0.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(arg0, NotificationReceiver.class);
			alarmIntent = PendingIntent.getBroadcast(arg0, 0, intent, 0);
			
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, nb.getCalendar().getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
		}
		
	}
}