package core;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

import java.io.File;
import java.util.List;

/**
 * Created by mey on 10/30/2016.
 */
public class ExampleDistiller {

    public static void main(String[] args) {

        System.out.println("HERE");

        //One folder for the
        File left = new File("C:\\Users\\mey\\workspace\\TestDistiler\\v1\\Main.java");

        File right = new File("C:\\Users\\mey\\workspace\\TestDistiler\\v2\\Main.java");

        FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
        try {
            distiller.extractClassifiedSourceCodeChanges(left, right);
        } catch (Exception e) {
			/*
			 * An exception most likely indicates a bug in ChangeDistiller.
			 * Please file a bug report at
			 * https://bitbucket.org/sealuzh/tools-changedistiller/issues and
			 * attach the full stack trace along with the two files that you
			 * tried to distill.
			 */
            System.err.println("Warning: error while change distilling. " + e.getMessage());
        }

        List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
        if (changes != null) {
            System.out.println("Here2");
            System.out.println("Changes: "+changes);
            for (SourceCodeChange change : changes) {
                // see Javadocs for more information
                System.out.println("Changes: "+change);
                System.out.println("Change Parent: "+change.getParentEntity());
                System.out.println("Changes Entity: "+change.getChangedEntity());
                System.out.println("Changes Type: "+change.getChangeType());
                System.out.println("Changes Significance: "+change.getSignificanceLevel());
                System.out.println("Changes Label: "+change.getLabel());
            }
            System.out.println("End");
        }else{
            System.out.println("no changes");
        }
    }
}
