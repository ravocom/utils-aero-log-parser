package dto;

import java.sql.Date;

public class BasicOndInfo {

	private String origin;

	private String destination;

	private boolean originCity;

	private boolean destinationCity;

	private Date departureDateTime;

	private int departureVariance;

	private Date arrivalDateTime;

	public Date getDepartureDateTime() {
		return departureDateTime;
	}

	public void setDepartureDateTime(Date departureDateTime) {
		this.departureDateTime = departureDateTime;
	}

	public int getDepartureVariance() {
		return departureVariance;
	}

	public void setDepartureVariance(int departureVariance) {
		this.departureVariance = departureVariance;
	}

	public Date getArrivalDateTime() {
		return arrivalDateTime;
	}

	public void setArrivalDateTime(Date arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isOriginCity() {
		return originCity;
	}

	public void setOriginCity(boolean originCity) {
		this.originCity = originCity;
	}

	public boolean isDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(boolean destinationCity) {
		this.destinationCity = destinationCity;
	}
}
