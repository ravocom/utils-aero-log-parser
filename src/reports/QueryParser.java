package reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class QueryParser {

	private static final String OUTPUT_FILE = "/tmp/live_reports.sql";

	public static void main(String[] args) {
		new QueryParser().parse("/home/rimaz/oracle_utils/reporting/11MAY2019_10/reporting.log");
	}

	public void parse(String fileName) {

		BufferedReader br = null;
		FileReader fr = null;
		int exceptionCount = 0;
		int count = 0;
		long startTime = new Date().getTime();
		StringBuilder sqlCollector = new StringBuilder();

		try {

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = null;

			try {

				while ((line = br.readLine()) != null) {
					if (isValidSQL(line)) {
						String timeStamp = line.split(",")[0];
						System.out.println("SQL_ID=" + ++count + ",TIMESTAMP=" + timeStamp);
						String sql = extractSql(line);
						sqlCollector.append(sql);
						sqlCollector.append("\n");

						System.out.println(sql);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				++exceptionCount;
			}

			writeToFile(sqlCollector);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Time taken to process =" + (new Date().getTime() - startTime) / 1000 + " seconds");
		System.out.println("Successfully parsed SQL count=" + count);
		System.out.println("Failed to parse SQL count =" + exceptionCount);
		System.out.println("*****************************************");

	}

	private String extractSql(String rawSql) {
		String plainSql = null;
		String array[] = rawSql.split("SQL is \\[");
		plainSql = array[1];
		plainSql = plainSql.replaceAll("\\]", "");
		return plainSql;
	}

	private boolean isValidSQL(String line) {
		boolean valid = false;
		if (line.contains("[ReportsJdbcDAOTemplate] SQL is [")) {
			valid = true;
		}
		return valid;
	}

	private static void writeToFile(StringBuilder sb) throws IOException {
		FileWriter writer = new FileWriter(OUTPUT_FILE, true);
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(sb.toString());
		bw.close();
		writer.close();

	}

}
