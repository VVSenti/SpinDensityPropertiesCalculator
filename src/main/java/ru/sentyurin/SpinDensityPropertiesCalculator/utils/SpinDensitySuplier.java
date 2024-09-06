package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;

@Component
public class SpinDensitySuplier implements DataFromFileSuplier {
	/**
	 * Объекты этого класса получают данные спиновой плотности из файлов.
	 * На данный момент реализована возможность получать данные из .txt файлов.
	 */
	private final List<String> possibleExtentions = new ArrayList<String>();
	{
		possibleExtentions.add("txt");
	}
	
	private double getDiffVolume(List<double[]> rawData) {
		int pointsCount = rawData.size();
		double[] firstPoint = rawData.get(0);
		double[] lastPoin = rawData.get(pointsCount - 1);
		int xGridCount = 1, yGridCount = 1, zGridCount = 1;
		// индекс, в котором впервые изменится значение координаты по оси y
		// равен числу точек по оси z
		for (int i = 0; i < pointsCount; i++) {
			double[] point = rawData.get(i);
			if (point[1] != firstPoint[1]) {
				zGridCount = i;
				break;
			}
		}
		// индекс, в котором впервые изменится значение координаты по оси x
		// равен числу точек по оси y умножить на число точек по оси z
		for (int i = 2; i < pointsCount; i++) {
			double[] point = rawData.get(i * zGridCount);
			if (point[0] != firstPoint[0]) {
				yGridCount = i;
				break;
			}
		}
		xGridCount = pointsCount / (yGridCount * zGridCount);

		Double xWidth = lastPoin[0] - firstPoint[0];
		Double yWidth = lastPoin[1] - firstPoint[1];
		Double zWidth = lastPoin[2] - firstPoint[2];
		Double fullVolume = Math.abs(xWidth * yWidth * zWidth);
		return fullVolume / ((xGridCount - 1) * (yGridCount - 1) * (zGridCount - 1));
	}

	private List<double[]> getDataFromTXTFile(String filePath) throws IOException {
		List<double[]> rawData = new ArrayList<double[]>();
		try (BufferedReader input = new BufferedReader(new FileReader(filePath))) {
			while (true) {
				String line = input.readLine();
				if (line == null)
					break;
				double[] lineDataDoubles = Stream.of(line.split(" ")).filter(x -> !x.isBlank())
						.mapToDouble(Double::parseDouble).toArray();
				rawData.add(lineDataDoubles);
			}
		} catch (FileNotFoundException e) {
			System.out.print("No such file!");
		}
		System.out.println("");
		return rawData;
	}
	
	public SquareGridData getSquareGridDataFromFile(String filePath) throws IOException {
		/**
		 * Функция импортирует данные из файла.
		 * Возвращает спиновую плотность в виде объекта SquareGridData.
		 */
		if(filePath.endsWith(".txt")) {
			List<double[]> rawData = getDataFromTXTFile(filePath);
			double diffVolume = getDiffVolume(rawData);
			return new SquareGridData(rawData, diffVolume);
		}
		return null;
	}
	
	/**
	 * По расширению файла функция определяет можно ли из него импортировать спиновую плотность.
	 */
	public boolean applicableFile(File file) {
		String filePath = file.getPath();
		for (String fileExtention : possibleExtentions) {
			if (filePath.endsWith(fileExtention))
				return true;
		}
		return false;
	}
}
