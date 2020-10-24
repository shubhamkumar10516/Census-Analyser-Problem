package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface CSVBuilder<E> {

	public Iterator<E>  getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException;
		
}
