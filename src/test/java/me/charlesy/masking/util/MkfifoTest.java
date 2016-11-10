/**
 * 
 */
package me.charlesy.masking.util;

import org.junit.Test;

/**
 * @author Charles Young
 *
 */
public class MkfifoTest {

	private Mkfifo mkfifo = new Mkfifo("pipe");

	@Test
	public void testExecute() {
		mkfifo.execute();
	}

}
