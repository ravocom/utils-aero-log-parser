package dto;

public class BasePreferences {

	private String seatType;

	private String cabinClass;

	private String logicalCabinClass;

	private String bookingCode;

	private AdditionalPreferences additionalPreferences;

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	public String getLogicalCabinClass() {
		return logicalCabinClass;
	}

	public void setLogicalCabinClass(String logicalCabinClass) {
		this.logicalCabinClass = logicalCabinClass;
	}

	public AdditionalPreferences getAdditionalPreferences() {
		if (additionalPreferences == null) {
			additionalPreferences = new AdditionalPreferences();
		}
		return additionalPreferences;
	}

	public void setAdditionalPreferences(AdditionalPreferences additionalPreferences) {
		this.additionalPreferences = additionalPreferences;
	}

	public String getBookingCode() {
		return bookingCode;
	}

	public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
	}

}
