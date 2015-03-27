package home.tony.reminder;

import home.tony.reminder.common.Consts;
import home.tony.reminder.sms.SMSMessageListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SMSMessageActivity extends Activity {

	private SMSMessageListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String address = getIntent().getStringExtra(Consts.ADDRESS);
		adapter = new SMSMessageListAdapter(getContentResolver(), this, address);
		ListView listView = (ListView) findViewById(R.id.alarmList);
		listView.setOnItemClickListener(adapter);
		listView.setAdapter(adapter);
	}

}
