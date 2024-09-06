package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.utils.CalculationManager;

@Component
public class ThresholdTextFieldListener implements PropertyChangeListener {

	private final CalculationManager calculationManager;

	@Autowired
	public ThresholdTextFieldListener(CalculationManager calculationManager) {
		this.calculationManager = calculationManager;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		int newValue = (int) evt.getNewValue();
		double newThreshold = Math.pow(10, newValue);
		calculationManager.setValueThreshhol(newThreshold);
	}

}
