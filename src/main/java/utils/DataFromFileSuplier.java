package utils;

import java.io.File;
import java.io.IOException;

import models.SquareGridData;

public interface DataFromFileSuplier {
	SquareGridData getSquareGridDataFromFile(String filePath) throws IOException;
	boolean applicableFile(File file);
}
