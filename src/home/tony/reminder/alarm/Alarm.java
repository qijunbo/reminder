package home.tony.reminder.alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;

public class Alarm implements Comparable<Alarm> {

	public static final long LONG_NEVER = Long.MAX_VALUE - 1000;
	public static final Date NEVER = new Date(Long.MAX_VALUE - 1);

	public enum PeriodType {
		Annual, Daily, Monthly, Once, Weekly, Remote
	}

	public static String BAR = "|";

	public static Alarm createDefault(PeriodType type) {
		Alarm alarm = new Alarm();
		final Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.set(Calendar.SECOND, 0);

		alarm.setPeriodType(type);
		alarm.setTitle(null);
		alarm.setDetail(null);
		alarm.setDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
		alarm.setDaysOfWeek(new Integer[] { 2, 3, 4, 5, 6 });
		if (PeriodType.Once.equals(type) || PeriodType.Remote.equals(type)) {
			alarm.setExpireTime(new Date());
		} else {
			alarm.setExpireTime(NEVER);
		}
		alarm.setHourOfDay(cal.get(Calendar.HOUR_OF_DAY));
		alarm.setMinutes(cal.get(Calendar.MINUTE));
		alarm.setMonth(cal.get(Calendar.MONTH));
		alarm.setSeconds(0);
		alarm.setCapture(null);
		return alarm;
	}

	public static Alarm decode(String alarm) {
		Gson gson = new Gson();
		return gson.fromJson(alarm, Alarm.class);
	}

	private String capture = "";

	private int dayOfMonth = 0;

	private Integer[] daysOfWeek;

	private String detail;

	private Date expireTime;

	private int hourOfDay;

	private long id;

	private int minutes;

	private int month = 0;

	private PeriodType periodType;

	private int seconds;

	private String title;

	private String from;

	public Alarm() {
		this.id = (int)(System.currentTimeMillis() & 0xFFFFL);
	}

	@Override
	public int compareTo(Alarm another) {

		if (this == another) {
			return 0;
		}

		long result = AlarmUtil.getAlarmTime(this) - AlarmUtil.getAlarmTime(another);

		if (result > 0) {
			return 1;
		}
		if (result < 0) {
			return -1;
		}

		return 0;

	}

	public String encode() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String getCapture() {
		return capture;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public Integer[] getDaysOfWeek() {
		return daysOfWeek;
	}

	public String getDetail() {
		return detail;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public String getFrom() {
		return from;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public long getId() {
		return id & 0xFFFFL;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getMonth() {
		return month;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public int getSeconds() {
		return seconds;
	}

	public String getTitle() {
		return title;
	}

	public void setCapture(String capture) {
		this.capture = capture;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public void setDaysOfWeek(Integer[] daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public void setId(long id) {
		this.id = id & 0xFFFFL;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return encode();
	}

}
