package home.tony.reminder.onboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * This BroadcastReceiver will start on boot, to call ConfigureLoadingService to
 * load the alarm configurations from the disk.
 * home.tony.reminder.onboot.BootNotificationReceiver
 * 
 * @author qijunbo
 * 
 */
public class BootNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
				Intent service = new Intent(context, ConfigureLoadingService.class);
				context.startService(service);
			}
		} catch (Throwable e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
