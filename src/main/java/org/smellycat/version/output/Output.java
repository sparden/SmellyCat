package org.smellycat.version.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.smellycat.architecture.Architecture;
import org.smellycat.domain.Repository;

import br.com.aniche.ck.CKNumber;

public interface Output {
	void printOutput(Architecture arch, ArrayList<Map<String, List<CKNumber>>> ckResults, ArrayList<Repository> smellResults);
}