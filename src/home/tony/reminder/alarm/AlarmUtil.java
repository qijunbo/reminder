package home.tony.reminder.alarm;

import home.tony.reminder.R;
import home.tony.reminder.common.Consts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;

/**
 * The class AlarmUtil is used to calculate the recent alarm time of a Alarm
 * configuration.
 * 
 * @author qijunbo
 */
public class AlarmUtil {

	public static String getDetail(Context context, Alarm alarm) {
		if (Consts.isEmpty(alarm.getDetail())) {
			switch (alarm.getPeriodType()) {
			case Once:
				return context.getString(R.string.text_detail_once);
			case Daily:
				return context.getString(R.string.text_detail_daily);
			case Weekly:
				return context.getString(R.string.text_detail_weekly);
			case Monthly:
				return context.getString(R.string.text_detail_monthly);
			case Annual:
				return context.getString(R.string.text_detail_annual);
			case Remote:
				break;
			default:
				break;
			}
		}
		return alarm.getDetail();
	}

	// TODO bug： you have no chance reload the configure file to get the next
	// alarm time if user doesn't reboot the phone.
	public static long getAlarmTime(Alarm alarm) {
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		// 把秒针设置为0，也就是说只精确到分钟。
		calendar.set(Calendar.SECOND, 0);

		// return null if the alarm is expired
		if (alarm.getExpireTime().before(calendar.getTime())) {
			return 0;
		}

		long alarmtime = 0;

		switch (alarm.getPeriodType()) {
		case Once:
			alarmtime = getAlarmTimeOfOnce(alarm);
			break;
		case Daily:
			alarmtime = getAlarmTimeOfDaily(alarm, calendar);
			break;
		case Weekly:
			alarmtime = getAlarmTimeOfWeekly(alarm, calendar);
			break;
		case Monthly:
			alarmtime = getAlarmTimeOfMonthly(alarm, calendar);
			break;
		case Annual:
			alarmtime = getAlarmTimeOfAnnual(alarm, calendar);
			break;
		default:
			return 0;
		}
		return alarmtime;
	}

	private static long getAlarmTimeOfAnnual(Alarm alarm, final Calendar calendar) {
		calendar.set(Calendar.MONTH, alarm.getMonth());
		calendar.set(Calendar.DAY_OF_MONTH, alarm.getDayOfMonth());
		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		calendar.set(Calendar.MINUTE, alarm.getMinutes());
		long alarmtime = calendar.getTimeInMillis();
		if (alarmtime < new Date().getTime()) {
			calendar.roll(Calendar.YEAR, 1);
			alarmtime = calendar.getTimeInMillis();
		}

		return alarmtime;
	}

	private static long getAlarmTimeOfMonthly(Alarm alarm, final Calendar calendar) {

		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		calendar.set(Calendar.MINUTE, alarm.getMinutes());
		calendar.set(Calendar.DAY_OF_MONTH, alarm.getDayOfMonth());
		long alarmtime = calendar.getTimeInMillis();
		if (alarmtime < new Date().getTime()) {
			calendar.roll(Calendar.MONTH, 1);
			alarmtime = calendar.getTimeInMillis();
		}
		return alarmtime;
	}

	private static long getAlarmTimeOfWeekly(Alarm alarm, final Calendar calendar) {
		List<Long> times = new ArrayList<Long>();
		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		calendar.set(Calendar.MINUTE, alarm.getMinutes());

		for (int day : alarm.getDaysOfWeek()) {
			calendar.set(Calendar.DAY_OF_WEEK, day);
			if (calendar.getTimeInMillis() < new Date().getTime()) {
				times.add(calendar.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000);
			} else {
				times.add(calendar.getTimeInMillis());
			}
		}
		Collections.sort(times);
		return times.get(0);
	}

	private static long getAlarmTimeOfDaily(Alarm alarm, final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		calendar.set(Calendar.MINUTE, alarm.getMinutes());
		long alarmtime = calendar.getTimeInMillis();
		if (alarmtime < new Date().getTime()) {
			calendar.roll(Calendar.DAY_OF_YEAR, 1);
			alarmtime = calendar.getTimeInMillis();
		}
		return alarmtime;
	}

	private static long getAlarmTimeOfOnce(Alarm alarm) {
		return alarm.getExpireTime().getTime();
	}

}
