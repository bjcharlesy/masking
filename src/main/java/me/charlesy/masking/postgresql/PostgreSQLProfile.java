/**
 * 
 */
package me.charlesy.masking.postgresql;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLProfile {

	private String tableName;

	private String delimiter;

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public PostgreSQLProfile() {
		this.delimiter = "\t";
	}

}
