package home.tony.reminder.alarm;

import home.tony.reminder.common.Consts;
import home.tony.reminder.event.ContextEvent;
import home.tony.reminder.event.ContextEventListener;
import home.tony.reminder.notification.AlarmReceiver;
import home.tony.reminder.settings.Settings;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerExtension implements ContextEventListener {

	private Context context;
	private AlarmManager manager;
	private Settings settings;

	public AlarmManagerExtension(Context context) {

		this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.context = context;
		this.settings = Settings.getInstance(context);
	}

	public void addAlarm(Alarm alarm) {
		Log.d("AlarmManagerExtension", "add alarm started.");
		long alarmtime = AlarmUtil.getAlarmTime(alarm);
		if (alarmtime > System.currentTimeMillis()) {
			PendingIntent pendingIntent = createPendingIntent(alarm, alarmtime);

			// user expect the alarm ring 15 minutes in advance.
			if (settings.isNotifyInAdvance()) {
				alarmtime = alarmtime - 15 * 60 * 1000;
			}

			manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmtime, 5 * 60 * 1000, pendingIntent);
		}
		Log.d("AlarmManagerExtension", "add alarm finished.");

	}

	public void cancelAlarm(Alarm alarm) {
		PendingIntent pendingIntent = retrievePendingIntent(alarm);
		if (pendingIntent != null) {
			manager.cancel(pendingIntent);
		}

	}

	private Intent createIntent(Alarm alarm, long expectedTime) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra(Consts.REQUEST, alarm.encode());
		intent.putExtra(Consts.EXPECTED_ALARM_TIME, expectedTime);
		return intent;
	}

	private PendingIntent createPendingIntent(Alarm alarm, long expectedTime) {
		Intent intent = createIntent(alarm, expectedTime);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	@Override
	public void onContextChange(ContextEvent event) {

		if (ContextEvent.Action.Add.equals(event.getAction())) {
			addAlarm(event.getAlarm());
		} else if (ContextEvent.Action.Remove.equals(event.getAction())) {
			cancelAlarm(event.getAlarm());
		}

	}

	private PendingIntent retrievePendingIntent(Alarm alarm) {
		Intent intent = createIntent(alarm, 0);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent,
				PendingIntent.FLAG_NO_CREATE);
		return pendingIntent;
	}
}
