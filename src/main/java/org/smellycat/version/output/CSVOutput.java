package org.smellycat.version.output;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.smellycat.analysis.smells.SmellDescription;
import org.smellycat.architecture.Architecture;
import org.smellycat.domain.Repository;
import org.smellycat.domain.SmellyClass;

import br.com.aniche.ck.CKNumber;

public class CSVOutput implements Output{
	
	private PrintStream output;

	public CSVOutput(String outputPath) throws FileNotFoundException {
		this.output = new PrintStream(outputPath + "smells.csv"); 
	}

	
	public void printOutput(Architecture arch, Map<String, List<CKNumber>> ckResults, Repository smellResults) {
		
		output.println("project,file,name,role,smell,note");
		for(SmellyClass clazz : smellResults.all()) {
			for(SmellDescription description : clazz.getSmells()) {
				output.println(
					clazz.getFile() + "," +
					clazz.getName() + "," +
					clazz.getRole().name() + "," +
					description.getName() + "," +
					description.getDescription()
				);
			}
		}		
		
		output.close();
	}
	
	@Override
	public void printOutput(Architecture arch, ArrayList<Map<String, List<CKNumber>>> ckResults, ArrayList<Repository> smellResults) {
		
		System.out.println("Output for version comparison for CSV format yet to be implemented :(");
		System.exit(-1);
		/*
		output.println("project,file,name,role,smell,note");
		for(SmellyClass clazz : smellResults.all()) {
			for(SmellDescription description : clazz.getSmells()) {
				output.println(
					clazz.getFile() + "," +
					clazz.getName() + "," +
					clazz.getRole().name() + "," +
					description.getName() + "," +
					description.getDescription()
				);
			}
		}		
		
		output.close();
		*/
	}

}
