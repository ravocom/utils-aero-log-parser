import java.io.File;

public class DirectoryReader {

	private static final String FEED = "/home/rimaz/oracle_utils/08hour/";

	public static void main(String[] args) {

		final File folder = new File(FEED);
		String[] fileNames = folder.list();

		for (String fileName : fileNames) {
			System.out.println(fileName);
			new WebserviceLogParser().parse(FEED + fileName);
		}
		
		System.out.println("Completed");

	}

}