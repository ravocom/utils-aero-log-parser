package dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Converter {

	public static void main(String[] args) {

		FileReader fr;
		try {
			fr = new FileReader("/home/rimaz/oracle_utils/utils-aero-log-parser/src/input.txt");

			BufferedReader br = new BufferedReader(fr);

			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = br.readLine()) != null) {

				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append("'");
				sb.append(line.trim());
				sb.append("'");
			}

			System.out.println(sb.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
