import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import dto.JourenyInfo;
import dto.OriginDestinationInfo;

public class ServiceAppLogParser {

	// private static final String FILENAME = "/tmp/test.log";
	// private static final String FILENAME =
	// "/home/rimaz/oracle/ws-analytics.log.2019-04-08-08-10.8.18.18.log";
//	private static final String FILENAME = "/home/rimaz/oracle_utils/service-app/sample.log";
	private static final String FILENAME = "/home/rimaz/oracle_utils/service-app/analytics_ibe_search.log.2019-04-10-12";

	private static final String CARRIER_HUB = "SHJ";
	private static final String COMMA = ",";

	private static final String OUTPUT_FILE = "/tmp/live_service_app_search.csv";
	private static final String KEY_OND_EXPANDED = "ond_expanded=<";
	private static final String KEY_SEARCH_SYSTEM = "search_system=<";
	private static final String KEY_SEARCH_KEY = "search_key=<";

	private static final String PARSE_DATE_FORMAT = "MMM dd, yyyy hh:mm:ss a";
	private static final String TIME_CONSTANT = "T00:00:00";

	private static final String JSON_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";

	private static final String RANGE_STR_START_DATE = "2019-04-10 10:37:23";
	private static final int RANGE_MINUUES = 4;
	
	private boolean mask =true;

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

			int exceptionCount = 0;
			int successCount = 0;

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = format.parse(RANGE_STR_START_DATE);
			Date endDate = DateUtil.addMinutes(startDate, RANGE_MINUUES);

			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				try {

					String jsonSearchSystem = extractValue(line, KEY_SEARCH_SYSTEM);
					String strTimestamp = line.split(",")[0];

					SimpleDateFormat jsonFormat = new SimpleDateFormat(JSON_TIMESTAMP);
					Date timestamp = jsonFormat.parse(strTimestamp);

					if (filterByRange(timestamp, startDate, endDate) && "AA".equals(jsonSearchSystem)) {

						String ondjson = extractValue(line, KEY_OND_EXPANDED);
						ObjectMapper mapper = new ObjectMapper();

						String searchKey = extractValue(line, KEY_SEARCH_KEY);
						
						
						String [] array = searchKey.split("_");
						
						int keySize = array.length -5;
						String adultCount  = array[keySize];
						int aa= Integer.parseInt(adultCount);
						
						if(aa >1) {
							System.out.println(searchKey);
							System.out.println(adultCount);
						}
						//System.out.println(aa);
						
						//System.out.println(adultCount);

						DateFormat dateFormat = new SimpleDateFormat(PARSE_DATE_FORMAT);
						mapper.setDateFormat(dateFormat);

						Map<String, OriginDestinationInfo> map = new HashMap<String, OriginDestinationInfo>();

						// convert JSON string to Map
						map = mapper.readValue(ondjson, new TypeReference<Map<String, OriginDestinationInfo>>() {
						});

						List<OriginDestinationInfo> travelList = new ArrayList<>(map.values());
						Collections.sort(travelList);

						String jourenyType = deriveJourenyType(travelList);

						if (isAllowJoureny(jourenyType)) {

							if(!mask) {
								sb.append(jourenyType);
								sb.append(COMMA);
								sb.append(jsonSearchSystem);
								sb.append(COMMA);
							}
							
							sb.append(adultCount);
							sb.append(COMMA);

							for (OriginDestinationInfo travelInfo : travelList) {
								String origin = travelInfo.getOrigin();
								String destination = travelInfo.getDestination();
								Date departureDate = travelInfo.getDepartureDateTime();

								sb.append(origin);
								sb.append(COMMA);
								sb.append(destination);
								sb.append(COMMA);
								sb.append(departureDate);
								sb.append(TIME_CONSTANT);
								sb.append(COMMA);
							}
							sb.append("\n");
							successCount++;
						}

					}

				} catch (Exception e) {
					++exceptionCount;
					e.printStackTrace();
				}

			}

			// System.out.println(sb.toString());

			writeToFile(sb);

			System.out.println("************************************");
			System.out.println("Time taken to process (seconds) = " + (new Date().getTime() - startTime) / 1000);
			System.out.println("Success count =" + successCount);
			System.out.println("Exception count =" + exceptionCount);
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

	private boolean isAllowJoureny(String jourenyType) {
		boolean allow = true;
		allow = jourenyType.indexOf("RETURN") > -1;
		return allow;
	}

	private boolean filterByRange(Date timestamp, Date startDate, Date endDate) {
		boolean allow = true;
		allow = DateUtil.isBetween(timestamp, startDate, endDate);
		return allow;
	}

	private static String deriveJourenyType(List<OriginDestinationInfo> travelList) {
		String jourenyType = "UNKNOWN";

		if (travelList.size() == 1) {
			jourenyType = "ONEWAY";
			OriginDestinationInfo single = travelList.get(0);
			if (!isHubRelated(single)) {
				jourenyType = "CONNECTION";
			}

		} else if (travelList.size() == 2) {

			OriginDestinationInfo outbound = travelList.get(0);
			OriginDestinationInfo inbound = travelList.get(1);

			if (outbound.getDestination().equals(inbound.getOrigin())) {
				if (inbound.getDestination().equals(outbound.getOrigin())) {
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

	private static boolean isHubRelated(OriginDestinationInfo single) {
		if (single.getOrigin().equals(CARRIER_HUB)) {
			return true;
		}
		if (single.getDestination().equals(CARRIER_HUB)) {
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
