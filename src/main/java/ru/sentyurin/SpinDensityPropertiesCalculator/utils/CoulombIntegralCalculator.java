package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;

@Component
public class CoulombIntegralCalculator implements CalculatorInterruptable {
	private volatile boolean toInterrupt = false;

	@Override
	public String toString() {
		return "Coulomb integral";
	}

	private double getCoulombIntegralInterruptable(List<double[]> data, double diffVolume)
			throws InterruptedException {
		AtomicInteger startPointIndex = new AtomicInteger();
		final int maxThreads = Runtime.getRuntime().availableProcessors() - 1;
		final int pointCount = data.size();

		ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
		List<Future<Double>> temporalResults = new ArrayList<Future<Double>>(maxThreads);

		for (int i = 0; i < maxThreads; i++) {
			Callable<Double> task = new Callable<Double>() {
				public Double call() {
					double integral = 0.0;
					boolean stop = false;
					while (!stop) {
						int startingIndex = startPointIndex.getAndIncrement();
						if (startingIndex >= pointCount)
							break;
						double[] firstPoint = data.get(startingIndex);
						double temp = 0.0;
						for (int m = startingIndex + 1; m < pointCount; m++) {
							double[] secondPoint = data.get(m);
							double distanceSquare = Math.pow(firstPoint[0] - secondPoint[0], 2);
							distanceSquare += (firstPoint[1] - secondPoint[1])
									* (firstPoint[1] - secondPoint[1]);
							distanceSquare += (firstPoint[2] - secondPoint[2])
									* (firstPoint[2] - secondPoint[2]);
							temp += secondPoint[3] / Math.sqrt(distanceSquare);
						}
						integral += firstPoint[3] * temp;
						stop = toInterrupt;
					}
					return integral;
				}
			};
			temporalResults.add(executor.submit(task));
		}

		Double integral = 0.0;
		for (Future<Double> futureValue : temporalResults) {
			try {
				integral += futureValue.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return integral * diffVolume * diffVolume * 627.5095 * 2.0 / 1.8897259886;
	}

	/**
	 * Вычисляет значение интеграла спиновой плотности.
	 */
	@Override
	public double calculate(SquareGridData squareGridData)
			throws IOException, InterruptedException {
		double diffVolume = squareGridData.getDiffVolume();
		toInterrupt = false;
		return getCoulombIntegralInterruptable(squareGridData.getData(), diffVolume);
	}

	@Override
	public void interrupt() {
		toInterrupt = true;
	}
}
