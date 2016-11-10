/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public PostgreSQLMasking(PostgreSQLProfile profile) {
		this.profile = profile;
	}

	public PostgreSQLMasking(PostgreSQLProfile profile, PostgreSQLDatabase psqldb) {
		this.psqldb = psqldb;
		this.profile = profile;
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

		CopyOutPg copyOutPg = new CopyOutPg(pipedOutputStream);
		copyOutPg.setDelimiter(profile.getDelimiter());
		copyOutPg.setTableName(profile.getTableName());
		Thread copyOutThread = new Thread(copyOutPg);		
		copyOutThread.start();

		int thread_number = profile.getThreadNumber();
		PostgreSQLMaskThread psqlMaskThread = new PostgreSQLMaskThread(pipedInputStream);
		Thread[] maskThreads = new Thread[thread_number];
		for (int i=0; i<thread_number; i++) {
			maskThreads[i] = new Thread(psqlMaskThread);
			maskThreads[i].start();
		}

		try {
			copyOutThread.join();
			psqlMaskThread.finish();
			for (Thread t : maskThreads) {
				t.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startCopyInGp();
	}

	private void startCopyInGp() {
		
	}

	private class CopyOutPg implements Runnable {

		private final PipedOutputStream pipedOutputStream;

		private String tableName;

		private String delimiter;

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public void setDelimiter(String delimiter) {
			this.delimiter = delimiter;
		}

		public CopyOutPg(PipedOutputStream pipedOutputStream) {
			this.pipedOutputStream = pipedOutputStream;
		}

		private String getCopyCommand() {
			StringBuilder contents = new StringBuilder(500);
			contents.append( "COPY " );
			contents.append("(SELECT * FROM ");
			contents.append(tableName);
			contents.append(") ");
			contents.append("TO STDOUT WITH DELIMITER '");
			contents.append(delimiter);
			contents.append("'");
			return contents.toString();
		}

		@Override
		public void run() {
			CopyManager copyManager;
			BaseConnection connection = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			System.out.println(sdf.format(new Date()) + ": begin copy.");
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
					System.out.println(sdf.format(new Date()) + ": copy out gp.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				psqldb.closeConnection(connection);
			}
		}
	}

	public static void main(String[] args) {
		PostgreSQLProfile profile = new PostgreSQLProfile();
		profile.setTableName("ext_user_test");
		profile.setThreadNumber(4);
		PostgreSQLDatabase psqldb = new PostgreSQLDatabase(
				"vm.mini", "5432", "gptest", "gpadmin", "");
		PostgreSQLMasking psqlMasking = new PostgreSQLMasking(profile, psqldb);
		psqlMasking.mask();
	}

}
