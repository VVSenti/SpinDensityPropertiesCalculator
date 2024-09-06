package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class SpinDensityTaskBeanFactory {
	public SpinDensityTaskBean getSpinDensityTaskBean(File file) {
		return new SpinDensityTaskBean(file);
	}
}
