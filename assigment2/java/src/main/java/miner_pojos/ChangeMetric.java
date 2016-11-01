package miner_pojos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import core.MethodBugLevelCollector.FileInfoHelper;

public class ChangeMetric {

	private int methodHistories;
	private int authors;
	private int sumOfStmtAdded;
	private int maxStmtAdded;
	private double avgStmtAdded;
	private int sumOfStmtDeleted;
	private int maxStmtDeleted;
	private double avgStmtDeleted;
	private int churn;
	private int maxChurn;
	private double avgChurn;
	private int decl;
	private int cond;
	private int elseAdded;
	private int elseDeleted;
	private int numberOfBugs;
	private List<String> commiterNameList = new ArrayList<>();

	public ChangeMetric() {
	}

	public void increaseAuthor(String commiterName) {
		if (!this.commiterNameList.contains(commiterName)) {
			this.commiterNameList.add(commiterName);
			authors++;
		}
	}

	public void updateMetricsPerCommit(int maxChurn, int maxDeleted, int maxAdded) {
		// Maximum number of source code statements added to a method body for
		// all method histories
		updateMaximunSourceCodeStatementsAdded(maxAdded);

		// Maximum number of source code statements deleted from a method body
		// for all method histories
		updateMaximunMethodStatementsDeleted(maxDeleted);

		// Maximum churn for all method histories
		updateMaximunChurn(maxChurn);
	}

	private void updateMaximunChurn(int maxChurn2) {
		// TODO Auto-generated method stub
		
	}

	private void updateMaximunMethodStatementsDeleted(int maxDeleted) {
		// TODO Auto-generated method stub
		
	}

	private void updateMaximunSourceCodeStatementsAdded(int maxAdded) {
		// TODO Auto-generated method stub
		
	}

	public void updateMetricsPerChange(FileInfoHelper fileInfo, SourceCodeChange change) {
		
		// Number of distinct authors that changed a method
		increaseAuthor(fileInfo.getCommitInfo().getCommiterName());

		if (change.getChangeType().equals(ChangeType.STATEMENT_INSERT)) {
			// Sum of all source code statements added to a method body over all
			// method histories
			this.sumOfStmtAdded++;
			// churn: Sum of stmtAdded - stmtDeleted over all method histories
			updateChurn();
		}
		if (change.getChangeType().equals(ChangeType.STATEMENT_DELETE)) {
			// Sum of all source code statements deleted from a method body over
			// all method histories
			this.sumOfStmtDeleted++;
			// churn: Sum of stmtAdded - stmtDeleted over all method histories
			updateChurn();
		}

		//REVISIONS
		
		// Number of method declaration changes over all method histories 
		/*
		 * More generally, method declarations have six components, in order:
		 * 
		 * Modifiers—such as public, private, and others you will learn about
		 * later. The return type—the data type of the value returned by the
		 * method, or void if the method does not return a value. The method
		 * name—the rules for field names apply to method names as well, but the
		 * convention is a little different. The parameter list in parenthesis—a
		 * comma-delimited list of input parameters, preceded by their data
		 * types, enclosed by parentheses, (). If there are no parameters, you
		 * must use empty parentheses. An exception list—to be discussed later.
		 * The method body, enclosed between braces—the method's code, including
		 * the declaration of local variables, goes here.
		 */
		if (change.getChangeType().equals(ChangeType.RETURN_TYPE_CHANGE)
				|| change.getChangeType().equals(ChangeType.RETURN_TYPE_DELETE)
				|| change.getChangeType().equals(ChangeType.RETURN_TYPE_INSERT)
				|| change.getChangeType().equals(ChangeType.PARAMETER_ORDERING_CHANGE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_TYPE_CHANGE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_RENAMING)
				|| change.getChangeType().equals(ChangeType.PARAMETER_DELETE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_INSERT)) {
				this.decl++;
		}
		
		// Number of condition expression changes in a method body over all
		// revisions
		if (change.getChangeType().equals(ChangeType.CONDITION_EXPRESSION_CHANGE)){
			this.cond++;
		}

		// Number of added else-parts in a method body over all revisions
		if (change.getChangeType().equals(ChangeType.STATEMENT_INSERT)
				&& change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT)) {
			this.elseDeleted++;
		}

		// Number of deleted else-parts from a method body over all revisions
		if (change.getChangeType().equals(ChangeType.STATEMENT_DELETE)
				&& change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT)) {
			this.elseDeleted++;
		}

		// Number of times a method was changed
		this.methodHistories++;

		// Average number of source code statements added to a method body per
		// method history
		updateAverageStatementsAdded();

		// Average number of source code statements deleted to a method body per
		// method history
		updateAverageStatementsDeleted();

		// Average churn per method history
		updateAverageChurn();
	}

	private void updateAverageChurn() {
		this.avgChurn = (double) this.churn / (double) this.methodHistories;
	}

	private void updateAverageStatementsDeleted() {
		this.avgStmtDeleted = (double) this.sumOfStmtDeleted / (double) this.methodHistories;
	}

	private void updateAverageStatementsAdded() {
		this.avgStmtAdded = (double) this.sumOfStmtAdded / (double) this.methodHistories;
	}

	private void updateChurn() {
		this.churn = this.sumOfStmtAdded - this.sumOfStmtDeleted;
	}
}