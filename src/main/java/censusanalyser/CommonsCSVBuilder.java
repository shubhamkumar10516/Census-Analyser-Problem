package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import censusanalyser.CensusAnalyserException.ExceptionType;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;

public class CommonsCSVBuilder implements CSVBuilder {

	@Override
	@SuppressWarnings("unchecked")
	public <E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException {
		try {
			CSVParser parser = CSVParser.parse(reader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			Iterator<CSVRecord> records = parser.iterator();
			return (Iterator<E>) records;
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(), ExceptionType.UNABLE_TO_PARSE);
		}
	}

	@Override
	public <E> List<E> getCSVFileList(Reader reader, Class<E> csvClaSss) throws CensusAnalyserException {
		try {
			CSVParser parser = CSVParser.parse(reader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			return (List<E>) parser.getRecords();
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(), ExceptionType.UNABLE_TO_PARSE);
		}
	}
}
