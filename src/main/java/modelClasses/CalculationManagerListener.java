package modelClasses;

import businessDomain.CalculationManager;

public interface CalculationManagerListener {
	void planChanged(CalculationManager source);
	void calculationStarted(CalculationManager source);
	void calculationStoped(CalculationManager source);
}
