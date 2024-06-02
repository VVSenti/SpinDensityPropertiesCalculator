package businessDomain;

import java.io.File;
import java.io.IOException;

public interface DataFromFileSuplier {
	SquareGridData getSquareGridDataFromFile(String filePath) throws IOException;
	boolean applicableFile(File file);
}
