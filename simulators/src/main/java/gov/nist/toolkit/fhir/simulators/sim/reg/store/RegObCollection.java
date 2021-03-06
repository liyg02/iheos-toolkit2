package gov.nist.toolkit.fhir.simulators.sim.reg.store;


import java.util.List;

abstract public class RegObCollection {

	abstract public String statsToString();
	abstract public boolean hasObject(String id);
	abstract public Ro getRo(String id);
	abstract public List<?> getAllRo();
	abstract public Ro getRoByUid(String uid);
	abstract public List<String> getIds();
	abstract public void delete(String id);  	// caller handles synchronization
	
}
