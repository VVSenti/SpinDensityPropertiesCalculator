package ru.sentyurin.SpinDensityPropertiesCalculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpinDensityPropertiesCalculatorApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpinDensityPropertiesCalculatorApplication.class)
				.headless(false).run(args);
	}

}
