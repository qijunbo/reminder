package home.tony.reminder.export;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.event.AppContext;
import home.tony.reminder.settings.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ImportExportService {

	private static final String TAG = "ImportExportService";
	private Configuration configuration;
	private Context context;

	public ImportExportService(Context context) {
		this.context = context;
		configuration = new Configuration(context);
	}

	public void importFrom(String file) {
		File f = new File(file);
		if (!f.exists()) {
			Toast.makeText(context, "Cant not find file at: " + file, Toast.LENGTH_LONG).show();
		}

		try {

			FileInputStream fis = new FileInputStream(file);
			List<Alarm> alarms = configuration.load(fis);
			for (int i = alarms.size() - 1; i >= 0; i--) {
				Alarm alarm = alarms.get(i);
				AppContext.getInstance().put(alarm);
			}

		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());

		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public void exportTo(String file, Collection<Alarm> alarms) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			configuration.save(alarms, fos);
			Toast.makeText(context, "File exported to: " + file, Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

	}
}
