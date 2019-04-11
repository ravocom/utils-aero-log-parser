import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

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
