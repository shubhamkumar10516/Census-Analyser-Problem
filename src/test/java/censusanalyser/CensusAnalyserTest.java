package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {
	private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
	private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
	private static final String INCORRECT_FILE_TYPE_GIVEN = "./src/main/resources/wrong_file_type.txt";
	private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";

	@Test
	public void givenIndianCensusCSVFileReturnsCorrectRecords() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			int numOfRecords = censusAnalyser.
					           loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
			System.out.println(numOfRecords);
			Assert.assertEquals(29, numOfRecords);
		} catch (CensusAnalyserException e) {
		}
	}

	@Test
	public void givenIndiaCensusDataCsvFileWithWrongFileShouldThrowException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			boolean check = CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM.equals(e.type)
					|| CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE.equals(e.type);
			Assert.assertTrue(check);
		}
	}

	@Test
	public void givenTheIndianCensusDataCsvFileIfTypeIsWrongThrowCensusAnalyserException()
			throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(INCORRECT_FILE_TYPE_GIVEN);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
			Assert.assertEquals(CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE, e.type);
		}
	}

	@Test
	public void givenCSVFileIfDelimiterIncorrectReturnsCensusAnalyserException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.DELIMITER_ISSUE, e.type);
		}
	}

	@Test
	public void whenCorrectCensusCSVFileButHeaderIncorrectShouldReturnFalse() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			boolean check = CensusAnalyserException.ExceptionType.DELIMITER_ISSUE.equals(e.type)
					|| CensusAnalyserException.ExceptionType.INCORRECT_HEADER_TYPE.equals(e.type);
			Assert.assertTrue(check);
		}
	}

	@Test
	public void givenIndianStateCodeCSVFileReturnsCorrectRecords() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			int numOfRecords = censusAnalyser.loadIndiaStateCode(INDIA_STATE_CODE_CSV_FILE_PATH);
			Assert.assertEquals(37, numOfRecords);
		} catch (CensusAnalyserException e) {
		}
	}

	@Test
	public void givenIndianStsteCodeCsvFileWithWrongFileShouldThrowException() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaStateCode(WRONG_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			boolean check = CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM.equals(e.type)
					|| CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE.equals(e.type);
			Assert.assertTrue(check);
		}
	}

	@Test
	public void givenTheIndianStateCodeCsvFileIfTypeIsWrongThrowCensusAnalyserException()
			throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaStateCode(INCORRECT_FILE_TYPE_GIVEN);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.INCORRECT_FILE_TYPE, e.type);
		}
	}

	@Test
	public void givenIndiaStateCodeCSVFileIfDelimiterIncorrectReturnsCensusAnalyserException()
			throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaStateCode(INDIA_STATE_CODE_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			Assert.assertEquals(CensusAnalyserException.ExceptionType.DELIMITER_ISSUE, e.type);
		}
	}

	@Test
	public void whenCorrectIndianStateCodeCSVFileButHeaderIncorrectShouldReturnFalse() throws IlleagalStateException {
		try {
			CensusAnalyser censusAnalyser = new CensusAnalyser();
			ExpectedException exceptionRule = ExpectedException.none();
			exceptionRule.expect(CensusAnalyserException.class);
			censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
		} catch (CensusAnalyserException e) {
			e.printStackTrace();
			boolean check = CensusAnalyserException.ExceptionType.DELIMITER_ISSUE.equals(e.type)
					|| CensusAnalyserException.ExceptionType.INCORRECT_HEADER_TYPE.equals(e.type);
			Assert.assertTrue(check);
		}
	}
}
