/**
 * 
 */
package me.charlesy.masking;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.charlesy.masking.gpload.GploadStore;
import me.charlesy.masking.load.Gpload;

/**
 * @author Charles Young
 *
 */
public class Masking {

	private int thread_number;

	private Maskable masking;

	private MaskThread markingThread;

	/**
	 * 执行 
	 */
	public void excute() {
		beforeMarking();
//		Map<String, Object> maskContext = masking.getMaskContext();
		markingThread = masking.getMaskThread();
		startMaskingThreads();
		masking.waitFor();
		afterMarking();
	}

	private void beforeMarking() {
		if (masking instanceof BeforeMasking) {
			BeforeMasking bm = (BeforeMasking) masking;
			bm.beforeMask();
		}
	}

	private void afterMarking() {
	}

	public void startMaskingThreads() {
		for (int i=0; i<thread_number; i++) {
			Thread thread = new Thread(markingThread);
			thread.setName("Marking Thread - " + i);
			thread.start();
		}
	}

}
