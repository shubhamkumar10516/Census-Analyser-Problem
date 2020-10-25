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
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

	List<IndiaCensusCSV> censusCSVList = null;
	List<IndiaStateCodeCSV> stateCodeList = null;

	@SuppressWarnings("unchecked")
	public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
		checkCorrectFileType(csvFilePath);
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			if (!checkHeader(reader)) {
				throw new CensusAnalyserException("Header is mismatching", ExceptionType.INCORRECT_HEADER_TYPE);
			}
			CSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			censusCSVList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
			return censusCSVList.size();
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
		}
	}

	// State Code Data
	@SuppressWarnings("rawtypes")
	public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException, IlleagalStateException {
		checkCorrectFileType(csvFilePath);
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			if (!checkHeader(reader)) {
				throw new CensusAnalyserException("Header is mismatching", ExceptionType.INCORRECT_HEADER_TYPE);
			}
			@SuppressWarnings("unchecked")
			CSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			stateCodeList = csvBuilder.getCSVFileList(reader, IndiaStateCodeCSV.class);
			return stateCodeList.size();
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException("there can be delimiter issue in file",
					CensusAnalyserException.ExceptionType.DELIMITER_ISSUE);
		}
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
		boolean result = (nextRecord[0].equals("State") && nextRecord[1].equals("Population")
				&& nextRecord[2].equals("AreaInSqKm") && nextRecord[3].equals("DensityPerSqKm"))
				|| (nextRecord[0].equals("SrNo") && nextRecord[1].equals("State Name") && nextRecord[2].equals("TIN")
						&& nextRecord[3].equals("StateCode"));
		return result;
	}
}
