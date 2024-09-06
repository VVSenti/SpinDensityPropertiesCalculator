package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.IOException;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;

public interface CalculatorInterruptable {
	double calculate(SquareGridData data) throws IOException, InterruptedException;
	void interrupt();
}
