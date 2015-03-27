package home.tony.reminder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.widget.CheckBox;

public class WeekDayAdapter {

	private final List<Integer> ids = new ArrayList<Integer>();

	private Set<Integer> selectedDays = new HashSet<Integer>(7);

	public void setSelectedDays(Integer[] daysOfWeek, Activity activity) {
		for (Integer day : daysOfWeek) {
			selectedDays.add(day);
			CheckBox checkBox = (CheckBox) activity.findViewById(ids.get(day
					.intValue()));
			if (checkBox != null) {
				checkBox.setChecked(true);
			}
		}
	}

	public WeekDayAdapter() {
		// this.context = context;
		ids.add(R.id.day0);
		ids.add(R.id.day1);
		ids.add(R.id.day2);
		ids.add(R.id.day3);
		ids.add(R.id.day4);
		ids.add(R.id.day5);
		ids.add(R.id.day6);
	}

	public void add(Integer id) {
		selectedDays.add(ids.indexOf(id));
	}

	public void remove(Integer id) {
		selectedDays.remove(ids.indexOf(id));
	}

	public Integer[] getSelectedDays() {
		if (selectedDays.size() > 0) {
			return selectedDays.toArray(new Integer[selectedDays.size()]);
		} else {
			return null;
		}
	}

}
