/**
 * 
 */
package me.charlesy.masking.load;

/**
 * @author Charles Young
 *
 */
public class GploadProfile {

	private String controlFile;

	private String gploadPath;

	private String logFile;

	private String inputFile;

	public void setControlFile(String controlFile) {
		this.controlFile = controlFile;
	}

	public String getControlFile() {
		return controlFile;
	}

	public void setGploadPath(String gploadPath) {
		this.gploadPath = gploadPath;
	}

	public String getGploadPath() {
		return gploadPath;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public String getLogFile() {
		return logFile;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

}
