package models;

import java.io.File;

public class SpinDensityTaskBean implements TaskBean {
	static final String FILE_EXTENTION = ".txt";
	static final String NOT_STARTED = "";
	static final String RUNNING = "Running";
	static final String INTERRUPTED = "Interrupted";
	static final String ERROR = "Error";
	static final String DONE = "Done";

	private final String filePath;
	private final String fileName;
	private String status;
	private Double result;

	public SpinDensityTaskBean(File file) {
		filePath = file.getPath();
		fileName = file.getName();
		status = NOT_STARTED;
	}

	public void setRunning() {
		status = RUNNING;
	}

	public void setDone() {
		status = DONE;
	}

	public void setInterrupted() {
		status = INTERRUPTED;
	}

	public void setHasError() {
		status = ERROR;
	}

	public boolean canBeRun() {
		return (status == NOT_STARTED) | (status == INTERRUPTED);
	}

	public void setResult(double value) {
		result = value;
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean canBeRemovedWhileWorking() {
		return status == NOT_STARTED;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public boolean isDone() {
		return status.equals(DONE);
	}
}
