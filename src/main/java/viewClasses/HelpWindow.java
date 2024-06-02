package viewClasses;

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
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
//		JTextArea textArea = new JTextArea();
		JTextPane textArea = new JTextPane();
		add(textArea);
		String helpText;
		String path =  "C:\\Users\\user\\eclipse-workspace\\CoulombIntegralCalculator\\src\\main\\java\\businessDomain\\Help.txt";
		
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
