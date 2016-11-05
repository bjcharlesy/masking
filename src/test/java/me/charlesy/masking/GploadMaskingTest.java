/**
 * 
 */
package me.charlesy.masking;

import org.junit.Before;
import org.junit.Test;

import me.charlesy.masking.gpload.GploadMasking;
import me.charlesy.masking.load.GploadProfile;

/**
 * @author Charles Young
 *
 */
public class GploadMaskingTest {

	private GploadMasking gploadMasking;

	@Before
	public void setup() {
		gploadMasking = new GploadMasking();
		GploadProfile profile = new GploadProfile();
		profile.setControlFile("~/gpload.yml");
		profile.setGploadPath("~/bin/gpload");
		profile.setLogFile("~/load.log");
		gploadMasking.setGpProfile(profile);
	}

	@Test
	public void testBeforeMask() {
		gploadMasking.beforeMask();
	}

}
