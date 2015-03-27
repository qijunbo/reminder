package home.tony.reminder.alarm;

import home.tony.reminder.AlarmActivity;
import home.tony.reminder.MainActivity;
import home.tony.reminder.R;
import home.tony.reminder.common.Consts;
import home.tony.reminder.event.AppContext;
import home.tony.reminder.event.ContextEvent;
import home.tony.reminder.event.ContextEventListener;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmListAdapter extends BaseAdapter implements ContextEventListener, OnItemClickListener,
		OnItemLongClickListener {

	private MainActivity context;

	private Set<Alarm> selectedItems = new HashSet<Alarm>();

	private boolean editModel = false;

	/**
	 * The reason we need to copy the Alarms in the ApplicationContext is the
	 * Alarms in it is not in order, but we need the Alarms in order to make the
	 * adapter work.
	 */
	private List<Alarm> alarmList = new LinkedList<Alarm>();

	public AlarmListAdapter(MainActivity context) {
		this.context = context;

	}

	private void drawItem(int position, View itemView, ViewGroup container) {
		try {
			Alarm alarm = alarmList.get(position);

			ImageView imageView = (ImageView) itemView.findViewById(R.id.alarm_item_logo);
			imageView.setImageResource(getLogo(alarm));

			TextView title = (TextView) itemView.findViewById(R.id.alarm_item_title);
			title.setText(alarm.getTitle());

			TextView nextalarm = (TextView) itemView.findViewById(R.id.alarm_item_next);
			long alarmTime = AlarmUtil.getAlarmTime(alarm);

			if (alarmTime > 0) {
				nextalarm.setText(DateFormat.format(Consts.DATE_TIME, alarmTime));
			} else {
				nextalarm.setText(context.getString(R.string.text_expired));
			}

			TextView detail = (TextView) itemView.findViewById(R.id.alarm_item_detail);
			detail.setText(AlarmUtil.getDetail(context, alarm));

			if (selectedItems.contains(alarmList.get(position))) {
				itemView.setBackgroundColor(Color.argb(125, 30, 30, 150));
			} else {
				itemView.setBackgroundColor(Color.TRANSPARENT);
			}

		} catch (RuntimeException e) {

			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {

		return alarmList.size();
	}

	@Override
	public Object getItem(int position) {

		try {
			return alarmList.get(position);
		} catch (RuntimeException e) {

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int position) {

		try {
			return alarmList.get(position).getId();
		} catch (RuntimeException e) {
			return 0;
		}
	}

	private int getLogo(Alarm alarm) {
		switch (alarm.getPeriodType()) {
		case Once:
			return R.drawable.img_question;
		case Daily:
			return R.drawable.img_clock;
		case Weekly:
			return R.drawable.img_weekly;
		case Monthly:
			return R.drawable.img_monthly;
		case Annual:
			return R.drawable.img_annual;
		default:
			return R.drawable.img_default;
		}
	}

	@Override
	public View getView(int position, View itemView, ViewGroup container) {

		if (itemView == null) {
			itemView = context.getLayoutInflater().inflate(R.layout.alarm_item, container, false);
		}
		drawItem(position, itemView, container);
		return itemView;
	}

	@Override
	public void onContextChange(ContextEvent event) {

		if (event.getAction().equals(ContextEvent.Action.Add)) {
			alarmList.add(0, event.getAlarm());
			notifyDataSetChanged();
		} else {
			alarmList.remove(event.getAlarm());
			notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Alarm alarm = alarmList.get(position);
		if (editModel) {
			// If selected, make it unselected; if not selected, select it.
			if (selectedItems.contains(alarm)) {
				selectedItems.remove(alarm);
			} else {
				selectedItems.add(alarm);
			}
			if (selectedItems.size() == 0) {
				editModel = false;
				context.setMenuItemEnable(editModel);
			}
			notifyDataSetInvalidated();
		} else {

			startEditActivity(alarm);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		editModel = true;
		context.setMenuItemEnable(editModel);
		selectedItems.add(alarmList.get(position));
		notifyDataSetInvalidated();
		return true;
	}

	private void startEditActivity(Alarm alarm) {

		Intent edit = new Intent(context, AlarmActivity.class);
		edit.putExtra(AlarmActivity.EDIT_MODEL, AlarmActivity.EDIT_MODEL);
		edit.putExtra(Consts.REQUEST, alarm.encode());
		edit.putExtra(Consts.TYPE, alarm.getPeriodType().name());
		context.startActivity(edit);
	}

	public void removeSelectedItems() {
		for (Alarm a : selectedItems) {
			AppContext.getInstance().remove(String.valueOf(a.getId()));
		}
		selectedItems.clear();
		editModel = false;
		context.setMenuItemEnable(editModel);
		notifyDataSetChanged();
	}

}
