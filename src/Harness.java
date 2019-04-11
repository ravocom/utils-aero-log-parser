import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Harness {

	public static void main(String[] args) {
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
