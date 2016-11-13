/**
 * 
 */
package me.charlesy.masking.postgresql;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLProfile {

	public static final String LOAD_INSERT = "insert";

	public static final String LOAD_TRUNCATE = "truncate";

	private int threadNumber;

	private String loadTable;

	private String targetTable;

	private String delimiter;

	private String loadAction;

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public String getLoadTable() {
		return loadTable;
	}

	public void setLoadTable(String loadTable) {
		this.loadTable = loadTable;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getLoadAction() {
		return loadAction;
	}

	public void setLoadAction(String loadAction) {
		this.loadAction = loadAction;
	}

	public PostgreSQLProfile() {
		this.delimiter = "\t";
		this.loadAction = LOAD_INSERT;
		this.threadNumber = 1;
	}

}
