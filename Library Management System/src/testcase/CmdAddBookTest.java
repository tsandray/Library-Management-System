package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdAddBook;
import library.Library;
import library.Member;
import library.Resource;
import library.Transaction;

public class CmdAddBookTest {
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
	
	/* Test case 1: Test Add Book with 3 cmdParts
	* Input: [add book,Item0,Publisher0, 0, 0]
	*/
	@Test
	public void testAddBook1() throws Exception {
		setOutput();
		String[] input = {"add book","Item0","Publisher0", "0", "100"};
		new CmdAddBook().execute(input);
		
		String expected = "Successfully added.\n" + 
				"ID: 0\n" + 
				"Name: Item0\n" + 
				"Publisher: Publisher0\n" + 
				"Rack no.: 0\n" +
				"Status: Available\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test Add Book with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testAddMember2() throws Exception {
		setOutput();
		String[] input = {"add book","Item0"};
		new CmdAddBook().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}
	
	
	// Test case 3: Test Add Book with invalid argument
	// Input: ["add member", "Item", "Publisher", "-" (invalid), "0.0"]
	@Test
	public void testAddMember3() throws Exception {
		setOutput();
		String[] input = {"add book","Item", "Publisher","-","0.0"};
		new CmdAddBook().execute(input);
        
		String expected = "Invalid argument!\n\n";
		assertEquals(expected, getOutput());
	} 
	
}
