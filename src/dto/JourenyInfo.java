package dto;

import java.io.Serializable;

public class JourenyInfo implements Serializable, Comparable<JourenyInfo> {

	private static final long serialVersionUID = 1L;
	private static final String COMMA = ",";

	private String origin;
	private String detination;
	private int windowBefore;
	private int windowAfter;
	private String departureDate;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDetination() {
		return detination;
	}

	public void setDetination(String detination) {
		this.detination = detination;
	}

	public int getWindowBefore() {
		return windowBefore;
	}

	public void setWindowBefore(int windowBefore) {
		this.windowBefore = windowBefore;
	}

	public int getWindowAfter() {
		return windowAfter;
	}

	public void setWindowAfter(int windowAfter) {
		this.windowAfter = windowAfter;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(origin).append(COMMA).append(detination).append(COMMA).append(departureDate).append(COMMA);
		return sb.toString();

	}

	public String log() {
		return "JourenyInfo [origin=" + origin + ", detination=" + detination + ", windowBefore=" + windowBefore
				+ ", windowAfter=" + windowAfter + ", departureDate=" + departureDate + "]";
	}

	@Override
	public int compareTo(JourenyInfo o) {
		int comparator = 0;
		if (this.getDepartureDate() != null && o.getDepartureDate() != null) {
			comparator = this.getDepartureDate().compareTo(o.getDepartureDate());

		}
		return comparator;

	}

}
