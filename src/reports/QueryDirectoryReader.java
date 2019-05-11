package reports;

import java.io.File;
import java.util.Arrays;

public class QueryDirectoryReader {

	public static void main(String[] args) {
		collectReportingeData("/home/rimaz/oracle_utils/reporting/11MAY2019_10/");
	}

	public static void collectReportingeData(String feed) {

		final File folder = new File(feed);
		String[] fileNames = folder.list();
		Arrays.sort(fileNames);

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new QueryParser().parse(feed + fileName);
		}

		System.out.println("Completed");

	}

}
