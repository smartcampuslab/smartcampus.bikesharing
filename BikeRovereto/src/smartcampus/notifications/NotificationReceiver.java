package smartcampus.notifications;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.util.GetStationsTask;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import eu.trentorise.smartcampus.bikerovereto.R;

public class NotificationReceiver extends BroadcastReceiver
{

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	public void registerAlarm(Context context, Calendar when, int uniqueID, String stationID)
	{
		alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, NotificationReceiver.class);
		intent.putExtra("stationID", stationID);
		alarmIntent = PendingIntent.getBroadcast(context, uniqueID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// wait 10 seconds and notify
		// alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// SystemClock.elapsedRealtime() + 1000 * 10, alarmIntent);

		// notify at the exact time
		// alarmMgr.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),
		// alarmIntent);
		Log.d("prova", stationID);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
	}
	
	public void updateAlarm(Context context, NotificationBlock notificationBlock, String stationID)
	{
		registerAlarm(context, notificationBlock.getCalendar(), notificationBlock.getUniqueID(), stationID);
	}
	
	public void cancelAlarm(Context context, int uniqueID, String stationID)
	{
		Intent intent = new Intent(context, NotificationReceiver.class);
		intent.putExtra("stationID", stationID);
		alarmIntent = PendingIntent.getBroadcast(context, uniqueID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if (alarmMgr == null)
		{
			alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		}
		alarmMgr.cancel(alarmIntent);
	}

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		Station station = null;
		String stationID = arg1.getStringExtra("stationID");
		try
		{
			station = new GetStationsTask(arg0).execute(stationID).get(10000, TimeUnit.MILLISECONDS).get(0);
			// define sound URI, the sound to be played when there's a
			// notification
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			// this is it, we'll build the notification!
			// in the addAction method, if you don't want any icon, just set the
			// first param to 0
			Resources res = arg0.getResources();
			Notification mNotification = new Notification.Builder(arg0).setContentTitle(res.getText(R.string.station) + " " + station.getName().toUpperCase()).setContentText(
					res.getText(R.string.sort_available_bikes) + ": " + station.getNBikesPresent() + " - " + res.getText(R.string.sort_available_slots) + ": " + station.getNSlotsEmpty()).setSmallIcon(
					R.drawable.ic_launcher).setContentIntent(alarmIntent).setSound(soundUri)

			.addAction(R.drawable.ic_launcher, "View", alarmIntent).addAction(0, "Remind", alarmIntent)

			.build();

			NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);

			// If you want to hide the notification after it was selected, do
			// the
			// code below
			mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(0, mNotification);

		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}