package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.utils.CalculationManager;

import java.util.HashSet;
import java.util.Set;

import ru.sentyurin.SpinDensityPropertiesCalculator.views.ButtonPanelModelListener;

@Component
public class ButtonPanelModel implements CalculationManagerListener, ButtonPanelListener {

	public Set<ButtonPanelModelListener> listeners = new HashSet<>();
	private final CalculationManager calculationManager;

	@Autowired
	public ButtonPanelModel(CalculationManager calculationManager) {
		this.calculationManager = calculationManager;
		calculationManager.addListener(this);
	}

	@Override
	public void planChanged(CalculationManager source) {
		if (source.getPlanSize() == 0) {
			listeners.forEach(l -> l.planIsEmpty());
		} else {
			listeners.forEach(l -> l.planIsNotEmpty());
		}
	}

	@Override
	public void calculationStarted(CalculationManager source) {
		listeners.forEach(l -> l.calculationStarted());
	}

	@Override
	public void calculationStoped(CalculationManager source) {
		listeners.forEach(l -> l.calculationStoped());
	}

	public void addListener(ButtonPanelModelListener buttonPanelModelListener) {
		listeners.add(buttonPanelModelListener);
	}

	@Override
	public void buttonPressed(ControlButtons button) {
		button.eval(calculationManager);
	}

	@Override
	public void buttonPressedWithIntMessage(ControlButtons button, int message) {
		button.evalWithIntMessage(calculationManager, message);
	}

}
