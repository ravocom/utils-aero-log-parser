import java.io.File;
import java.util.Arrays;

public class DirectoryReader {

	private static final String FEED = "/home/rimaz/oracle_utils/08hour/";

	public static void main(String[] args) {
		//collectServiceAppData("/home/rimaz/oracle_utils/service-app/logs_serviceapp_10MAY2019_08/");

		collectWebserviceData("/home/rimaz/oracle_utils/ws/logs_webservice_10MAY2019_08/");
	}

	public static void collectWebserviceData(String feed) {

		final File folder = new File(feed);
		String[] fileNames = folder.list();
		Arrays.sort(fileNames);

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new WebserviceLogParser().parse(feed + fileName);
		}

		System.out.println("Completed");

	}

	public static void collectServiceAppData(String feed) {

		final File folder = new File(feed);
		String[] fileNames = folder.list();
		Arrays.sort(fileNames);

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new ServiceAppLogParser().parse(feed + fileName);
		}

		System.out.println("Completed");

	}

}