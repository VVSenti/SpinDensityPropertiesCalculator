package models;

import java.io.File;


public class SpinDensityTaskBeanFactory {
	public SpinDensityTaskBean getSpinDensityTaskBean(File file) {
		return new SpinDensityTaskBean(file);
	}
}
