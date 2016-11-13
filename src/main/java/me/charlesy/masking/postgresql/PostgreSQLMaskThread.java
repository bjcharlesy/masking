/**
 * 
 */
package me.charlesy.masking.postgresql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charles Young
 *
 */
public class PostgreSQLMaskThread implements Runnable {

	private AtomicBoolean isFinish = new AtomicBoolean(false);

	private AtomicBoolean isClose = new AtomicBoolean(false);

	private BufferedReader bufferedReader;

	private BufferedWriter bufferedWriter;

	public void finish() {
		isFinish.set(true);
	}

	public PostgreSQLMaskThread(final PipedInputStream pipedInputStream, PipedOutputStream pipedOutputStream) {
		bufferedReader = new BufferedReader(new InputStreamReader(pipedInputStream));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(pipedOutputStream));
	}

	public void close() {
		isClose.set(true);
	}

	public void closeWriter() {
		try {
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String line = null;
			// IMPORTANT: 易错, 除非发现bug, 否则不建议轻易更改这段控制代码。
			// 1. 当outstream写入完成, 即isFinish为True时。
			// 2. reader已经读完，readline不为null。isClose检查bufferedReader是否已经关闭。
			// 满足上面两个条件才能关闭
			while (!isFinish.get() ||
					(!isClose.get() && (line = bufferedReader.readLine()) != null)) {
				if (line == null) {
					continue;
				}
				// 模拟处理时间
				if (false)
					Thread.sleep(60 * 1000);
				System.out.println(line);
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				close();
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
