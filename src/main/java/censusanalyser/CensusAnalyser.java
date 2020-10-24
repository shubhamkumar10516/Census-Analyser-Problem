package censusanalyser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import censusanalyser.CensusAnalyserException.ExceptionType;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

	@SuppressWarnings("unchecked")
	public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
		checkCorrectFileType(csvFilePath);
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			CSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			Iterator<IndiaCensusCSV> censusCSVIterator = new OpenCSVBuilder().getCSVFileIterator(reader,IndiaCensusCSV.class);
			return getCount(censusCSVIterator);
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
		}
	}

	// State Code Data
	@SuppressWarnings("rawtypes")
	public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException, IlleagalStateException {
		checkCorrectFileType(csvFilePath);
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			@SuppressWarnings("unchecked")
			Iterator<IndiaStateCodeCSV> censusCSVIterator = new OpenCSVBuilder().getCSVFileIterator(reader,IndiaStateCodeCSV.class);
			return getCount(censusCSVIterator);
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException("there can be delimiter issue in file",CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
		}
	}

	// get count of entries
	private <E> int getCount(Iterator<E> iterator) {
		Iterable<E> csvIterable = () -> iterator;
		int numOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
		return numOfEateries;
	}

	// Check for correct file type
	private void checkCorrectFileType(String csvFilePath) throws CensusAnalyserException {
		int lastIndex = csvFilePath.lastIndexOf(".");
		if (!csvFilePath.substring(lastIndex).equals(".csv"))
			throw new CensusAnalyserException("Enter correct file extension", ExceptionType.INCORRECT_FILE_TYPE);
	}

	// checking for header correctness
	private boolean checkHeader(Reader reader) throws IOException {
		CSVReader csvReader = new CSVReader(reader);
		String[] nextRecord;
		nextRecord = csvReader.readNext();
		boolean result = nextRecord[0].equals("State") && nextRecord[1].equals("Population")
				&& nextRecord[2].equals("AreaInSqKm") && nextRecord[3].equals("DensityPerSqKm");
		return result;
	}
}
