/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLDatabase {

	private String hostname;

	private String port;

	private String databaseName;

	private String user;

	private String password;

	public PostgreSQLDatabase() {
		this.hostname = "127.0.0.1";
		this.port = "5432";
		this.databaseName = "gpadmin";
		this.user = "gpadmin";
		this.password = "";
	}

	public PostgreSQLDatabase(String hostname, String port, String databaseName, String user, String password) {
		this.hostname = hostname;
		this.port = port;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}

	public String getURL( String hostname, String port, String databaseName ) {
		return "jdbc:postgresql://" + hostname + ":" + port + "/" + databaseName;
    }

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("postgresql jdbc jar not Found.", e);
		}
	}

	public Connection getConnection() {
		Connection connection;
		try {
			connection = DriverManager.getConnection(getURL(hostname, port, databaseName), user, password);
		} catch (SQLException e) {
			throw new RuntimeException("connect gostgresl failed.", e);
		}
		return connection;
	}

	public void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException("close connection failed.", e);
		}
	}

}
