/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
				// 模拟处理时间
				if (false)
					Thread.sleep(60 * 1000);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				System.out.println(sdf.format(new Date()) + ": copy into gp.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
