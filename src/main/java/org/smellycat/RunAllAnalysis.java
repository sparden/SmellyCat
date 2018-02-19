package org.smellycat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.smellycat.analysis.ck.CKAnalysis;
import org.smellycat.analysis.smells.SmellAnalysis;
import org.smellycat.architecture.Architecture;
import org.smellycat.domain.Repository;
import org.smellycat.version.output.Output;

import br.com.aniche.ck.CKNumber;

public class RunAllAnalysis {
	
	private Architecture arch;
	//private String projectPath;
	ArrayList<String> projectPaths;
	private Output output;

	public RunAllAnalysis(Architecture arch, ArrayList<String> projectPaths, Output output) {
		this.arch = arch;
		this.projectPaths = projectPaths;
		this.output = output;
		
	}

	public void run() throws FileNotFoundException {
		ArrayList<Repository> all_smells = new ArrayList<Repository>(0);
		ArrayList<Map<String, List<CKNumber>>> all_analysis = new ArrayList<Map<String, List<CKNumber>>>(0);
		
		for(String project:projectPaths) {
			System.out.println("\n RUNNING ON PROJECT VERSION: "+project);
			CKAnalysis ck = new CKAnalysis(arch,project);
			Map<String, List<CKNumber>> ckResults = ck.run();
			
			SmellAnalysis smells = new SmellAnalysis(arch, project);
			Repository smellResults = smells.run();
			System.out.println("------"+ckResults.size()+"----------");
			all_smells.add(smellResults);
			all_analysis.add(ckResults);
		}
		output.printOutput(arch, all_analysis, all_smells);
		
	}


}
