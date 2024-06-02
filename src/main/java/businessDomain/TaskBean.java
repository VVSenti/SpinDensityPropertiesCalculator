package businessDomain;

public interface TaskBean {

	public void setRunning();

	public void setDone();

	public void setInterrupted();

	public void setHasError();

	public boolean canBeRun();

	public void setResult(double value);

	public String getFilePath();

	public boolean canBeRemovedWhileWorking();
	
	public boolean isDone();

	public Double getResult();

	public void setResult(Double result);

	public String getFileName();
	
	public String getStatus();
}
