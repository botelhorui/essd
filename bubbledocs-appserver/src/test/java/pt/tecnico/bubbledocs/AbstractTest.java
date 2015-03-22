package pt.tecnico.bubbledocs;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

import pt.tecnico.bubbledocs.domain.BubbleDocs;


public abstract class AbstractTest {
	
	BubbleDocs bd;
	
    @Before
    public void setUp() throws Exception {
        try {
        	bd = BubbleDocs.getInstance();
            FenixFramework.getTransactionManager().begin(false);
            populateDomain();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    protected abstract void populateDomain();
}
