package home.tony.reminder.sms;

import home.tony.reminder.R;
import home.tony.reminder.SMSMessageActivity;
import home.tony.reminder.common.Consts;

import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSAddressListAdapter extends BaseAdapter implements OnItemClickListener {

	private SMSDao dao;
	private List<String> data;
	private Activity context;

	public SMSAddressListAdapter(ContentResolver resolver, Activity activity) {
		dao = new SMSDao(resolver);
		data = dao.getAddress();
		this.context = activity;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(R.layout.alarm_item, parent, false);
		}
		drawItem(position, convertView, parent);

		return convertView;
	}

	private void drawItem(int position, View itemView, ViewGroup container) {
		try {
			String phoneNumber = data.get(position);
			List<SMSItem> msg = dao.getByAddress(phoneNumber);

			ImageView imageView = (ImageView) itemView.findViewById(R.id.alarm_item_logo);
			imageView.setImageResource(R.drawable.img_message);

			TextView nextalarm = (TextView) itemView.findViewById(R.id.alarm_item_next);
			nextalarm.setText(String.valueOf(msg.size()));
			 
			TextView title = (TextView) itemView.findViewById(R.id.alarm_item_title);
			title.setText(phoneNumber);

			TextView detail = (TextView) itemView.findViewById(R.id.alarm_item_detail);
			detail.setText(msg.get(0).body);

		} catch (RuntimeException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(context, SMSMessageActivity.class);
		intent.putExtra(Consts.ADDRESS, data.get(position));
		context.startActivityForResult(intent, Consts.RESULT_LOAD_SMS_MESSAGE);
	}

}
