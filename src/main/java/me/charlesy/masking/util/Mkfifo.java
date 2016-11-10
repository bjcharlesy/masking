/**
 * 
 */
package me.charlesy.masking.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Charles Young
 *
 */
public class Mkfifo {
	private String pipeName;

	private String createCommandLine() {
		return "notepad " + pipeName;
	}

	public Mkfifo() {
		this("defaultNamePipe");
	}

	public Mkfifo(String pipeName) {
		this.pipeName = pipeName;
	}

	public void execute() {
		Runtime rt = Runtime.getRuntime();
		String cmd = createCommandLine();
		// TODO: "Executing command: " + cmd
		try {
			Process mkfifoProcess = rt.exec(cmd);

			StreamLogger errorLogger = new StreamLogger( mkfifoProcess.getErrorStream(), "ERROR {0}" );
			StreamLogger outputLogger = new StreamLogger( mkfifoProcess.getInputStream(), "OUTPUT {0}" );

			mkfifoProcess.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("interrupted.");
		}
	}

	public static void main(String[] args) {
		new Mkfifo().execute();
	}

}
