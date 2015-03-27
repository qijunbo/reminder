package home.tony.reminder;

import home.tony.reminder.alarm.Alarm;
import home.tony.reminder.bitmap.ImageLoder;
import home.tony.reminder.common.Consts;
import home.tony.reminder.common.FileHelper;
import home.tony.reminder.event.AppContext;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity {

	public static final String EDIT_MODEL = "EDIT";
	public static final int HOME = 0x0102002c;

	private Alarm request;
	private Alarm.PeriodType type;
	private WeekDayAdapter weekDayAdapter = new WeekDayAdapter();

	public void actionCaptureImage(View view) {

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// set the image file name
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
		// start the image capture Intent
		startActivityForResult(intent, Consts.CAPTURE_IMG_FROM_CAREMA);
	}

	public void actionLoadImage(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, Consts.RESULT_LOAD_IMAGE);
	}

	public void actionLoadMessage(View view) {
		Intent intent = new Intent(this, SMSActivity.class);
		startActivityForResult(intent, Consts.RESULT_LOAD_SMS_ADDRESS);
	}

	public void actionSaveAlarm(View view) {

		saveOrUpdateAlarm();
		this.request = null;
	}

	public void actionSendAlarm(View view) {
		showMessage("Will send the message to your friend!");
		this.request = null;
	}

	public void actionViewImage(View view) {
		Intent intent = new Intent(this, ViewImageActivity.class);
		intent.putExtra(Consts.REQUEST, request.getCapture());
		startActivity(intent);
	}

	private String getFieldDetail() {
		EditText detail = (EditText) findViewById(R.id.edit_detail);
		String text = detail.getText().toString();
		return text;
	}

	private String getFieldTitle() {
		EditText title = (EditText) findViewById(R.id.edit_title);
		String text = title.getText().toString();

		if ("".endsWith(text)) {
			text = type.name() + "Event";
		}

		return text;
	}

	private Uri getOutputMediaFileUri() {
		File file = new File(FileHelper.getConfigPath() + File.separator
				+ DateFormat.format(Consts.PIC_NAME, new Date()));
		if (FileHelper.isExternalStorageWritable()) {
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			request.setCapture(file.getAbsolutePath());
			return Uri.fromFile(file);
		}
		return null;
	}

	public Alarm getRequest() {
		return request;
	}

	private void initUI() {
		if (EDIT_MODEL.equals(this.getIntent().getStringExtra(EDIT_MODEL))) {
			this.request = Alarm.decode(this.getIntent().getStringExtra(Consts.REQUEST));
		} else {
			this.request = Alarm.createDefault(type);
		}
		final Calendar cal = Calendar.getInstance(Locale.CHINA);
		setFieldTitle(request.getTitle());

		cal.set(Calendar.MONTH, request.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, request.getDayOfMonth());
		cal.set(Calendar.HOUR_OF_DAY, request.getHourOfDay());
		cal.set(Calendar.MINUTE, request.getMinutes());
		setFieldDate(cal);
		setFieldTime(cal);
		if (!Consts.isEmpty(request.getDetail())) {
			setFieldDetail(request.getDetail());
		}
		if (!Alarm.NEVER.equals(request.getExpireTime())) {
			cal.setTime(request.getExpireTime());
			setFieldExpireDate(cal);
		}
		setFieldDaysOfWeek();
		setFieldCapture(request.getCapture());
		if (!Consts.isEmpty(request.getFrom())) {
			String from = request.getFrom() + getString(R.string.intro_message_from);
			setFieldFrom(from);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Consts.CAPTURE_IMG_FROM_CAREMA) {
			if (resultCode == RESULT_OK) {
				setFieldCapture(request.getCapture());
				// Toast.makeText(this, request.getCapture(),
				// Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				request.setCapture(null);
			} else {
				request.setCapture(null);
			}
		} else if (requestCode == Consts.RESULT_LOAD_IMAGE) {
			if (null != data && resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				request.setCapture(picturePath);
				setFieldCapture(picturePath);
			}
		} else if (requestCode == Consts.RESULT_LOAD_SMS_ADDRESS) {
			if (null != data && resultCode == RESULT_OK) {
				String content = data.getStringExtra(Consts.RESPONSE);
				setFieldDetail(content);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String periodType = intent.getStringExtra(Consts.TYPE);
		type = Alarm.PeriodType.valueOf(periodType);
		setContentView();
		// setHeader(periodType);
		if (Alarm.PeriodType.Remote.equals(type)) {
			type = Alarm.PeriodType.Once;
		}
		initUI();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		EditText edit_title = (EditText) findViewById(R.id.edit_title);
		imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);

	}

	private void saveOrUpdateAlarm() {
		Alarm alarm = request;

		alarm.setTitle(getFieldTitle());
		alarm.setDetail(getFieldDetail());
		alarm.setDaysOfWeek(weekDayAdapter.getSelectedDays());

		if (type.equals(Alarm.PeriodType.Once)) {
			final Calendar cal = Calendar.getInstance(Locale.CHINA);
			cal.setTime(alarm.getExpireTime());
			cal.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
			cal.set(Calendar.MINUTE, alarm.getMinutes());
			cal.set(Calendar.SECOND, 0);
			alarm.setExpireTime(cal.getTime());
		}

		if (EDIT_MODEL.equals(this.getIntent().getStringExtra(EDIT_MODEL))) {
			AppContext.getInstance().remove(String.valueOf(alarm.getId()));
		}
		AppContext.getInstance().put(alarm);
		super.onBackPressed();
	}

	private void setContentView() {
		switch (type) {
		case Once:
			setContentView(R.layout.edit_once);
			break;
		case Daily:
			setContentView(R.layout.edit_daily);
			break;
		case Weekly:
			setContentView(R.layout.edit_weekly);
			break;
		case Monthly:
			setContentView(R.layout.edit_monthly);
			break;
		case Annual:
			setContentView(R.layout.edit_annual);
			break;
		case Remote:
			setContentView(R.layout.edit_remote);
			break;
		}
	}

	private void setFieldCapture(String capture) {
		ImageView picture = (ImageView) findViewById(R.id.picture);
		if (picture == null) {
			return;
		}

		if (Consts.isEmpty(capture)) {
			// picture.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			return;
		}
		// picture.setLayoutParams(new LayoutParams(100, 200));
		File file = new File(capture);
		if (file.exists()) {
			// new ImageLoder(picture).load(capture);
			picture.setImageBitmap(ImageLoder.decodeSampledBitmapFromResource(capture, 100, 100));

		}
	}

	private void setFieldDate(final Calendar cal) {
		EditText edit_date = (EditText) findViewById(R.id.edit_date);
		if (edit_date != null) {
			edit_date.setText(DateFormat.format(Consts.DATE, cal.getTime()));
		}
	}

	private void setFieldDaysOfWeek() {
		weekDayAdapter.setSelectedDays(request.getDaysOfWeek(), this);
	}

	private void setFieldDetail(String detail) {
		EditText edit_detail = (EditText) findViewById(R.id.edit_detail);
		edit_detail.setText(detail);

	}

	private void setFieldExpireDate(final Calendar cal) {

		EditText edit_expire_datetime = (EditText) findViewById(R.id.edit_expire_datetime);
		if (cal.getTimeInMillis() <= Alarm.LONG_NEVER) {
			edit_expire_datetime.setText(DateFormat.format(Consts.DATE, cal.getTime()));
		} else {
			edit_expire_datetime.setText("");
		}
	}

	private void setFieldTime(final Calendar cal) {
		EditText edit_time = (EditText) findViewById(R.id.edit_time);
		edit_time.setText(DateFormat.format(Consts.TIME, cal.getTime()));
	}

	private void setFieldTitle(String title) {
		EditText edit_title = (EditText) findViewById(R.id.edit_title);
		edit_title.setText(title);
	}

	private void setFieldFrom(String from) {
		TextView text_from = (TextView) findViewById(R.id.from);
		text_from.setText(from);
	}

	private void setHeader(String periodType) {
		// LinearLayout layout = (LinearLayout) findViewById(R.id.header);
		//
		// ImageView logo = (ImageView) layout.findViewById(R.id.logo);
		// TextView description = (TextView)
		// layout.findViewById(R.id.description);
		// switch (Alarm.PeriodType.valueOf(periodType)) {
		// case Once:
		// logo.setImageResource(R.drawable.img_default);
		// description.setText(R.string.intro_message_once);
		// break;
		// case Daily:
		// logo.setImageResource(R.drawable.img_clock);
		// description.setText(R.string.intro_message_daily);
		// break;
		// case Weekly:
		// logo.setImageResource(R.drawable.img_weekly);
		// description.setText(R.string.intro_message_weekly);
		// break;
		// case Monthly:
		// logo.setImageResource(R.drawable.img_monthly);
		// description.setText(R.string.intro_message_monthly);
		// break;
		// case Annual:
		// logo.setImageResource(R.drawable.img_annual);
		// description.setText(R.string.intro_message_annual);
		// break;
		//
		// }

	}

	public void setRequest(Alarm request) {
		this.request = request;
	}

	public void setSelectedDay(View view) {

		CheckBox checkbox = (CheckBox) view;
		if (checkbox.isChecked()) {
			weekDayAdapter.add(view.getId());
		} else {
			weekDayAdapter.remove(view.getId());
		}
	}

	public void showDatePickerDialog(View view) {
		final Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.set(Calendar.MONTH, request.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, request.getDayOfMonth());
		OnDateSetListener listener = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				request.setMonth(monthOfYear);
				request.setDayOfMonth(dayOfMonth);
				cal.set(year, monthOfYear, dayOfMonth);
				setFieldDate(cal);

			}

		};

		DatePickerDialog dialog = new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));

		dialog.show();

	}

	public void showExpireDatePickerDialog(View view) {
		final Calendar cal = Calendar.getInstance(Locale.CHINA);
		if (request.getExpireTime().getTime() <= Alarm.LONG_NEVER) {
			cal.setTime(request.getExpireTime());
		}
		OnDateSetListener listener = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				cal.set(year, monthOfYear, dayOfMonth);
				request.setExpireTime(cal.getTime());
				setFieldExpireDate(cal);
			}

		};

		DatePickerDialog dialog = new DatePickerDialog(this, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));

		dialog.show();

	}

	private void showMessage(Object msg) {
		Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
	}

	public void showTimePickerDialog(View view) {

		final Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.set(Calendar.HOUR_OF_DAY, request.getHourOfDay());
		cal.set(Calendar.MINUTE, request.getMinutes());
		OnTimeSetListener listener = new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				request.setHourOfDay(hourOfDay);
				request.setMinutes(minute);
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				setFieldTime(cal);
			}
		};

		TimePickerDialog dialog = new TimePickerDialog(this, listener, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), true);
		dialog.show();
	}
}
