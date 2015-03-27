package home.tony.reminder.event;

import home.tony.reminder.alarm.Alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Request {

	long id = 0L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	int month;

	int dayOfMonth;

	int hour;

	int minute;

	Date expireDate = Alarm.NEVER;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(int year, int month, int day) {
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.set(year, month, day);
		this.expireDate = calendar.getTime();
	}

}
