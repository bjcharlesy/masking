/**
 * 
 */
package me.charlesy.masking;

import java.util.Map;

/**
 * @author Charles Young
 *
 */
public interface Maskable {

	public Map<String, Object> getMaskContext();

	public MaskThread getMaskThread();

	public void waitFor();

}
