package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;

import java.io.IOException;
import java.util.List;

@Component
public class ShennonEntropyCalculator implements CalculatorInterruptable {
	@SuppressWarnings("unused")
	private volatile boolean toInterrupt = false;

	@Override
	public String toString() {
		return "Shennon entropy";
	}

	@Override
	public double calculate(SquareGridData squareGridData) throws IOException, InterruptedException {
		toInterrupt = false;
		double diffVolume = squareGridData.getDiffVolume();
		List<double[]> data = squareGridData.getData();
		System.out.println("diffVolume = " + diffVolume);
		return diffVolume * data.stream().parallel().mapToDouble(x -> x[3]).map(i -> Math.log(i) * i).sum();
	}

	@Override
	public void interrupt() {
		toInterrupt = true;
	}
}
