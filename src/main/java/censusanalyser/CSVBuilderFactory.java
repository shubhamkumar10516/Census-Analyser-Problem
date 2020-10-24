package censusanalyser;

public class CSVBuilderFactory {

	public static CSVBuilder createCSVBuilder() {
		return new OpenCSVBuilder();
	}
}
