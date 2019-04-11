import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Harness {

	public static void main(String[] args) {
		String dateStr = "Apr 23, 2019 05:40:00 PM";
	//	String dateStr = "Apr 23, 2019 13:00:00";

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

}
