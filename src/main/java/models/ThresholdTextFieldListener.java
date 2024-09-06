package models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.CalculationManager;

@Component
public class ThresholdTextFieldListener implements PropertyChangeListener {
	
	@Autowired
	CalculationManager calculationManager;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		int newValue = (int) evt.getNewValue();
		double newThreshold = Math.pow(10, newValue);
		calculationManager.setValueThreshhol(newThreshold);
	}

}
