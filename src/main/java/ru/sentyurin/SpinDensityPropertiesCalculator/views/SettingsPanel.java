package ru.sentyurin.SpinDensityPropertiesCalculator.views;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.CalculatorComboBoxModel;
import ru.sentyurin.SpinDensityPropertiesCalculator.utils.CalculatorInterruptable;

@SuppressWarnings("serial")
@Component
public class SettingsPanel extends JPanel {

	JFormattedTextField thresholdTextField;

	public SettingsPanel(CalculatorComboBoxModel calculatorComboBoxModel,
			PropertyChangeListener thresholdTextFieldListener) {
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(BOTTOM_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel aLabel = new JLabel("Settings");
		aLabel.setMaximumSize(new Dimension(200, 20));
		aLabel.setMinimumSize(new Dimension(200, 20));
		aLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(aLabel);

		add(new JLabel("   "));
		aLabel = new JLabel("Calculation task:");
		aLabel.setMaximumSize(new Dimension(200, 20));
		aLabel.setMinimumSize(new Dimension(200, 20));
		aLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(aLabel);

		JComboBox<CalculatorInterruptable> comboBox = new JComboBox<>(calculatorComboBoxModel);
		comboBox.setAlignmentX(CENTER_ALIGNMENT);
		comboBox.setEditable(false);
		comboBox.setMaximumSize(new Dimension(200, 20));
		comboBox.setMinimumSize(new Dimension(200, 20));
		add(comboBox);
		add(new JLabel("   "));

		add(new JPanel() {
			{
				this.setAlignmentX(CENTER_ALIGNMENT);
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(new JLabel("Threshold:   10^"));

				thresholdTextField = new JFormattedTextField(-6);
				thresholdTextField.addPropertyChangeListener("value", thresholdTextFieldListener);
				thresholdTextField.setMaximumSize(new Dimension(100, 20));
				thresholdTextField.setMinimumSize(new Dimension(100, 20));
				add(thresholdTextField);
			}
		});

	}

}
