package home.tony.reminder;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.alarm.AlarmManagerExtension;
import home.tony.reminder.alarm.AlarmUtil;
import home.tony.reminder.common.Consts;
import home.tony.reminder.player.AudioPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

public class AlarmPopupActivity extends Activity {

	private Alarm alarm;
	private AlarmManagerExtension alarmManagerExtension;
	private AudioPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_alarm_popup);
		alarm = Alarm.decode(getIntent().getStringExtra(Consts.REQUEST));

		initUI();
		alarmManagerExtension = new AlarmManagerExtension(this);

		// player = new AudioPlayer(this);
		// if (Settings.isPlaySound()) {
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // player.play(1);
		// }
	}

	private void initUI() {
		TextView edit_title = (TextView) findViewById(R.id.edit_title);
		edit_title.setText(alarm.getTitle());
		TextView edit_detail = (TextView) findViewById(R.id.edit_detail);
		edit_detail.setText(AlarmUtil.getDetail(this, alarm));
		TextView edit_time = (TextView) findViewById(R.id.edit_time);
		long extectedTime = getIntent().getLongExtra(Consts.EXPECTED_ALARM_TIME, 0);
		edit_time.setText(DateFormat.format(Consts.DATE_TIME,extectedTime));
	}

	public void stopAlarm(View view) {
		alarmManagerExtension.cancelAlarm(alarm);
		//player.stop();
		finish();
	}

	public void remindAgain(View view) {
		//player.stop();
		finish();
	}

}
