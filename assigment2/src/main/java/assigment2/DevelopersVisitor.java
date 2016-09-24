package assigment2;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

public class DevelopersVisitor implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		// TODO Auto-generated method stub
		   writer.write(
		            commit.getHash(),
		            commit.getCommitter().getName()
		        );

	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		  return "developers";
	}

}
