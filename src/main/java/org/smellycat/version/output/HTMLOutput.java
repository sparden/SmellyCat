package org.smellycat.version.output;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.smellycat.analysis.ck.CKAnalysis;
import org.smellycat.architecture.Architecture;
import org.smellycat.domain.Repository;

import com.google.gson.Gson;

import br.com.aniche.ck.CKNumber;

public class HTMLOutput implements Output {
	
	private PrintStream output;

	public HTMLOutput(String output) throws FileNotFoundException {
		this.output = new PrintStream(output+"result.html");
	}

	public void printOutput(Architecture arch, ArrayList<Map<String, List<CKNumber>>> ckResults, ArrayList<Repository> smellResults) {
		//////////////////
		// Assuming 2 results, changing script
		// String ckResultsInJson = new Gson().toJson(ckResults);
		String ckResultsInJson = new String();
		String smellResultsInJson = new String();
		
		for(int i=0;i<ckResults.size();i++) {
			System.out.println("======="+ckResults.get(i).size()+"=======");
			ckResultsInJson = ckResultsInJson + new Gson().toJson(ckResults.get(i));
			if(i!=(ckResults.size()-1)) {
				ckResultsInJson = ckResultsInJson + ",";
			}
		}
		
		for(int i=0;i<smellResults.size();i++) {
			smellResultsInJson = smellResultsInJson + new Gson().toJson(smellResults.get(i).all());
			if(i!=(smellResults.size()-1)) {
				smellResultsInJson = smellResultsInJson + ",";
			}
		}
		
		ckResultsInJson = "[" + ckResultsInJson + "]";
		smellResultsInJson = "[" + smellResultsInJson + "]";
		
		try {
			String thresholds = readResource("/output/" + arch.thresholdsFile());
			//////////////////////////
			
			// Changing template file from results.html to ver-template.html
			String html = readResource("/output/ver-template.html");
			// String html = readResource("/output/result.html");
			
			/////////////////////////
			html = html
				.replace("##ck##", ckResultsInJson)
				.replace("##archThresholds##", thresholds)
				.replace("##smells##", smellResultsInJson)
				.replace("##date##", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
			output.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
		
	}

	private String readResource(String name) throws IOException {
		InputStream is = CKAnalysis.class.getResource(name).openStream();
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		String content = writer.toString();
		is.close();
		
		return content;
	}
}