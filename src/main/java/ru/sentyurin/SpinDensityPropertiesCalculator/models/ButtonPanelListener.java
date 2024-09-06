package ru.sentyurin.SpinDensityPropertiesCalculator.models;

public interface ButtonPanelListener {
	void buttonPressed(ControlButtons button);	
	void buttonPressedWithIntMessage(ControlButtons button, int message);	
}
