package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OriginDestinationInfo extends BasicOndInfo implements Comparable<OriginDestinationInfo> {

	private List<Stopover> stopovers;

	private BasePreferences preferences;

	private List<SpecificFlight> specificFlights;

	public List<Stopover> getStopovers() {
		if (stopovers == null) {
			stopovers = new ArrayList<Stopover>();
		}
		return stopovers;
	}

	public void setStopovers(List<Stopover> stopovers) {
		this.stopovers = stopovers;
	}

	public BasePreferences getPreferences() {
		if (preferences == null) {
			preferences = new BasePreferences();
		}
		return preferences;
	}

	public void setPreferences(BasePreferences preferences) {
		this.preferences = preferences;
	}

	public List<SpecificFlight> getSpecificFlights() {
		return specificFlights;
	}

	public void setSpecificFlights(List<SpecificFlight> specificFlights) {
		this.specificFlights = specificFlights;
	}

	@Override
	public int compareTo(OriginDestinationInfo o) {
		if (o.getDepartureDateTime() != null) {
			if (this.getDepartureDateTime().compareTo(o.getDepartureDateTime()) == 0) {
				if (this.getDestination().equals(o.getOrigin())) {
					return -1;
				} else if (this.getOrigin().equals(o.getDestination())) {
					return 1;
				}
				return 0;
			}
			return this.getDepartureDateTime().compareTo(o.getDepartureDateTime());
		}
		return 0;
	}

	public static boolean isReturnSearch(List<OriginDestinationInfo> ondInfo) {
		return !CollectionUtilsIsEmpty(ondInfo) && ondInfo.size() == OndSequence.RETURN_OND_SIZE
				&& StringUtilsEquals(ondInfo.get(OndSequence.OUT_BOUND).getOrigin(),
						ondInfo.get(OndSequence.IN_BOUND).getDestination())
				&& StringUtilsEquals(ondInfo.get(OndSequence.OUT_BOUND).getDestination(),
						ondInfo.get(OndSequence.IN_BOUND).getOrigin());
	}

	private static boolean CollectionUtilsIsEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	private static boolean StringUtilsEquals(String str1, String str2) {
		boolean equal = false;
		if (str1 != null && str2 != null && str1.equals(str2)) {
			equal = true;
		}
		return equal;
	}

	@Override
	public String toString() {
		return "OriginDestinationInfo [stopovers=" + stopovers + ", preferences=" + preferences + ", specificFlights="
				+ specificFlights + "]";
	}

}
