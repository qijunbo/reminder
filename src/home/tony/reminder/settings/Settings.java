package home.tony.reminder.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	private static final String AUTOREMOVEEXPIREDITEM = "autoRemoveExpiredItem";

	private static final String DIALOGENABLE = "dialogEnable";

	private static final String NOTIFYBARENABLE = "notifyBarEnable";

	private static final String NOTIFYINADVANCE = "notifyInAdvance";

	private static final String PLAYSOUND = "playSound";

	private static final String VIBRATE = "vibrate";

	private boolean autoRemoveExpiredItem = true;

	private boolean dialogEnable = true;

	private boolean notifyBarEnable = true;

	private boolean notifyInAdvance = false;

	private boolean playSound = true;

	private boolean vibrate = false;

	private Context context;

	private Settings(Context context) {

		this.context = context;
		load();
	}

	private void load() {

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		setNotifyInAdvance(sharedPref.getBoolean(NOTIFYINADVANCE, false));
		setNotifyBarEnable(sharedPref.getBoolean(NOTIFYBARENABLE, true));
		setDialogEnable(sharedPref.getBoolean(DIALOGENABLE, true));
		setAutoRemoveExpiredItem(sharedPref.getBoolean(AUTOREMOVEEXPIREDITEM, false));
		setPlaySound(sharedPref.getBoolean(PLAYSOUND, true));
		setVibrate(sharedPref.getBoolean(VIBRATE, false));
	}

	private static Settings settings;

	public static Settings getInstance(Context context) {
		if (settings == null) {
			settings = new Settings(context);
		}
		return settings;
	}

	public String toString(){

		return AUTOREMOVEEXPIREDITEM + ":" + this.isAutoRemoveExpiredItem() + "\n" +
				DIALOGENABLE + ":" + this.isDialogEnable() + "\n" +
				NOTIFYBARENABLE + ":" + this.isNotifyBarEnable() + "\n" +
				NOTIFYINADVANCE + ":" + this.isNotifyInAdvance() + "\n" +
				PLAYSOUND  + ":" + this.isPlaySound() + "\n" +
				VIBRATE  + ":" + this.isVibrate() + "\n" ;
				
	}
	
	/**
	 * @return the autoRemoveExpiredItem
	 */
	public boolean isAutoRemoveExpiredItem() {
		return autoRemoveExpiredItem;
	}

	/**
	 * @param autoRemoveExpiredItem
	 *            the autoRemoveExpiredItem to set
	 */
	public void setAutoRemoveExpiredItem(boolean autoRemoveExpiredItem) {
		this.autoRemoveExpiredItem = autoRemoveExpiredItem;
	}

	/**
	 * @return the dialogEnable
	 */
	public boolean isDialogEnable() {
		return dialogEnable;
	}

	/**
	 * @param dialogEnable
	 *            the dialogEnable to set
	 */
	public void setDialogEnable(boolean dialogEnable) {
		this.dialogEnable = dialogEnable;
	}

	/**
	 * @return the notifyBarEnable
	 */
	public boolean isNotifyBarEnable() {
		return notifyBarEnable;
	}

	/**
	 * @param notifyBarEnable
	 *            the notifyBarEnable to set
	 */
	public void setNotifyBarEnable(boolean notifyBarEnable) {
		this.notifyBarEnable = notifyBarEnable;
	}

	/**
	 * @return the notifyInAdvance
	 */
	public boolean isNotifyInAdvance() {
		return notifyInAdvance;
	}

	/**
	 * @param notifyInAdvance
	 *            the notifyInAdvance to set
	 */
	public void setNotifyInAdvance(boolean notifyInAdvance) {
		this.notifyInAdvance = notifyInAdvance;
	}

	/**
	 * @return the playSound
	 */
	public boolean isPlaySound() {
		return playSound;
	}

	/**
	 * @param playSound
	 *            the playSound to set
	 */
	public void setPlaySound(boolean playSound) {
		this.playSound = playSound;
	}

	/**
	 * @return the vibrate
	 */
	public boolean isVibrate() {
		return vibrate;
	}

	/**
	 * @param vibrate
	 *            the vibrate to set
	 */
	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

}
