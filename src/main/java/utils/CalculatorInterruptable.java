package utils;

import java.io.IOException;

import models.SquareGridData;

public interface CalculatorInterruptable {
	double calculate(SquareGridData data) throws IOException, InterruptedException;
	void interrupt();
}
