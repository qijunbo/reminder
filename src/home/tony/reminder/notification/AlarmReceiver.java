package home.tony.reminder.notification;

import home.tony.reminder.AlarmPopupActivity;
import home.tony.reminder.R;
import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.alarm.AlarmUtil;
import home.tony.reminder.common.Consts;
import home.tony.reminder.settings.Settings;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends WakefulBroadcastReceiver {

	private static final String TAG = "BroadcastReceiver";

	private Context context;

	private Alarm alarm;

	private String msg;

	private long expectTime;

	private Settings settings;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(TAG, "Alarm received.");
		
		this.context = context;
		settings = Settings.getInstance(context);
		msg = intent.getStringExtra(Consts.REQUEST);
		alarm = Alarm.decode(msg);
		expectTime = intent.getLongExtra(Consts.EXPECTED_ALARM_TIME, 0);
		
		createNotification();
		refreshStatusOnUI();
		
		Log.d(TAG, "Alarm finished.");
	}

	private void refreshStatusOnUI() {
		// TODO Auto-generated method stub

	}

	private void createNotification() {

		Intent newIntent = new Intent(context, AlarmPopupActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		newIntent.putExtra(Consts.REQUEST, msg);
		newIntent.putExtra(Consts.EXPECTED_ALARM_TIME, expectTime);

		int requestCode = (int) alarm.getId();
		String title = alarm.getTitle();
		PendingIntent intent = PendingIntent.getActivity(context, requestCode, newIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentTitle(title)

		.setContentText(AlarmUtil.getDetail(context, alarm))

		.setSmallIcon(R.drawable.ic_launcher)

		.setContentIntent(intent)

		.setAutoCancel(true);

		if (settings.isVibrate()) {
			long[] pattern = new long[] { 500L, 500L, 500L, 600L, 500L, 700L };
			builder.setVibrate(pattern);
		}

		if (settings.isDialogEnable()) {
			builder.setFullScreenIntent(intent, true);
		}

		if (settings.isPlaySound()) {
			Uri sound = Uri.parse("android.resource://home.tony.reminder/raw/" + R.raw.simplelow);
			builder.setSound(sound);
		} else {
			// do not play this sound if user choose to play music.
			builder.setDefaults(Notification.DEFAULT_SOUND);
		}

		Notification notification = builder.build();
		manager.notify(requestCode, notification);
	}

	/**
	 * This method will not be used any more.
	 */
	// private void popUpDialog() {
	// if (!settings.isDialogEnable() && !settings.isPlaySound()) {
	// return;
	// }
	//
	// Intent newIntent = new Intent(context, AlarmPopupActivity.class);
	// newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// newIntent.putExtra(Consts.REQUEST, msg);
	// context.startActivity(newIntent);
	// }

	/**
	 * This method will not be used any more.
	 */
	// private void vibrate() {
	// if (settings.isVibrate()) {
	// vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
	// if (vibrator.hasVibrator()) {
	// long[] pattern = new long[] { 500L, 500L };
	// vibrator.vibrate(pattern, 0);
	// }
	// }
	// }

}
