package home.tony.reminder.event;

import home.tony.reminder.alarm.Alarm;

public class ContextEvent {

	public static enum Action {
		Add, Remove
	};

	Alarm alarm;

	Action action;

	public ContextEvent(Alarm alarm, Action action) {
		super();
		this.alarm = alarm;
		this.action = action;
	}

	public ContextEvent() {

	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
