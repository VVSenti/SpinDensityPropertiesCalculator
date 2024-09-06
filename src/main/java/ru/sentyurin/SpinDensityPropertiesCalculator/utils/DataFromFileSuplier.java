package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;

@Component
public interface DataFromFileSuplier {
	SquareGridData getSquareGridDataFromFile(String filePath) throws IOException;
	boolean applicableFile(File file);
}
