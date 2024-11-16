package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import java.util.List;

/**
 * Изменяемый объект, который содержит в себе:
 * 
 * 1) поле data — список из массивов double[] координат (x, y, z) и значения в
 * этой точке
 * 
 * 2) Объем dxdydz (то есть произведение шагов сетки по трём осям) в поле
 * diffVolume
 */
public class SquareGridData {
	public List<double[]> data;
	public final double diffVolume;

	public SquareGridData(List<double[]> data, double diffVolume) {
		this.data = data;
		this.diffVolume = diffVolume;
	}

	public List<double[]> getData() {
		return data;
	}

	public double getDiffVolume() {
		return diffVolume;
	}

	/**
	 * Все значения становятся положительными. Новые значения нормируются на
	 * единицу.
	 */
	public SquareGridData absAndNormalize() {
		data.stream().parallel().forEach(x -> x[3] = Math.abs(x[3]));
		double integral = diffVolume * data.stream().parallel().mapToDouble(x -> x[3]).sum();
		data.stream().parallel().forEach(x -> x[3] /= integral);
		return this;
	}

	/**
	 * Удаляет значения меньше minValue
	 * 
	 * @param minValue
	 */
	public SquareGridData filterByValue(double minValue) {
		data = data.stream().parallel().filter(x -> x[3] > minValue).toList();
		return this;
	}
}
