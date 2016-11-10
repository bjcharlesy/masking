/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLMasking {

	private PostgreSQLDatabase psqldb;

	private PostgreSQLProfile profile;

	public PostgreSQLMasking() {

	}

	public PostgreSQLMasking(PostgreSQLDatabase psqldb) {
		this.psqldb = psqldb;
	}

	public PostgreSQLProfile getGpCopyProfile() {
		return this.profile;
	}

	public void setGpCopyProfile(PostgreSQLProfile profile) {
		this.profile = profile;
	}

	public void setPostgreSQLDatabase(PostgreSQLDatabase psqldb) {
		this.psqldb = psqldb;
	}

	public void mask() {
		PipedOutputStream pipedOutputStream = new PipedOutputStream();
		PipedInputStream pipedInputStream = null;
		try {
			pipedInputStream = new PipedInputStream(pipedOutputStream);
		} catch (IOException e) {
			// Nothing will happen.
		};

		Thread copyOutThread = new Thread(new CopyOutPg(pipedOutputStream));
		copyOutThread.start();

		Thread maskThread = new Thread(new PostgreSQLMaskThread(pipedInputStream));
		maskThread.start();

		try {
			copyOutThread.join();
			maskThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startCopyInGp();
	}

	private void startCopyInGp() {
		
	}

	private class CopyOutPg implements Runnable {

		final PipedOutputStream pipedOutputStream;

		public CopyOutPg(PipedOutputStream pipedOutputStream) {
			this.pipedOutputStream = pipedOutputStream;
		}

		private String getCopyCommand() {
			StringBuilder contents = new StringBuilder(500);
			contents.append( "COPY " );
			contents.append("(SELECT * FROM ext_user_test) ");
			contents.append("TO STDOUT WITH DELIMITER '");
			contents.append("\t");
			contents.append("'");
			return contents.toString();
		}

		@Override
		public void run() {
			CopyManager copyManager;
			BaseConnection connection = null;
			try {
				connection =  (BaseConnection) psqldb.getConnection();
				copyManager = new CopyManager(connection);
				copyManager.copyOut(getCopyCommand(), pipedOutputStream);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					pipedOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				psqldb.closeConnection(connection);
			}
		}
	}

	public static void main(String[] args) {
		PostgreSQLDatabase psqldb = new PostgreSQLDatabase(
				"vm.mini", "5432", "gptest", "gpadmin", "");
		new PostgreSQLMasking(psqldb).mask();
	}

}
