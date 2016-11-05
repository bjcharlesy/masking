/**
 * 
 */
package me.charlesy.masking;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charles Young
 *
 */
public abstract class MaskThread implements Runnable {

	private AtomicBoolean isStop = new AtomicBoolean(false);

	private List<String> maskMethodNames;

	private List<MaskMethod> maskMethods;

	public void setStop(boolean isStop) {
		this.isStop.set(isStop);
	}

	public AtomicBoolean isStop() {
		return this.isStop;
	}

	public void setMarkingMethodNames(List<String> maskMethodNames) {
		this.maskMethodNames = maskMethodNames;
	}

	protected List<MaskMethod> getMaskMethods() {
		return this.maskMethods;
	}

}
