package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdReturnBook;
import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

public class CmdReturnTest {
	private Library lib;
	
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
	
	private void addResource(String name) {
		lib.addResource(name, "pub", 1, 0.0, ResourceStatus.AVAILABLE);
	}
	
	private void addMember(String name) {
		lib.addMember(name , name + "@email.com");	
	}
	
	private void setSystemDate() {
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		
		SystemDate.getInstance().setDate(c.getTime());
	}
	
	@BeforeEach
	public void resetEnv() {
		Library.reset();
		lib = Library.getInstance();
		Member.resetNextID();
		Resource.resetNextID();
		Transaction.resetNextID();
		setSystemDate();
	}
	
	/* Test case 1: Test return book with 3 cmdParts
	 * Precondition: 
	 * - Resources in library: [item0]
	 * - Members in library: [mb0]
	 * - item0 is borrowed by mb0
	 * Input: [return book, 0, 0]
	 */
	@Test
	public void testReturn1() throws Exception {
		addResource("item0");
		addMember("mb0");
		lib.lendResource(0, 0);
		
		setOutput();
		String[] input = {"return book", "0", "0"};
		new CmdReturnBook().execute(input);;
		
		String expected = "Successfully returned!\n" + 
				"\n" + 
				"Return Record\n" + 
				"---------\n" + 
				"TransactionID: 1\n" + 
				"Issue date: 2019-01-01\n" + 
				"Borrower: mb0 (0)\n" + 
				"Item: item0, pub (0)\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test return book with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testReturn2() throws Exception {
		setOutput();
		String[] input = {"return book","0"};
		new CmdReturnBook().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}
	
	
	// Test case 3: Test reserve book with invalid argument
	// Input: ["return book", "0", "-"]
	@Test
	public void testReturn3() throws Exception {
		setOutput();
		String[] input = {"return book","0", "-"};
		new CmdReturnBook().execute(input);
        
		String expected = "Invalid argument!\n\n";
		assertEquals(expected, getOutput());
	} 
	
}
