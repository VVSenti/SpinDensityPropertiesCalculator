package ru.sentyurin.SpinDensityPropertiesCalculator.views;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class SettingsWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public SettingsWindow() {
		super("Settings");
		setSize(600, 300);
		setLocation(500, 240);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setVisible(true);
	}
}
