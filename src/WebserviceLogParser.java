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
	// private static final String FILENAME =
	// "/home/rimaz/oracle/ws-analytics.log.2019-04-08-08-10.8.18.18.log";
	private static final String FILENAME = "/home/rimaz/oracle_utils/filtered/raw_one_min_getAvailability_09ARP2019_0826.log";
	private static final String OPERATION = "getAvailability";

	private static final String KEY_JOURNEY = "JOURNEY_INFO=<";
	private static final String KEY_QUANTITY = "QUANTITY=<";
	private static final String RESPONSE_TIME = "RESPONSE_TIME=<";

	private static final String COMMA = ",";
	private static final String DUMMY_RETURN = ",,,";
	private static final int MAX_JOURENY_COUNT = 2;

	private static final int RESPONSE_TIME_LOWER_THRESHOLD = 600;
	private static boolean FILETER_ONLY_RETURN = true;

	private static final String CARRIER_HUB = "SHJ";

	private static final String OUTPUT_FILE = "/tmp/live_webservice_search.csv";

	public static void main(String[] args) {
		new WebserviceLogParser().parse(FILENAME);
	}

	public void parse(String fileName) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			int jourenyCount = 0;

			long startTime = new Date().getTime();

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String line = null;
			int recordCount = 0;
			int errorCount = 0;
			int exceededJourenyCount = 0;
			int noAdults = 0;
			int lowerResponse = 0;
			int nonReturnCount = 0;

			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				if (line.indexOf(OPERATION) > 1) {

					try {
						String jsonQuantity = extractValue(line, KEY_QUANTITY);
						String adultQuantity = extractPlainValueFromJson(jsonQuantity, "ADT");

						String strResponseTime = extractValue(line, RESPONSE_TIME);
						int responseTime = Integer.parseInt(strResponseTime);

						String jsonJoureny = extractValue(line, KEY_JOURNEY);
						List<JourenyInfo> journeyList = convertToJourney(jsonJoureny);
						String journeyType = deriveJourenyType(journeyList);

						if (responseTime > RESPONSE_TIME_LOWER_THRESHOLD) {
							System.out.println("Response Time=" + responseTime);

							if (adultQuantity != null) {
								if (journeyList.size() <= MAX_JOURENY_COUNT) {

									if (isIncludeNonReturn(journeyType)) {

										jourenyCount++;

										sb.append(journeyType);
										sb.append(COMMA);
										sb.append(adultQuantity);
										sb.append(COMMA);
										journeyList.forEach(x -> sb.append(x.toString()));

										if (journeyList.size() == 1) {
											sb.append(DUMMY_RETURN);
										}

										sb.append("\n");

										if (jourenyCount > 301) {
											break;
										}

									} else {
										++nonReturnCount;
									}

								} else {
									++exceededJourenyCount;
								}
							} else {
								++noAdults;
							}
						} else {
							++lowerResponse;
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
			System.out.println("Exception count (ignored)= " + errorCount);
			System.out.println("ExceededJoureny count( ignored)= " + exceededJourenyCount);
			System.out.println("noAdults count( ignored)= " + noAdults);
			System.out.println("lowerResponse count( ignored)= " + lowerResponse);
			System.out.println("nonReturnCount count( ignored)= " + nonReturnCount);
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

	private static boolean isIncludeNonReturn(String journeyType) {
		boolean include = true;
		if (FILETER_ONLY_RETURN == true) {
			if (journeyType.indexOf("RETURN") > 0) {
				include = true;
			} else {
				include = false;
			}
		}

		return include;
	}

	private static String deriveJourenyType(List<JourenyInfo> journeyList) {
		String jourenyType = "UNKNOWN";

		if (journeyList.size() == 1) {
			jourenyType = "ONEWAY";
			JourenyInfo single = journeyList.get(0);
			if (!isHubRelated(single)) {
				jourenyType = "CONNECTION";
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
