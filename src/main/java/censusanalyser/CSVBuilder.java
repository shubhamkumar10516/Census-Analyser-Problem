package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface CSVBuilder {

	public<E> Iterator<E>  getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException;
	public<E> List<E> getCSVFileList(Reader reader, Class<E> csvClass) throws CensusAnalyserException;
}
