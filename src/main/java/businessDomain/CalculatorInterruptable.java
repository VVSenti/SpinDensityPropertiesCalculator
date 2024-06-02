package businessDomain;

import java.io.IOException;

public interface CalculatorInterruptable {
	double calculate(SquareGridData data) throws IOException, InterruptedException;
	void interrupt();
}
