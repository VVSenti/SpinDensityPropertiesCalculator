import javax.swing.SwingUtilities;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
		
	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> new Main());
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		

	}
}
