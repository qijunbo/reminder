package home.tony.reminder.sms;

import home.tony.reminder.R;
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
import android.widget.TextView;

public class SMSMessageListAdapter extends BaseAdapter implements OnItemClickListener {

	private SMSDao dao;
	private List<SMSItem> data;
	private Activity context;

	public SMSMessageListAdapter(ContentResolver resolver, Activity activity, String number) {
		dao = new SMSDao(resolver);
		data = dao.getByAddress(number);
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
			convertView = context.getLayoutInflater().inflate(R.layout.message_item, parent, false);
		}
		drawItem(position, convertView, parent);

		return convertView;
	}

	private void drawItem(int position, View itemView, ViewGroup container) {
	
		try {
			
			// SMSItem item = data.get(position);
			// TextView title = (TextView)
			// itemView.findViewById(R.id.alarm_item_title);
			// title.setText(DateFormat.format(Consts.DATE_TIME, new
			// Date(item.date)));

			TextView detail = (TextView) itemView.findViewById(R.id.alarm_item_detail);
			String content = getContent(position);
			detail.setText(content);

		} catch (RuntimeException e) {

			e.printStackTrace();
		}
	}

	private String getContent(int position) {
		SMSItem item = data.get(position);
		String content = item.address + " " + item.person + "\n" + item.body;
		return content;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra(Consts.CONTENT, getContent(position));
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();

	}

}
