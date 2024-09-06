import java.beans.PropertyChangeListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import models.ButtonPanelModel;
import models.CalculatorComboBoxModel;
import models.FilesTableModel;
import models.SpinDensityTaskBeanFactory;
import models.ThresholdTextFieldListener;
import utils.CalculationManager;
import utils.CoulombIntegralCalculator;
import utils.DataFromFileSuplier;
import utils.ShennonEntropyCalculator;
import utils.SpinDensitySuplier;
import views.ButtonPanel;
import views.FilesTable;
import views.MainWindow;
import views.SettingsPanel;

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
