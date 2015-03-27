package home.tony.reminder.common;


public class Consts {
	
	public static final String DATE_TIME = "yyyy-MM-dd kk:mm";
	public static final String PIC_NAME = "yyyyMMddkkmmss.jpg";
	public static final String TIME = "kk:mm";
	public static final String DATE = "yyyy-MM-dd";
	public static final String REQUEST = "request_";
	public static final String EXPECTED_ALARM_TIME = "expected_alarm_time";
	public static final String ADDRESS = "address";
	public static final String  CONTENT = "CONTENT";
	public static final String RESPONSE = "response_";
	public static final String TYPE = "TYPE";

	public static final int CAPTURE_IMG_FROM_CAREMA = 100;
	public static final int RESULT_LOAD_IMAGE = 397;
	public static final int RESULT_LOAD_SMS_ADDRESS = 398;
	public static final int RESULT_LOAD_SMS_MESSAGE = 399;

	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}

		if ("".equals(s.trim())) {
			return true;
		}
		return false;
	}
	

}
