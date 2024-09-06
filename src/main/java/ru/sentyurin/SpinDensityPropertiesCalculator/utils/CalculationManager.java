package ru.sentyurin.SpinDensityPropertiesCalculator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.CalculationManagerListener;
import ru.sentyurin.SpinDensityPropertiesCalculator.models.SpinDensityTaskBeanFactory;
import ru.sentyurin.SpinDensityPropertiesCalculator.models.SquareGridData;
import ru.sentyurin.SpinDensityPropertiesCalculator.models.TaskBean;

@Component
public class CalculationManager {
	private CalculatorInterruptable calculator;
	private final List<TaskBean> plan = new ArrayList<>();
	private final Set<String> filePaths = new HashSet<>();
	private boolean isWorking = false;
	private boolean toStop = false;
	private Double valueThreshhol = 1e-6;

	Thread calculationThread;

	@Autowired
	public List<CalculatorInterruptable> calculatorsList;
	private Set<CalculationManagerListener> listeners = new HashSet<>();
	private DataFromFileSuplier dataFromFileSuplier;
	private SpinDensityTaskBeanFactory taskBeanFactory;

	@Autowired
	public CalculationManager(DataFromFileSuplier dataFromFileSuplier,
			SpinDensityTaskBeanFactory taskBeanFactory) {
		this.dataFromFileSuplier = dataFromFileSuplier;
		this.taskBeanFactory = taskBeanFactory;
	}

	public void addTasks(File[] files) {
		for (File file : files) {
			String filePath = file.getPath();
			if (!filePaths.contains(filePath) && dataFromFileSuplier.applicableFile(file)) {
				plan.add(taskBeanFactory.getSpinDensityTaskBean(file));
				filePaths.add(filePath);
			}
		}
		listeners.forEach(l -> l.planChanged(this));
	}

	public void removeTask(int index) {
		if (index < 0 || index >= plan.size())
			return;
		TaskBean toRemoveBean = plan.get(index);
		if (!isWorking || toRemoveBean.canBeRemovedWhileWorking()) {
			TaskBean removedBean = plan.remove(index);
			filePaths.remove(removedBean.getFilePath());
			listeners.forEach(l -> l.planChanged(this));
		}
	}

	public void moveTaskUp(int index) {
		if (index <= 0 || index >= plan.size())
			return;
		TaskBean toSwopBean1 = plan.get(index);
		TaskBean toSwopBean2 = plan.get(index - 1);
		if (!isWorking || (toSwopBean1.canBeRemovedWhileWorking()
				&& toSwopBean2.canBeRemovedWhileWorking())) {
			plan.add(index - 1, toSwopBean1);
			plan.remove(index + 1);
			listeners.forEach(l -> l.planChanged(this));
		}
	}

	public void moveTaskDown(int index) {
		if (index < 0 || index >= plan.size() - 1)
			return;
		TaskBean toSwopBean1 = plan.get(index);
		TaskBean toSwopBean2 = plan.get(index + 1);
		if (!isWorking || (toSwopBean1.canBeRemovedWhileWorking()
				&& toSwopBean2.canBeRemovedWhileWorking())) {
			plan.add(index + 2, toSwopBean1);
			plan.remove(index);
			listeners.forEach(l -> l.planChanged(this));
		}
	}

	public void removeAllTasks() {
		if (!isWorking) {
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
		listeners.forEach(l -> l.planChanged(this));
	}

	public void startCalculation() {
		toStop = false;
		isWorking = true;
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
				isWorking = false;
				listeners.forEach(l -> l.calculationStoped(this));
			}
		};

		calculationThread = new Thread(taskRunnable);
		calculationThread.start();
	}

	public void stopCalculation() {
		toStop = true;
		calculator.interrupt();
		listeners.forEach(l -> l.calculationStoped(this));
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
