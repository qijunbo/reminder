package home.tony.reminder.settings;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.common.Consts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.text.format.DateFormat;

public class Configuration {

	public static final String PROFILE = "profile.txt";
	private Context context;

	private File file;

	public Configuration(Context context) {
		this.context = context;
		file = new File(context.getFilesDir(), PROFILE);
	}

	/**
	 * load alarm configuration from saved file, if no file ever saved, then
	 * load default.
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Alarm> load() throws IOException {
		InputStream is;

		if (file.exists()) {
			// Load from memory of phone
			is = context.openFileInput(PROFILE);
		} else {
			// Load default from jar file
			is = Configuration.class.getResourceAsStream(PROFILE);
		}

		List<Alarm> alarms = load(is);
		Collections.sort(alarms);
		return alarms;

	}

	/**
	 * load alarm configuration from specified input stream.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public List<Alarm> load(InputStream is) throws IOException {
		List<Alarm> config = new ArrayList<Alarm>();
		Properties properties = new Properties();
		properties.load(is);
		for (Object value : properties.values()) {
			Alarm alarm = Alarm.decode(value.toString());
			if (alarm != null) {
				config.add(alarm);
			}
		}
		return config;
	}

	/**
	 * Save user's alarm in to memory of phone.
	 * 
	 * @throws IOException
	 */
	public void save(Collection<Alarm> configs) throws IOException {
		FileOutputStream fos = context.openFileOutput(PROFILE, Context.MODE_PRIVATE);
		save(configs, fos);
	}

	public void save(Collection<Alarm> config, OutputStream oStream) throws IOException {
		Properties properties = new Properties();
		for (Alarm configure : config) {
			properties.put(String.valueOf(configure.getId()), configure.encode());
		}
		properties.store(oStream, DateFormat.format(Consts.DATE_TIME, new Date()).toString());

	}
}
