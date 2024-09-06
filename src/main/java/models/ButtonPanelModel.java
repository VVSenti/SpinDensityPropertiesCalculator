package models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.CalculationManager;

import java.util.HashSet;
import java.util.Set;

import views.ButtonPanelModelListener;

@Component
public class ButtonPanelModel implements CalculationManagerListener, ButtonPanelListener {

	@Autowired
	public Set<ButtonPanelModelListener> listeners = new HashSet<>();
	@Autowired
	CalculationManager calculationManager;

	public void setCalculationManager(CalculationManager calculationManager) {
		this.calculationManager = calculationManager;
	}

	@Override
	public void planChanged(CalculationManager source) {
		if (source.getPlanSize() == 0) {
			for (ButtonPanelModelListener listener : listeners) {
				listener.planIsEmpty();
			}
		} else {
			for (ButtonPanelModelListener listener : listeners) {
				listener.planIsNotEmpty();
			}
		}
	}

	@Override
	public void calculationStarted(CalculationManager source) {
		for (ButtonPanelModelListener listener : listeners) {
			listener.calculationStarted();
		}
	}

	@Override
	public void calculationStoped(CalculationManager source) {
		for (ButtonPanelModelListener listener : listeners) {
			listener.calculationStoped();
		}
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
