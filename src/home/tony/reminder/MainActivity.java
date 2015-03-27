package home.tony.reminder;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.alarm.AlarmListAdapter;
import home.tony.reminder.alarm.AlarmManagerExtension;
import home.tony.reminder.alarm.AlarmUtil;
import home.tony.reminder.common.Consts;
import home.tony.reminder.common.FileHelper;
import home.tony.reminder.event.AppContext;
import home.tony.reminder.export.ImportExportService;
import home.tony.reminder.settings.Configuration;
import home.tony.reminder.settings.Settings;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AlarmListAdapter alarmListAdapter;
	private Configuration configuration;
	private Menu menu;
	private ImportExportService service;
	private Settings settings;

	public MainActivity() {
		alarmListAdapter = new AlarmListAdapter(this);
	}

	private void actionHelp() {
		Intent intent = new Intent(this, CommonActivity.class);
		startActivity(intent);

	}

	private void actionOpenSetting() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void actionPopOptionsDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.title_activity_menu).setIcon(R.drawable.ic_action_view_as_list)
				.setSingleChoiceItems(R.array.PeriodicalTypes, 0, new ItemClickEventAdapter(MainActivity.this)).show();
	}

	private void actionRemoveSelected() {
		alarmListAdapter.removeSelectedItems();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		configuration = new Configuration(this);
		settings = Settings.getInstance(this);
		service = new ImportExportService(this);
		ListView listView = (ListView) findViewById(R.id.alarmList);
		listView.setAdapter(alarmListAdapter);
		listView.setOnItemClickListener(alarmListAdapter);
		listView.setOnItemLongClickListener(alarmListAdapter);

		AppContext.getInstance().addContextEventListener(alarmListAdapter);
		// ApplicationContext.setContext(this);
		// add alarmManagerExtension as ContextEventListener
		AppContext.getInstance().addContextEventListener(new AlarmManagerExtension(this));

		try {
			List<Alarm> alarms = configuration.load();
			for (int i = alarms.size() - 1; i >= 0; i--) {
				Alarm alarm = alarms.get(i);
				if (settings.isAutoRemoveExpiredItem() && AlarmUtil.getAlarmTime(alarm) == 0) {
					continue;
				}
				AppContext.getInstance().put(alarm);
			}

		} catch (Exception e) {
			showMessage(e.getMessage());
		}

		if (alarmListAdapter.getCount() == 0) {
			this.actionPopOptionsDialog();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		setMenuItemEnable(false);
		return true;
	}

	@Override
	protected void onStop() {
		try {
			configuration.save(AppContext.getAlarms());
		} catch (Exception e) {
			showMessage(e.getMessage());
		}
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send:
			sendToFriend();
			return true;
		case R.id.action_new:
			actionPopOptionsDialog();
			return true;
		case R.id.action_settings:
			actionOpenSetting();
			return true;
		case R.id.action_delete:
			actionRemoveSelected();
			return true;
		case R.id.action_help:
			actionHelp();
			return true;
		case R.id.action_import:
			service.importFrom(FileHelper.getConfigPath() + File.separator + Configuration.PROFILE);
			return true;
		case R.id.action_export:
			service.exportTo(FileHelper.getConfigPath() + File.separator + Configuration.PROFILE,
					AppContext.getAlarms());
			return true;
		default:
			return true;
		}

	}

	private void sendToFriend() {
		// TODO Auto-generated method stub

	}

	public void setMenuItemEnable(boolean status) {
		menu.getItem(0).setVisible(status);
		menu.getItem(1).setVisible(status);
	}

	private void showMessage(Object msg) {
		Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
	}

	private class ItemClickEventAdapter implements DialogInterface.OnClickListener {

		private Context context;

		ItemClickEventAdapter(Context activity) {
			context = activity;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {

			Intent intent = new Intent(context, AlarmActivity.class);
			String periodType = Alarm.PeriodType.Once.name();
			switch (which) {
			case 0:
				// intent = new Intent(context, AlarmOnceActivity.class);
				periodType = Alarm.PeriodType.Once.name();
				break;
			case 1:
				periodType = Alarm.PeriodType.Daily.name();
				break;
			case 2:
				periodType = Alarm.PeriodType.Weekly.name();
				break;
			case 3:
				periodType = Alarm.PeriodType.Monthly.name();
				break;
			case 4:
				periodType = Alarm.PeriodType.Annual.name();
				break;
			case 5:
				periodType = Alarm.PeriodType.Remote.name();
				break;
			default:
				Toast.makeText(context, "Type not supported for now.", Toast.LENGTH_LONG).show();
				dialog.dismiss();
				return;
			}
			intent.putExtra(Consts.TYPE, periodType);
			startActivity(intent);
			dialog.dismiss();
		}

	}

}
