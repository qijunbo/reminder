package home.tony.reminder.common;

import java.io.File;

import android.os.Environment;

public class FileHelper {

	private static final String Directory = "reminder";

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public static String getSdPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	public static String getConfigPath() {
		return getSdPath() + File.separator + Directory;
	}

}
