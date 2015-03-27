package home.tony.reminder;

import home.tony.reminder.common.Consts;
import home.tony.reminder.sms.SMSAddressListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class SMSActivity extends Activity {

	private SMSAddressListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new SMSAddressListAdapter(getContentResolver(), this);
		ListView listView = (ListView) findViewById(R.id.alarmList);
		listView.setOnItemClickListener(adapter);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Consts.RESULT_LOAD_SMS_MESSAGE) {
			if (data != null) {
				Intent intent = new Intent();
				intent.putExtra(Consts.RESPONSE,  data.getStringExtra(Consts.CONTENT));
				this.setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}

	}

}
