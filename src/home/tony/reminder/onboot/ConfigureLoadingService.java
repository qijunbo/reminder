package home.tony.reminder.onboot;

import home.tony.reminder.R;
import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.alarm.AlarmUtil;
import home.tony.reminder.settings.Configuration;
import home.tony.reminder.settings.Settings;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ConfigureLoadingService extends IntentService {

	private static final String TAG = "ConfigureLoadingService";
	public static final String BROADCAST_ACTION = "ConfigureLoadingService_Broadcast_Action";
	public static final String EXTENDED_DATA = "Extra_Data_From_ConfigureLoadingService";

	private Configuration configuration;
	private Settings settings;
	private LocalBroadcastManager broadcastManager;

	public ConfigureLoadingService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Service started!");

		try {

			configuration = new Configuration(this);
			settings = Settings.getInstance(this);
			broadcastManager = LocalBroadcastManager.getInstance(this);

			IntentFilter mStatusIntentFilter = new IntentFilter(BROADCAST_ACTION);
			// Instantiates a new SetAlarmReceiver
			SetAlarmReceiver mSetAlarmReceiver = new SetAlarmReceiver();
			// Registers the SetAlarmReceiver and its intent filters
			broadcastManager.registerReceiver(mSetAlarmReceiver, mStatusIntentFilter);

			List<Alarm> alarms = configuration.load();

			for (Alarm alarm : alarms) {

				// if user select to auto remove the expired alarm item,
				// then do not add the expired ones in to application
				// context.
				if (settings.isAutoRemoveExpiredItem() && AlarmUtil.getAlarmTime(alarm) == 0) {
					continue;
				}
				// Broadcasts the Intent to receivers in this app.
				Intent localIntent = new Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA, alarm.encode());
				broadcastManager.sendBroadcast(localIntent);
			}
			
			if (settings.isNotifyBarEnable()) {
				createNotification();
			}
			
			//Toast.makeText(this, settings.toString(), Toast.LENGTH_LONG).show();
			
			Log.d(TAG, "Service finished!");

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	private void createNotification() {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentTitle(getText(R.string.app_name))

		.setContentText(getText(R.string.text_onboot_loadfinished))

		.setSmallIcon(R.drawable.ic_launcher)

		.setDefaults(Notification.DEFAULT_SOUND)

		.setAutoCancel(true);

		Notification notification = builder.build();
		manager.notify(2015, notification);
	}

}
