package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.IOException;
import java.util.List;
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

	private double getCoulombIntegralInterruptable(List<double[]> data, double diffVolume) throws InterruptedException {
		AtomicInteger startPointIndex = new AtomicInteger();
		int maxThreads = Runtime.getRuntime().availableProcessors() - 1;
		int pointCount = data.size();
		DoubleAdder integral = new DoubleAdder();

		Thread[] threads = new Thread[maxThreads];

		for (int i = 0; i < maxThreads; i++) {
			Runnable task = new Runnable() {
				@Override
				public void run() {
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
							distanceSquare += (firstPoint[1] - secondPoint[1]) * (firstPoint[1] - secondPoint[1]);
							distanceSquare += (firstPoint[2] - secondPoint[2]) * (firstPoint[2] - secondPoint[2]);
							temp += secondPoint[3] / Math.sqrt(distanceSquare);
						}
						integral.add(firstPoint[3] * temp);
						stop = toInterrupt;
					}
				}
			};
			Thread thread = new Thread(task);
			thread.start();
			threads[i] = thread;
		}

		for (Thread x : threads) {
			try {
				x.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return integral.doubleValue() * diffVolume * diffVolume * 627.5095 * 2.0 / 1.8897259886;
	}

	/**
	 * Вычисляет значение интеграла спиновой плотности.
	 */
	@Override
	public double calculate(SquareGridData squareGridData) throws IOException, InterruptedException {
		double diffVolume = squareGridData.getDiffVolume();
		toInterrupt = false;
		return getCoulombIntegralInterruptable(squareGridData.getData(), diffVolume);
	}

	@Override
	public void interrupt() {
		toInterrupt = true;
	}
}
