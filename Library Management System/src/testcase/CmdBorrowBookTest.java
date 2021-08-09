package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdBorrowBook;
import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

public class CmdBorrowBookTest {
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
	
	/* Test case 1: Test Add Book with 3 cmdParts
	 * Precondition: 
	 * - Resources in library: [item0]
	 * - Members in library: [mb0]
	 * Input: [borrow, 0, 0]
	 */
	@Test
	public void testBorrow1() throws Exception {
		addResource("item0");
		addMember("mb0");
		
		setOutput();
		String[] input = {"borrow", "0", "0"};
		new CmdBorrowBook().execute(input);;
		
		String expected = "Successfully borrowed!\n" + 
				"\n" + 
				"Borrow Record\n" + 
				"---------\n" + 
				"TransactionID: 0\n" + 
				"Issue date: 2019-01-01\n" + 
				"Borrower: mb0 (0)\n" + 
				"Item: item0, pub (0)\n" + 
				"Due date: 2019-01-15\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test Borrow with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testBorrow2() throws Exception {
		setOutput();
		String[] input = {"borrow","0"};
		new CmdBorrowBook().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}
	
	
	// Test case 3: Test Borrow with invalid argument
	// Input: ["borrow", "0", "-"]
	@Test
	public void testBorrow3() throws Exception {
		setOutput();
		String[] input = {"borrow","0", "-"};
		new CmdBorrowBook().execute(input);
        
		String expected = "Invalid argument!\n\n";
		assertEquals(expected, getOutput());
	} 
	
}
