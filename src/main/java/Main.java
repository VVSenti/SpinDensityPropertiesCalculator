import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(SpringConfig.class);
	}
}
