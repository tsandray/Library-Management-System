package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdReserveBook;
import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

public class CmdReserveTest {
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
	
	/* Test case 1: Test reserve book with 3 cmdParts
	 * Precondition: 
	 * - Resources in library: [item0]
	 * - Members in library: [mb0]
	 * - item0 is borrowed by mb0
	 * Input: [reserve book, 0, 0]
	 */
	@Test
	public void testReserve1() throws Exception {
		addResource("item0");
		addMember("mb0");
		
		setOutput();
		String[] input = {"reserve book", "0", "0"};
		new CmdReserveBook().execute(input);;
		
		String expected = "Successfully reserved!\n" + 
				"\n" + 
				"Reserve Record\n" + 
				"---------\n" + 
				"TransactionID: 0\n" + 
				"Issue date: 2019-01-01\n" + 
				"Borrower: mb0 (0)\n" + 
				"Item: item0, pub (0)\n" + 
				"Due date: 2019-01-08\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test reserve book with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testReserve2() throws Exception {
		setOutput();
		String[] input = {"reserve book","0"};
		new CmdReserveBook().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}
	
	
	// Test case 3: Test reserve book with invalid argument
	// Input: ["reserve book", "0", "-"]
	@Test
	public void testReserve3() throws Exception {
		setOutput();
		String[] input = {"reserve book","0", "-"};
		new CmdReserveBook().execute(input);
        
		String expected = "Invalid argument!\n\n";
		assertEquals(expected, getOutput());
	} 
	
}
