package org.smellycat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.smellycat.architecture.Architecture;
import org.smellycat.architecture.ArchitectureFactory;
import org.smellycat.version.output.CSVOutput;
import org.smellycat.version.output.HTMLOutput;
import org.smellycat.version.output.Output;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
//import org.apache.log4j.Logger;

public class SmellyCat {
	
	//private static Logger log = Logger.getLogger(SmellyCat.class);

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Options opts = new Options();
		opts.addOption("arch", true, "Architecture ('springmvc', 'android')");
		opts.addOption("o", "output", true, "Path to the output. Should be a dir ending with /");
		opts.addOption("otype", true, "Type of the output: 'csv', 'html'");
		opts.addOption("p", "project", true, "Path to the project");
		// Adding option for version comparison
		opts.addOption("v", false, " If version control is required");
		opts.addOption("g", "commitHash",true," If git initialized");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(opts, args);
		////////
		
		
		boolean missingArgument = 
				!cmd.hasOption("arch") || 
				!cmd.hasOption("output") || 
				!cmd.hasOption("otype") || 
				!cmd.hasOption("project"); 

		if(missingArgument) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("smellycat", opts);
			System.exit(-1);
		}
		
		boolean invalidParams = 
				(!cmd.getOptionValue("arch").equals("springmvc") && !cmd.getOptionValue("arch").equals("android"));

		if(invalidParams) {
			System.out.println("invalid parameter");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("smellycat", opts);
			System.exit(-1);
		}
		
		ArrayList<String> projectPaths = new ArrayList<String>(0);
		
		// ooption g
		if(cmd.hasOption("g")) {
			System.out.println("Received g option!");
			String oldCommit = cmd.getOptionValue("commitHash");
			System.out.println("Checking out old version");
			
			///// TO run git in another process
			/*
				String[] command =
		    {
		    		"cmd",
		    };
		    Process p;
			try {
				p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			    PrintWriter stdin = new PrintWriter(p.getOutputStream());
			    stdin.println("ls");
			    // write any other commands you want here
			    stdin.println("cd" + cmd.getOptionValue("project"));
			    stdin.println("git log");
			    stdin.println("mkdir thingworks");
			    stdin.close();
			    int returnCode = p.waitFor();
			    System.out.println("Return code = " + returnCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			*/
		    //////////// TRY 2
			String makeDir = "mkdir versions";
			String makeversion = "mkdir versions/version1";
			String makeversion2 = "mkdir versions/version2";
			String cur_ver = "cp -r " + cmd.getOptionValue("project") + " versions/version1";
			String move_pointer = "cd " + cmd.getOptionValue("project");
		    String[] whatToRun = {"/bin/sh",
		    		"-c", "git archive " + cmd.getOptionValue("commitHash") +" | tar -xC ../versions/version2"};
		    //String[] ls = {"ls"};
		    try
		    {
		      Runtime rt = Runtime.getRuntime();
		      Process proc = rt.exec(makeDir);
		      int exitVal = proc.waitFor();
		      Process proc1 = rt.exec(makeversion);
		      int exitVal1 = proc1.waitFor();
		      Process proc2 = rt.exec(makeversion2);
		      int exitVal2 = proc2.waitFor();
		      Process proc3 = rt.exec(cur_ver);
		      int exitVal3 = proc3.waitFor();
		      System.out.println(whatToRun);
		      //Process proc4 = rt.exec(move_pointer);
		      //int exitVal4 = proc4.waitFor();
		      //System.exit(0);
		      //Process procl = rt.exec("ls",new String[] {},new File(cmd.getOptionValue("project")));
		      //int exitVall = proc4.waitFor();
		      Process proc5 = rt.exec(whatToRun,new String[] {},new File(cmd.getOptionValue("project")));
		      int exitVal5 = proc.waitFor();
		      
		      BufferedReader stdInput = new BufferedReader(new 
		    		     InputStreamReader(proc5.getInputStream()));

		    		BufferedReader stdError = new BufferedReader(new 
		    		     InputStreamReader(proc5.getErrorStream()));

		    		// read the output from the command
		    		System.out.println("Here is the standard output of the command:\n");
		    		String s = null;
		    		while ((s = stdInput.readLine()) != null) {
		    		    System.out.println(s);
		    		}

		    		// read any errors from the attempted command
		    		System.out.println("Here is the standard error of the command (if any):\n");
		    		while ((s = stdError.readLine()) != null) {
		    		    System.out.println(s);
		    		}
		      
		      System.out.println("Process exitValue:" + exitVal5);
		      projectPaths.add("versions/version1");
		      projectPaths.add("versions/version2");
		    } catch (Throwable t)
		      {
		        t.printStackTrace();
		      }
		   }
		
		// Temporary debuggin message
		else if (cmd.hasOption("v")) {
			System.out.println("Received version comparison option!");
			File versions_folder = new File(cmd.getOptionValue("project"));
			if(versions_folder.exists() && versions_folder.isDirectory()) {
				File[] directories = versions_folder.listFiles(File::isDirectory);
				for(File directory: directories) {
					System.out.println(directory.toString());
					projectPaths.add(directory.toString());
				}
			}
			else {
				System.out.println("Incorrect folder path!");
				System.exit(-1);
			}
			
		}
		else {
			projectPaths.add(cmd.getOptionValue("project"));
		}
		
		Architecture arch = new ArchitectureFactory().build(cmd.getOptionValue("arch"));
		String outputPath = cmd.getOptionValue("output");
		
		Output output;
		if(cmd.getOptionValue("otype").equals("html")) {
			output = new HTMLOutput(outputPath);
		} else {
			output = new CSVOutput(outputPath);
		}
		
		long startTime = System.currentTimeMillis();
		//log.info("# ----------------------------------------- #");
		//log.info("#          Smelly Cat - Spring MVC          #");
		//log.info("#  www.github.com/mauricioaniche/smellycat  #");
		//log.info("# ----------------------------------------- #");
		
		// Running analysis on all files found
		
		
		new RunAllAnalysis(arch, projectPaths, output).run();
		// new RunAllAnalysis(arch, projectPath, output).run();
		
		long endTime = System.currentTimeMillis();
		long time = (endTime - startTime) / 1000;
		//log.info(String.format("That's it! It only took %d seconds", time));
		
		try
	    {
	      Runtime rt = Runtime.getRuntime();
	      Process procdel = rt.exec("rm -r versions");
	      int exitValdel = procdel.waitFor();
	    }
		catch (Throwable t){
	      {
	        t.printStackTrace();
	      }
	   }
	}
	
	
}

class SyncPipe implements Runnable
{
public SyncPipe(InputStream istrm, OutputStream ostrm) {
      istrm_ = istrm;
      ostrm_ = ostrm;
  }
  public void run() {
      try
      {
          final byte[] buffer = new byte[1024];
          for (int length = 0; (length = istrm_.read(buffer)) != -1; )
          {
              ostrm_.write(buffer, 0, length);
          }
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
  }
  private final OutputStream ostrm_;
  private final InputStream istrm_;
}
