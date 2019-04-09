public class Harness {

	public static void main(String[] args) {
		String dateStr = "20190801";
		String out = dateStr.substring(6, 8);

		String converted = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8)
				+ "T00:00:00";

		System.out.println(converted);

	}

}
