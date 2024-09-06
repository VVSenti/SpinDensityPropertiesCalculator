package ru.sentyurin.SpinDensityPropertiesCalculator.views;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JTextPane;

public class HelpWindow extends JFrame {	
	private static final long serialVersionUID = 1943961600611433242L;

	public HelpWindow() {
		super("Settings");
		this.setSize(600, 300);
		this.setLocation(500, 240);
		JTextPane textArea = new JTextPane();
		add(textArea);
		String helpText;
		String path =  "src/main/resources/help.txt";
		
		try {
			helpText = new String(Files.readAllBytes(Paths.get(path)), 
									StandardCharsets.UTF_8);
			textArea.setText(helpText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		textArea.setEditable(false);
		setVisible(true);
	}

}
