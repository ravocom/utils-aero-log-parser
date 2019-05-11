import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Harness {
	
	
	public static void main(String[] args) {
		
	}
	

	public static void main5(String[] args) {
		System.out.println(addDateVarience(new Date(), 7));
	}

	public static Date addDateVarience(Date target, int varience) {
		GregorianCalendar gcDate = new GregorianCalendar();
		gcDate.clear();
		gcDate.setTime(target);

		int year = gcDate.get(Calendar.YEAR);
		int month = gcDate.get(Calendar.MONTH);
		int day = gcDate.get(Calendar.DAY_OF_MONTH) + varience;
		int hours = gcDate.get(Calendar.HOUR_OF_DAY);
		int mins = gcDate.get(Calendar.MINUTE);
		int secs = gcDate.get(Calendar.SECOND);

		GregorianCalendar gc2 = new GregorianCalendar(year, month, day, hours, mins, secs);
		return gc2.getTime();
	}

	public static void main4(String[] args) {
		String searchKey = "CAI_GOI_0_09/05/2019_GOI_CAI_0_11/05/2019_1_0_0_Y_Y";
		String[] array = searchKey.split("_");
		System.out.println(array.length);
		int keySize = array.length - 5;
		System.out.println(array[keySize]);
		System.out.println(searchKey);
	}

	public static void main1(String[] args) {
		String strDate = "2019-04-10 12:08:22";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(strDate);

			Date after = addMinutes(date, 5);
			System.out.println(after);

			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main2(String[] args) {
		String dateStr = "Apr 23, 2019 05:40:00 PM";
		// String dateStr = "Apr 23, 2019 13:00:00";

		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
		Date date;
		try {
			date = format.parse(dateStr);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Date addMinutes(Date zuluDate, int minutes) {
		GregorianCalendar calander = new GregorianCalendar();
		calander.setTime(zuluDate);
		calander.add(GregorianCalendar.MINUTE, minutes);
		return calander.getTime();
	}

	public static GregorianCalendar getCalendar(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public static boolean isBetween(Date parseDate, Date fromDate, Date toDate) {

		boolean status = false;
		// GregorianCalendar(int year, int month, int date, int hour, int
		// minute, int second)

		GregorianCalendar compareDate = getCalendar(parseDate);
		GregorianCalendar dateFrom = getCalendar(fromDate);
		GregorianCalendar dateTo = getCalendar(toDate);

		if (compareDate.after(dateFrom) && compareDate.before(dateTo)) {
			status = true;
		}
		return status;
	}

}
