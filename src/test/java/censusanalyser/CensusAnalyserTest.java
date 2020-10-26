package censusanalyser;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.gson.Gson;

public class CensusAnalyserTest {
	private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
	private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
	private static final String INCORRECT_FILE_TYPE_GIVEN = "./src/main/resources/wrong_file_type.txt";
	private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
	public CensusAnalyser censusAnalyser = null;

	@Before
	public void intitializeTest() {
		CensusAnalyser censusAnalyser = new CensusAnalyser();
		ExpectedException exceptionRule = ExpectedException.none();
		exceptionRule.expect(CensusAnalyserException.class);
	}

	@Test
	public void givenIndianCensusCSVFileReturnsCorrectRecords() throws IlleagalStateException {
		try {
			int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
			System.out.println(numOfRecords);
			Assert.assertEquals(29, numOfRecords);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void givenIndiaCensusDataCsvFileWithWrongFileShouldThrowException() throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
		}
	}

	@Test
	public void givenTheIndianCensusDataCsvFileIfTypeIsWrongThrowCensusAnalyserException()
			throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaCensusData(INCORRECT_FILE_TYPE_GIVEN);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
			Assert.assertEquals(CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE, e.type);
		}
	}

	@Test
	public void givenCSVFileIfDelimiterIncorrectReturnsCensusAnalyserException() throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.DELIMITER_ISSUE, e.type);
		}
	}

	@Test
	public void whenCorrectCensusCSVFileButHeaderIncorrectShouldReturnFalse() throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
			Assert.assertTrue(CensusAnalyserException.ExceptionType.INCORRECT_HEADER_TYPE.equals(e.type));
		}
	}

	@Test
	public void givenIndianStateCodeCSVFileReturnsCorrectRecords() throws IlleagalStateException {
		try {
			int numOfRecords = censusAnalyser.loadIndiaStateCode(INDIA_STATE_CODE_CSV_FILE_PATH);
			Assert.assertEquals(37, numOfRecords);
		} catch (CensusAnalyserException e) {
		}
	}

	@Test
	public void givenIndianStsteCodeCsvFileWithWrongFileShouldThrowException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			censusAnalyser.loadIndiaStateCode(WRONG_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
		}
	}

	@Test
	public void givenTheIndianStateCodeCsvFileIfTypeIsWrongThrowCensusAnalyserException()
			throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			censusAnalyser.loadIndiaStateCode(INCORRECT_FILE_TYPE_GIVEN);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE, e.type);
		}
	}

	@Test
	public void givenIndiaStateCodeCSVFileIfDelimiterIncorrectReturnsCensusAnalyserException()
			throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaStateCode(INDIA_STATE_CODE_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertNotEquals(CensusAnalyserException.ExceptionType.DELIMITER_ISSUE, e.type);
		}
	}

	@Test
	public void whenCorrectIndianStateCodeCSVFileButHeaderIncorrectShouldReturnFalse() throws IlleagalStateException {
		try {
			censusAnalyser.loadIndiaStateCode(INDIA_STATE_CODE_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
			Assert.assertFalse(CensusAnalyserException.ExceptionType.INCORRECT_HEADER_TYPE.equals(e.type));
		}
	}

	@Test
	public void givenIndianCensusDataSortedByStateName() {
		try {
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
			String stateNameWiseSortedCensusData = censusAnalyser.getStateNameWiseSortedCensusData();
			IndiaCensusCSV[] censusCSV = new Gson().fromJson(stateNameWiseSortedCensusData, IndiaCensusCSV[].class);
			assertEquals("Andhra Pradesh", censusCSV[0].state);
		} catch (CensusAnalyserException e) {
			System.out.println(e.getMessage());
		}
	}
}
