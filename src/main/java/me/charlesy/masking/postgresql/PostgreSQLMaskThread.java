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
		closeReader();
		closeWriter();
	}

	public void closeReader() {
		try {
			if (bufferedReader.read() != -1)
				throw new RuntimeException("not read finished.");
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeWriter() {
		try {
			bufferedWriter.flush();
			bufferedWriter.close();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			System.out.println(sdf.format(new Date()) + ": copy into gp.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String line = null;
			// 当输入已经读完(即: bufferReader.readLine = null), 并且写入已经结束(即: isFinish为True)
			while ((line = bufferedReader.readLine()) != null || !isFinish.get()) {
				if (line == null) {
					// 说明输入还没准备好，短暂等待
					Thread.sleep(2);
					continue;
				}
				// 模拟处理时间
				if (false)
					Thread.sleep(60 * 1000);
				System.out.println(line);
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			System.out.println(sdf.format(new Date()) + ": "
					+ Thread.currentThread().getName() + " end.");
		}
	}

}
