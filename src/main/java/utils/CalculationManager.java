package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import models.CalculationManagerListener;
import models.SpinDensityTaskBeanFactory;
import models.SquareGridData;
import models.TaskBean;

@Component
public class CalculationManager {
	private CalculatorInterruptable calculator;
	private final List<TaskBean> plan = new ArrayList<>();
	private final Set<String> filePaths = new HashSet<>();
	private boolean working = false;
	private boolean toStop = false;
	private Double valueThreshhol = 1e-6;
	
	Thread calculationThread;

	@Autowired
	private DataFromFileSuplier dataFromFileSuplier;
	@Autowired
	private Set<CalculationManagerListener> listeners;
	@Autowired
	public List<CalculatorInterruptable> calculatorsList;
	@Autowired
	private SpinDensityTaskBeanFactory taskBeanFactory;

	public void addTasks(File[] files) {
		for (File file : files) {
			String filePath = file.getPath();
			if (!filePaths.contains(filePath) && dataFromFileSuplier.applicableFile(file)) {
				plan.add(taskBeanFactory.getSpinDensityTaskBean(file));
				filePaths.add(filePath);
			}
		}
		for (CalculationManagerListener listener : listeners) {
			listener.planChanged(this);
		}
	}

	public void removeTask(int index) {
		if (index < 0 || index >= plan.size())
			return;
		TaskBean toRemoveBean = plan.get(index);
		if (!working || toRemoveBean.canBeRemovedWhileWorking()) {
			TaskBean removedBean = plan.remove(index);
			filePaths.remove(removedBean.getFilePath());
			for (CalculationManagerListener listener : listeners) {
				listener.planChanged(this);
			}
		}
	}

	public void moveTaskUp(int index) {
		if (index <= 0 || index >= plan.size())
			return;
		TaskBean toSwopBean1 = plan.get(index);
		TaskBean toSwopBean2 = plan.get(index - 1);
		if (!working || (toSwopBean1.canBeRemovedWhileWorking() && toSwopBean2.canBeRemovedWhileWorking())) {
			plan.add(index - 1, toSwopBean1);
			plan.remove(index + 1);
			for (CalculationManagerListener listener : listeners) {
				listener.planChanged(this);
			}
		}
	}

	public void moveTaskDown(int index) {
		if (index < 0 || index >= plan.size() - 1)
			return;
		TaskBean toSwopBean1 = plan.get(index);
		TaskBean toSwopBean2 = plan.get(index + 1);
		if (!working || (toSwopBean1.canBeRemovedWhileWorking() && toSwopBean2.canBeRemovedWhileWorking())) {
			plan.add(index + 2, toSwopBean1);
			plan.remove(index);
			for (CalculationManagerListener listener : listeners) {
				listener.planChanged(this);
			}
		}
	}

	public void removeAllTasks() {
		if (!working) {
			plan.clear();
			filePaths.clear();
		} else {
			int index = plan.size() - 1;
			TaskBean lastBean = plan.get(index);
			while (lastBean.canBeRemovedWhileWorking()) {
				plan.remove(index--);
				filePaths.remove(lastBean.getFilePath());
				lastBean = plan.get(index);
			}
		}
		for (CalculationManagerListener listener : listeners) {
			listener.planChanged(this);
		}
	}

	private void runTask(TaskBean taskBean) {
		taskBean.setRunning();
		String filePath = taskBean.getFilePath();
		Double result = 0.0;
		try {
			SquareGridData data = dataFromFileSuplier.getSquareGridDataFromFile(filePath);
			data.absAndNormalize().filterByValue(valueThreshhol);
			result = calculator.calculate(data);
			if (toStop) {
				taskBean.setInterrupted();
			} else {
				taskBean.setDone();
				taskBean.setResult(result);
			}
		} catch (Exception e) {
			taskBean.setInterrupted();
			e.printStackTrace();
		}
		for (CalculationManagerListener listener : listeners) {
			listener.planChanged(this);
		}
	}

	public void startCalculation() {
		toStop = false;
		working = true;
		for (CalculationManagerListener listener : listeners) {
			listener.calculationStarted(this);
		}
		Runnable taskRunnable = () -> {
			try {
				for (TaskBean taskBean : plan) {
					if (toStop)
						break;
					if (taskBean.canBeRun())
						runTask(taskBean);
				}
			} finally {
				toStop = false;
				working = false;
				for (CalculationManagerListener listener : listeners) {
					listener.calculationStoped(this);
				}
			}
		};

		calculationThread = new Thread(taskRunnable);
		calculationThread.start();
	}

	public void stopCalculation() {
		toStop = true;
		calculator.interrupt();
		for (CalculationManagerListener listener : listeners) {
			listener.calculationStoped(this);
		}
	}

	public int getPlanSize() {
		return plan.size();
	}

	public TaskBean getTaskBean(int index) {
		return plan.get(index);
	}

	public void addListener(CalculationManagerListener listener) {
		listeners.add(listener);
	}

	public void removeListener(CalculationManagerListener listener) {
		listeners.remove(listener);
	}

	public String getPlanTextPresentation() {
		String delimiter = "\t";
		StringBuilder text = new StringBuilder();
		for (TaskBean taskBean : plan) {
			text.append(taskBean.getFileName());
			text.append(delimiter);
			if (taskBean.isDone())
				text.append(taskBean.getResult());
			text.append("\n");
		}
		return text.toString();
	}
	
	public void setValueThreshhol(Double valueThreshhol) {
		this.valueThreshhol = valueThreshhol;
	}
	
	public void setCalculator(CalculatorInterruptable calculator) {
		this.calculator = calculator;
	}

	@PostConstruct
	private void springContextPostConstruct() {
		calculator = calculatorsList.get(0);
	}

}
