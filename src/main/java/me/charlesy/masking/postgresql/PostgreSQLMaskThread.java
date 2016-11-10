/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLMaskThread implements Runnable {

	private PipedInputStream pipedInputStream;

	public PostgreSQLMaskThread(final PipedInputStream pipedInputStream) {
		this.pipedInputStream = pipedInputStream;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pipedInputStream));
		try {
			String line = bufferedReader.readLine();
			do {
				System.out.println(line);
				if (line == null) {
					Thread.sleep(500);
				}
			} while ((line = bufferedReader.readLine()) != null);
//			} while (pipedInputStream.available() != 0
//					|| (line = bufferedReader.readLine()) != null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
