/**
 * 
 */
package me.charlesy.masking.gpload;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.charlesy.masking.BeforeMasking;
import me.charlesy.masking.MaskThread;
import me.charlesy.masking.Maskable;
import me.charlesy.masking.load.GploadProfile;

/**
 * @author Charles Young
 *
 */
public class GploadMasking implements Maskable, BeforeMasking {

	private Map<String, Object> maskContext = new HashMap<String, Object>();
	private ConcurrentLinkedQueue<Object []> rowBuff =
			new ConcurrentLinkedQueue<Object[]>();

	private Gpload gpload = new Gpload();

	private GploadProfile profile;

	public GploadMasking() {
		maskContext.put("rowBuff", rowBuff);
	}

	public GploadProfile getGpProfile() {
		return this.profile;
	}

	public void setGpProfile(GploadProfile profile) {
		this.profile = profile;
		gpload.setGpProfile(profile);
	}

	public void beforeMask() {
		gpload.execute();
	}

	public Map<String, Object> getMaskContext() {
		return maskContext;		
	}

	public void waitFor() {
		load();
	}

	/**
	 * load数据从文件到内存
	 */
	@SuppressWarnings("static-access")
	public void load() {
		// 按行读取一块数据，写入队列
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(profile.getInputFile()));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				// 如果buff数据大于1W条则暂停读取文件
				if (i > 10 * 1000) {
					i = 0;
					try {
						Thread.currentThread().sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rowBuff.add(splitLine(line));
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("miss input file", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// do nothing.
				}
		}
	}

	private static Object[] splitLine(String line) {
		Object[] o = line.split("\t");
		return o;
	}

	public MaskThread getMaskThread() {
		GploadMaskThread mt = new GploadMaskThread();
		mt.setRowBuff(rowBuff);
		return mt;
	}

}
