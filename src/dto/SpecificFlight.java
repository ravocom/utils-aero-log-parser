package dto;

import java.util.Date;

public class SpecificFlight implements Comparable<SpecificFlight> {

	private String flightDesignator;

	private String segmentCode;

	private Date departureDateTime;

	private String flightSegmentRPH;

	private String routeRefNumber;

	private Date arrivalDateTime;

	public String getFlightDesignator() {
		return flightDesignator;
	}

	public void setFlightDesignator(String flightDesignator) {
		this.flightDesignator = flightDesignator;
	}

	public String getSegmentCode() {
		return segmentCode;
	}

	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}

	public Date getDepartureDateTime() {
		return departureDateTime;
	}

	public void setDepartureDateTime(Date departureDateTime) {
		this.departureDateTime = departureDateTime;
	}

	public void setFlightSegmentRPH(String flightSegmentRPH) {
		this.flightSegmentRPH = flightSegmentRPH;
	}

	public String getFlightSegmentRPH() {
		return flightSegmentRPH;
	}

	public String getRouteRefNumber() {
		return routeRefNumber;
	}

	public void setRouteRefNumber(String routeRefNumber) {
		this.routeRefNumber = routeRefNumber;
	}

	@Override
	public int compareTo(SpecificFlight o) {
		return this.departureDateTime.compareTo(o.getDepartureDateTime());
	}

	public Date getArrivalDateTime() {
		return arrivalDateTime;
	}

	public void setArrivalDateTime(Date arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

}
