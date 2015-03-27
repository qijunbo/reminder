package home.tony.reminder.sms;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class SMSDao {

	private final String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
	private ContentResolver resolver;
	final String SMS_URI_ALL = "content://sms/";
	final String SMS_URI_DRAFT = "content://sms/draft";

	final String SMS_URI_INBOX = "content://sms/inbox";
	final String SMS_URI_SEND = "content://sms/sent";

	public SMSDao(ContentResolver resolver) {

		this.resolver = resolver;
	}

	List<String> getAddress() {
		List<String> msg = new ArrayList<String>();
		Uri uri = Uri.parse(SMS_URI_ALL);
		Cursor cursor = resolver.query(uri, projection, null, null, "date desc");
		int phoneNumberColumn = cursor.getColumnIndex("address");
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String address = cursor.getString(phoneNumberColumn);
				// if (address.startsWith("+")) {
				// address = address.substring(3);
				// }
				if (msg.indexOf(address) < 0) {
					msg.add(address);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return msg;
	}

	List<SMSItem> getAllInbox() {
		Uri uri = Uri.parse(SMS_URI_INBOX);
		Cursor cursor = resolver.query(uri, projection, null, null, "date desc");
		List<SMSItem> msg = wrapUp(cursor);
		cursor.close();
		return msg;
	}

	List<SMSItem> getByAddress(String address) {

		Uri uri = Uri.parse(SMS_URI_ALL);
		String where = " address = ? ";
		String[] parms = new String[] { address };
		Cursor cursor = resolver.query(uri, projection, where, parms, "date desc");

		List<SMSItem> msg = wrapUp(cursor);

		cursor.close();
		return msg;
	}

	private List<SMSItem> wrapUp(Cursor cursor) {
		List<SMSItem> msg = new ArrayList<SMSItem>();
		int idColumn = cursor.getColumnIndex("_id");
		int nameColumn = cursor.getColumnIndex("person");
		int phoneNumberColumn = cursor.getColumnIndex("address");
		int smsbodyColumn = cursor.getColumnIndex("body");
		int dateColumn = cursor.getColumnIndex("date");
		int typeColumn = cursor.getColumnIndex("type");

		if (cursor != null && cursor.moveToFirst()) {
			do {
				SMSItem item = new SMSItem();
				item.id = cursor.getString(idColumn);
				item.person = cursor.getString(nameColumn);
				item.address = cursor.getString(phoneNumberColumn);
				if (item.address.startsWith("+")) {
					item.address = item.address.substring(3);
				}
				item.body = cursor.getString(smsbodyColumn);
				item.date = cursor.getLong(dateColumn);
				item.type = cursor.getInt(typeColumn);
				msg.add(item);
			} while (cursor.moveToNext());
		}
		return msg;
	}

}
