package home.tony.reminder.event;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.event.ContextEvent.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppContext {

	private static List<ContextEventListener> listeners = new ArrayList<ContextEventListener>();

	private static Map<String, Alarm> alarms = new HashMap<String, Alarm>();

	private static AppContext instance = new AppContext();

	public static AppContext getInstance() {
		return instance;
	}

	private AppContext() {
	}

	public void put(Alarm alarm) {
		String key = String.valueOf(alarm.getId());
		alarms.put(key, alarm);
		ContextEvent event = new ContextEvent(alarm, Action.Add);
		fireContextEvent(event);
	}

	private void fireContextEvent(ContextEvent event) {
		for (ContextEventListener listener : listeners) {
			listener.onContextChange(event);
		}
	}

	public void remove(String key) {
		Alarm alarm = alarms.get(key);
		alarms.remove(key);
		ContextEvent event = new ContextEvent(alarm, Action.Remove);
		fireContextEvent(event);
	}

	public void addContextEventListener(ContextEventListener listener) {

		listeners.add(listener);
	}

	public void removeContextEventListener(ContextEventListener listener) {
		listeners.remove(listener);
	}

	public static Collection<Alarm> getAlarms() {
		return alarms.values();
	}

}
