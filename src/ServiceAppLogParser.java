import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.BasePreferences;
import dto.JourenyInfo;
import dto.OriginDestinationInfo;

public class ServiceAppLogParser {

	// private static final String FILENAME = "/tmp/test.log";
	// private static final String FILENAME =
	// "/home/rimaz/oracle/ws-analytics.log.2019-04-08-08-10.8.18.18.log";
	private static final String FILENAME = "/home/rimaz/oracle_utils/service-app/sample.log";

	private static final String CARRIER_HUB = "SHJ";

	private static final String OUTPUT_FILE = "/tmp/live_service_app_search.csv";
	private static final String KEY_OND_EXPANDED = "ond_expanded=<";

	public static void main(String[] args) {
		new ServiceAppLogParser().parse(FILENAME);
	}

	public void parse(String fileName) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			long startTime = new Date().getTime();

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = null;

			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				String ondjson = extractValue(line, KEY_OND_EXPANDED);
				System.out.println(ondjson);

				ObjectMapper mapper = new ObjectMapper();

				Map<String, OriginDestinationInfo> map = new HashMap<String, OriginDestinationInfo>();

				// convert JSON string to Map
				map = mapper.readValue(ondjson, new TypeReference<Map<String, OriginDestinationInfo>>() {
				});

				List<OriginDestinationInfo> travelList = new ArrayList<>(map.values());

				for (OriginDestinationInfo travelInfo : travelList) {
					String origin = travelInfo.getOrigin();
					String destination = travelInfo.getDestination();
					String departureDate = travelInfo.getDepartureDateTime();
					System.out.println(origin + ":" + destination + ":" + departureDate);
				}

			}

			writeToFile(sb);

			System.out.println("************************************");
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
			jourenyType = "CONNECTION";
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
		FileWriter writer = new FileWriter(OUTPUT_FILE, true);
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(sb.toString());
		bw.close();
		writer.close();

	}

	private static String extractPlainValueFromJson(String json, String fieldName) throws IOException {
		String fieldValue = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);

		fieldValue = jsonNode.get(fieldName) != null ? jsonNode.get(fieldName).asText() : null;
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
