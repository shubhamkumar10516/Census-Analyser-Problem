package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import censusanalyser.CensusAnalyserException.ExceptionType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

	private static final String JSON_SORT_BY_POPULATION = null;
	List<IndiaCensusCSV> censusCSVList = null;
	List<IndiaStateCodeCSV> stateCodeList = null;

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
	public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException, IlleagalStateException {
		checkCorrectFileType(csvFilePath);
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			if (!checkHeader(reader)) {
				throw new CensusAnalyserException("Header is mismatching", ExceptionType.INCORRECT_HEADER_TYPE);
			}
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

	public String getStateNameWiseSortedCensusData() throws CensusAnalyserException {
		if (censusCSVList == null || censusCSVList.size() == 0) {
			throw new CensusAnalyserException("No List Available",
					CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
		}
		Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
		this.sort(censusComparator, censusCSVList);
		String sortedStateCensusJson = new Gson().toJson(censusCSVList);
		return sortedStateCensusJson;
	}

	//SORTING STATE CODE WISE
	public String getStateCodeWiseSortedCensusData() throws CensusAnalyserException {
		if (stateCodeList == null || stateCodeList.size() == 0) {
			throw new CensusAnalyserException("No List Available",
					CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
		}
		Comparator<IndiaStateCodeCSV> stateCodeComparator = Comparator.comparing(StateCode -> StateCode.stateCode);
		this.sort(stateCodeComparator, stateCodeList);
		String sortedStateCensusJson = new Gson().toJson(stateCodeList);
		return sortedStateCensusJson;
	}
	
	// sorting population wise
	public String getPopulationWiseSortedData() throws CensusAnalyserException {
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(JSON_SORT_BY_POPULATION))) {
			if (censusCSVList == null || censusCSVList.size() == 0) {
				throw new CensusAnalyserException("No List Available",
						CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
			}
			Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.population);
			this.sort(censusComparator, censusCSVList);
			Collections.reverse(censusCSVList);
			String sortedStateCensusJson = new Gson().toJson(censusCSVList);
			bufferedWriter.write(sortedStateCensusJson);
			return sortedStateCensusJson;
		}	catch (IOException e) {
			throw new CensusAnalyserException("No List Available",
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		}
	}

	// sorting the data of list
	private <E> void sort(Comparator<E> censusComparator, List<E> sortList) {
		for (int i = 0; i < sortList.size() - 1; i++) {
			for (int j = 0; j < sortList.size() - i - 1; j++) {
				E census1 = sortList.get(j);
				E census2 = sortList.get(j + 1);
				if (censusComparator.compare(census1, census2) > 0) {
					sortList.set(j, census2);
					sortList.set(j + 1, census1);
				}
			}
		}
	}
}
