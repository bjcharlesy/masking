/**
 * 
 */
package me.charlesy.masking.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;

import me.charlesy.masking.util.Utils;

/**
 * @author Charles Young
 *
 */
public class Gpload {

	private GploadProfile profile;

	public GploadProfile getGpProfile() {
		return this.profile;
	}

	public void setGpProfile(GploadProfile profile) {
		this.profile = profile;
	}

	private void createControlFile() {
		String filename = profile.getControlFile();
		if (Utils.isEmpty(filename)) {
			throw new RuntimeException("filename is empty");
		}

		File controlFile = new File( filename );
	    FileWriter fw = null;

	    try {
	    	controlFile.createNewFile();
	    	fw = new FileWriter(controlFile);
//	    	fw.write(getControlFileContents(profile));
	    } catch (IOException e) {
	    	throw new RuntimeException("IO errors");
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch ( Exception ignored ) {
				// Ignore error
			}
		}
	}

	private String createCommandLine() {
		StringBuffer sbCommandLine = new StringBuffer(300);
		sbCommandLine.append(profile.getGploadPath());

		// get the path to the control file
	    sbCommandLine.append( " -f " );
	    sbCommandLine.append(profile.getControlFile());

	    // get the path to the log file, if specified
	    String logfile = profile.getLogFile();
	    if (Utils.isEmpty(logfile)) {
	    	sbCommandLine.append( " -l " );
	    	sbCommandLine.append(logfile);
	    }

	    return sbCommandLine.toString();
	}

	/**
	 * 执行gpload命令
	 */
	public void execute() {

		try {
			String commandLine = null;
			Runtime rt = Runtime.getRuntime();
			int gpLoadExitVal = 0;

			commandLine = createCommandLine();
			// TODO: log.info( "Executing: " + commandLine )
			Process gploadProcess = rt.exec(commandLine);

			// any error message?
			StreamLogger errorLogger = new StreamLogger( gploadProcess.getErrorStream(), "ERROR" );

			// any output?
			StreamLogger outputLogger = new StreamLogger( gploadProcess.getInputStream(), "OUTPUT" );

			// kick them off
			errorLogger.start();
			outputLogger.start();

			gpLoadExitVal = gploadProcess.waitFor();

			if (gpLoadExitVal != -0) {
				throw new RuntimeException("gpload exit code: " + gpLoadExitVal);
			}
			
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}

//		createControlFile();
	}

	/**
	 * load数据从文件到内存
	 */
	@SuppressWarnings("static-access")
	public void load(Queue<Object []> rowBuff) {
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

	private final class StreamLogger extends Thread {
		private InputStream input;
		private String type;

	    StreamLogger( InputStream is, String type ) {
	      this.input = is;
	      this.type = type + ">";
	    }

	    public void run() {
	      try {
	        final BufferedReader br = new BufferedReader(new InputStreamReader(input));
	        String line;
	        while ((line = br.readLine()) != null) {
	          // Only perform the concatenation if at basic level. Otherwise,
	          // this just reads from the stream.
//	          if ( log.isBasic() ) {
//	            logBasic( type + line );
//	          }
	        }
	      } catch ( IOException ioe ) {
	        ioe.printStackTrace();
	      }

	    }

	  }
}
