/**
 * 
 */
package me.charlesy.masking.gpload;

import java.util.concurrent.ConcurrentLinkedQueue;

import me.charlesy.masking.MaskMethod;
import me.charlesy.masking.MaskThread;

/**
 * @author Charles Young
 *
 */
public class GploadMaskThread extends MaskThread {

	private ConcurrentLinkedQueue<Object []> rowBuff;

	public void setRowBuff(ConcurrentLinkedQueue<Object []> rowBuff) {
		this.rowBuff = rowBuff;
	}

	public void run() {

		while (isStop().get()) {

			Object[] row = rowBuff.poll();
			for (MaskMethod mkmd : getMaskMethods()) {
				mkmd.marking(new Object());
			}

		}

	}

}
