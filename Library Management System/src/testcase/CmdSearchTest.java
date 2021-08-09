package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdSearchBook;
import library.Library;
import library.Member;
import library.Resource;
import library.Transaction;

public class CmdSearchTest {
	PrintStream oldPrintStream;
	ByteArrayOutputStream bos;

	private void setOutput() throws Exception {
		oldPrintStream = System.out;
		bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));
	}

	private String getOutput() { // throws Exception
		System.setOut(oldPrintStream);
		return bos.toString();
	}
	
	@BeforeEach
	public void resetEnv() {
		Library.reset();
		Member.resetNextID();
		Resource.resetNextID();
		Transaction.resetNextID();
	}
	
	/* Test case 1: Test search book with 3 cmdParts
	 * Precondition: 
	 * - Resources in library: []
	 * - Members in library: []
	 * Input: [search book, 0]
	 */
	@Test
	public void testSearch1() throws Exception {
		setOutput();
		String[] input = {"search book", "0"};
		new CmdSearchBook().execute(input);;
		
		String expected = "Item is not found.\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test search book with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testSearch2() throws Exception {
		setOutput();
		String[] input = {"search book"};
		new CmdSearchBook().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}	
}
