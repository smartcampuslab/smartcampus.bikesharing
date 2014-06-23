package smartcampus.notifications;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import eu.trentorise.smartcampus.bikerovereto.R;

public class MyReceiver extends BroadcastReceiver
{

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	public void registerAlarm(Context context, Calendar when)
	{

		Log.d("prova", when.toString());
		alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MyReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		// wait 10 seconds and notify
		// alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// SystemClock.elapsedRealtime() + 1000 * 10, alarmIntent);

		// notify at the exact time
		// alarmMgr.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),
		// alarmIntent);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
	}

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		// define sound URI, the sound to be played when there's a notification
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the
		// first param to 0
		Notification mNotification = new Notification.Builder(arg0)

		.setContentTitle(arg0.getResources().getText(R.string.app_name)).setContentText(arg0.getResources().getText(R.string.sort_available_bikes)).setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(alarmIntent).setSound(soundUri)

				.addAction(R.drawable.ic_launcher, "View", alarmIntent).addAction(0, "Remind", alarmIntent)

				.build();

		NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);

		// If you want to hide the notification after it was selected, do the
		// code below
		// mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, mNotification);

	}
}