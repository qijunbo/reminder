package home.tony.reminder.onboot;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.alarm.AlarmManagerExtension;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class will add a clock(Alarm) configuration to system as soon as it
 * received a broadcast from the class ConfigureLoadingService.
 * 
 * @author qijunbo
 * 
 */
public class SetAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "BroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Message alamr received.");
		//get extra data from intent and decode it to a Alarm object.
		String msg = intent.getStringExtra(ConfigureLoadingService.EXTENDED_DATA);
		Alarm alarm = Alarm.decode(msg);
		
		//add an alarm configuration to system.
		AlarmManagerExtension manager = new AlarmManagerExtension(context);
		manager.addAlarm(alarm);
		Log.d(TAG, "Message alamr consumed.");
	}

}
