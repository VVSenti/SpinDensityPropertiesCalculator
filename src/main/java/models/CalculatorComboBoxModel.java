package models;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.CalculationManager;
import utils.CalculatorInterruptable;

@Component
public class CalculatorComboBoxModel implements javax.swing.ComboBoxModel<CalculatorInterruptable> {
	@Autowired
	CalculationManager calculationManager;
	Set<ListDataListener> listeners = new HashSet<>();

	int counter = 0;

	int currentIndex = 0;

	public void setCalculationManager(CalculationManager calculationManager) {
		this.calculationManager = calculationManager;
	}

	@Override
	public int getSize() {
		return calculationManager.calculatorsList.size();
	}

	@Override
	public CalculatorInterruptable getElementAt(int index) {
		return calculationManager.calculatorsList.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		currentIndex = calculationManager.calculatorsList.indexOf(anItem);
		calculationManager.setCalculator((CalculatorInterruptable) anItem);
		for (ListDataListener listener : listeners) {
			listener.contentsChanged(new ListDataEvent(this, 0, 0, 1));
		}
	}

	@Override
	public Object getSelectedItem() {
		return calculationManager.calculatorsList.get(currentIndex);
	}

}
