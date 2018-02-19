package org.smellycat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.smellycat.architecture.Architecture;
import org.smellycat.architecture.ArchitectureFactory;
import org.smellycat.version.output.CSVOutput;
import org.smellycat.version.output.HTMLOutput;
import org.smellycat.version.output.Output;

public class SmellyCat {
	
	private static Logger log = Logger.getLogger(SmellyCat.class);

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Options opts = new Options();
		opts.addOption("arch", true, "Architecture ('springmvc', 'android')");
		opts.addOption("o", "output", true, "Path to the output. Should be a dir ending with /");
		opts.addOption("otype", true, "Type of the output: 'csv', 'html'");
		opts.addOption("p", "project", true, "Path to the project");
		// Adding option for version comparison
		opts.addOption("v", false, " If version control is required");
		
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
		
		// Temporary debuggin message
		if (cmd.hasOption("v")) {
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
		log.info("# ----------------------------------------- #");
		log.info("#          Smelly Cat - Spring MVC          #");
		log.info("#  www.github.com/mauricioaniche/smellycat  #");
		log.info("# ----------------------------------------- #");
		
		// Running analysis on all files found
		
		
		new RunAllAnalysis(arch, projectPaths, output).run();
		// new RunAllAnalysis(arch, projectPath, output).run();
		
		long endTime = System.currentTimeMillis();
		long time = (endTime - startTime) / 1000;
		log.info(String.format("That's it! It only took %d seconds", time));


	}
	
}
