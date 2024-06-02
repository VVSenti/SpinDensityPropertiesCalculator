import java.beans.PropertyChangeListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import businessDomain.CalculationManager;
import businessDomain.CoulombIntegralCalculator;
import businessDomain.DataFromFileSuplier;
import businessDomain.SpinDensitySuplier;
import businessDomain.SpinDensityTaskBeanFactory;
import businessDomain.ShennonEntropyCalculator;
import modelClasses.ButtonPanelModel;
import modelClasses.CalculatorComboBoxModel;
import modelClasses.FilesTableModel;
import modelClasses.ThresholdTextFieldListener;
import viewClasses.ButtonPanel;
import viewClasses.FilesTable;
import viewClasses.MainWindow;
import viewClasses.SettingsPanel;

@Configuration
public class SpringConfig {

	@Bean
	SpinDensityTaskBeanFactory spinDensityTaskBeanFactory() {
		return new SpinDensityTaskBeanFactory();
	}

	@Bean
	DataFromFileSuplier dataFromFileSuplier() {
		return new SpinDensitySuplier();
	}

	@Bean
	public CalculationManager calculationManager() {
		return new CalculationManager();
	}

	@Bean
	public CoulombIntegralCalculator coulombIntegralCalculator() {
		return new CoulombIntegralCalculator();
	}

	@Bean
	public ShennonEntropyCalculator shennonEntropyCalculator() {
		return new ShennonEntropyCalculator();
	}

	@Bean
	public FilesTableModel filesTableModel() {
		return new FilesTableModel();
	}

	@Bean
	public FilesTable filesTable(FilesTableModel filesTableModel) {
		return new FilesTable(filesTableModel);
	}

	@Bean
	public ButtonPanelModel buttonPanelModel() {
		return new ButtonPanelModel();
	}

	@Bean
	PropertyChangeListener thresholdTextFieldListener() {
		return new ThresholdTextFieldListener();
	}

	@Bean
	public ButtonPanel buttonPanel() {
		return new ButtonPanel();
	}

	@Bean
	public CalculatorComboBoxModel calculatorComboBoxModel() {
		return new CalculatorComboBoxModel();
	}

	@Bean
	SettingsPanel settingsPanel(CalculatorComboBoxModel calculatorComboBoxModel,
			PropertyChangeListener thresholdTextFieldListener) {
		return new SettingsPanel(calculatorComboBoxModel, thresholdTextFieldListener);
	}

	@Bean
	MainWindow mainWindow() {
		return new MainWindow();
	}

}
