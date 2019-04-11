import java.io.File;

public class DirectoryReader {

	private static final String FEED = "/home/rimaz/oracle_utils/08hour/";

	public static void main(String[] args) {
		//collectServiceAppData("/home/rimaz/oracle_utils/service-app/filtered_1hour_10ARP2019_10/");
		collectWebserviceData("/home/rimaz/oracle_utils/08hour/");
	}

	public static void collectWebserviceData(String feed) {

		final File folder = new File(feed);
		String[] fileNames = folder.list();

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new WebserviceLogParser().parse(feed + fileName);
		}

		System.out.println("Completed");

	}

	public static void collectServiceAppData(String feed) {

		final File folder = new File(feed);
		String[] fileNames = folder.list();

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new ServiceAppLogParser().parse(feed + fileName);
		}

		System.out.println("Completed");

	}

}