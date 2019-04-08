import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.JourenyInfo;

public class WebserviceLogParser {

	// private static final String FILENAME = "/tmp/test.log";
	private static final String FILENAME = "/home/rimaz/oracle/ws-analytics.log.2019-04-08-08-10.8.18.18.log";
	private static final String OPERATION = "getAvailability";

	private static final String KEY_JOURNEY = "JOURNEY_INFO=<";
	private static final String KEY_QUANTITY = "QUANTITY=<";

	private static final String COMMA = ",";
	private static final int MAX_JOURENY_COUNT = 2;

	private static final String CARRIER_HUB = "SHJ";

	private static final String OUTPUT_FILE = "/tmp/webservice_search.csv";

	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			long startTime = new Date().getTime();

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String line = null;
			int recordCount = 0;
			int errorCount = 0;
			int exceededJourenyCount = 0;

			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				if (line.indexOf(OPERATION) > 1) {

					try {
						String jsonQuantity = extractValue(line, KEY_QUANTITY);
						String adultQuantity = extractPlainValueFromJson(jsonQuantity, "ADT");

						String jsonJoureny = extractValue(line, KEY_JOURNEY);
						List<JourenyInfo> journeyList = convertToJourney(jsonJoureny);
						String journeyType = deriveJourenyType(journeyList);

						if (journeyList.size() <= MAX_JOURENY_COUNT) {
							sb.append(journeyType);
							sb.append(COMMA);
							sb.append(adultQuantity);
							sb.append(COMMA);
							journeyList.forEach(x -> sb.append(x.toString()));
							sb.append("\n");
						} else {
							++exceededJourenyCount;
						}

					} catch (Exception e) {
						++errorCount;
						System.out.println("Error processing line=" + line);
						e.printStackTrace();
					}

					++recordCount;
				}
			}

			writeToFile(sb);

			System.out.println("************************************");
			System.out.println("Record count= " + recordCount);
			System.out.println("Error count (ignored)= " + errorCount);
			System.out.println("ExceededJoureny count( ignored)= " + exceededJourenyCount);
			System.out.println("Time taken to process (seconds) = " + (new Date().getTime() - startTime) / 1000);
			System.out.println("\n File is generated at " + OUTPUT_FILE);
			System.out.println("************************************");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	private static String deriveJourenyType(List<JourenyInfo> journeyList) {
		String jourenyType = "UNKNOWN";

		if (journeyList.size() == 1) {
			jourenyType = "CONNECTION-OR-INVALID";
			JourenyInfo single = journeyList.get(0);
			if (isHubRelated(single)) {
				jourenyType = "ONEWAY";
			}

		} else if (journeyList.size() == 2) {

			JourenyInfo outbound = journeyList.get(0);
			JourenyInfo inbound = journeyList.get(1);

			if (outbound.getDetination().equals(inbound.getOrigin())) {
				if (inbound.getDetination().equals(outbound.getOrigin())) {
					jourenyType = "RETURN";
					if (!isHubRelated(outbound)) {
						jourenyType = "CONNECTION_RETURN";
					}

				} else {
					jourenyType = "CONNECTION";
				}

			}

		}

		return jourenyType;
	}

	private static boolean isHubRelated(JourenyInfo joureney) {
		if (joureney.getOrigin().equals(CARRIER_HUB)) {
			return true;
		}
		if (joureney.getDetination().equals(CARRIER_HUB)) {
			return true;
		}
		return false;
	}

	private static void writeToFile(StringBuilder sb) throws IOException {
		FileWriter writer = new FileWriter(OUTPUT_FILE);
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(sb.toString());
		bw.close();
		writer.close();

	}

	private static String extractPlainValueFromJson(String json, String fieldName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String fieldValue = jsonNode.get(fieldName).asText();
		return fieldValue;
	}

	private static List<JourenyInfo> convertToJourney(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Map<String, JourenyInfo> map = mapper.readValue(jsonString, new TypeReference<Map<String, JourenyInfo>>() {
		});

		List<JourenyInfo> journeyList = new ArrayList<>(map.values());
		Collections.sort(journeyList);
		return journeyList;
	}

	private static String extractValue(String line, String key) {
		String[] arrary = line.split(key);
		String value = arrary[1].split(">")[0];
		return value;
	}

}
