package ru.sentyurin.SpinDensityPropertiesCalculator.views;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1380942234098320694L;

	@Autowired
	public MainWindow(ButtonPanel buttonPanel, FilesTable filesTable, SettingsPanel settingsPanel) {
		this.setSize(700, 378);
		this.setLocation(500, 240);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		add(buttonPanel);
		add(new JScrollPane(filesTable));
		add(settingsPanel);
		setVisible(true);
	}

}
