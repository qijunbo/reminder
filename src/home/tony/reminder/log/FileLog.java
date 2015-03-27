package home.tony.reminder.log;

import home.tony.reminder.common.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

public class FileLog {

	private static final String TAG = FileLog.class.getName();

	private static String path;

	private static FileOutputStream fos;

	private static boolean initlized = false;

	public enum Level {
		D, E, I, V, W
	};

	private static synchronized void write(Level level, String tag, CharSequence info) {
		if (!BuildConfig.DEBUG) {
			return;
		}

		if (initlized) {
			try {
				fos.write((level.name() + "\t" + tag + "\t" + info.toString() + "\t\r\n").getBytes());
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
			}
		} else {
			path = FileHelper.getConfigPath() + File.separator + "log.txt";
			File file = new File(path);
			if (FileHelper.isExternalStorageWritable()) {
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					fos = new FileOutputStream(path);
					fos.write((byte) 10);
					initlized = true;
				} catch (FileNotFoundException e) {
					Log.v(TAG, e.getMessage());
				} catch (IOException e) {
					Log.v(TAG, e.getMessage());
				}
			}
		}
	}

	public static void d(String tag, CharSequence info) {
		write(Level.D, tag, info);
	}

	public static void e(String tag, CharSequence info) {
		write(Level.E, tag, info);
	}

	public static void i(String tag, CharSequence info) {
		write(Level.I, tag, info);
	}

	public static void v(String tag, CharSequence info) {
		write(Level.V, tag, info);
	}

	public static void w(String tag, CharSequence info) {
		write(Level.W, tag, info);
	}

}
