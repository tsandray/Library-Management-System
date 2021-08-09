package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdAddMember;
import library.Library;
import library.Member;
import library.Resource;
import library.Transaction;

// # test case = 3
public class CmdAddMemberTest {
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
	
	/* Test case 1: Test Add Member with 3 cmdParts
	* Input: [add member,Cindy,c@email.com]
	*/
	@Test
	public void testAddMember1() throws Exception {
		setOutput();
		String[] input = {"add member","Cindy","c@email.com"};
		new CmdAddMember().execute(input);
		
		String expected = "Successfully added.\n" + 
				"MemberID: 0\n" + 
				"Name: Cindy\n" + 
				"Email: c@email.com\n" + 
				"Status: Activated\n" + 
				"Limit: 10\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test Add Member with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testAddMember2() throws Exception {
		setOutput();
		String[] input = {"add member","Bob"};
		new CmdAddMember().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}
	
	
	// Test case 3: Test Add Member with >3 cmdParts - excess arguments will be ignored
	@Test
	public void testAddMember3() throws Exception {
		setOutput();
		String[] input = {"add member","Adam", "a@email.com","18","student"};
		new CmdAddMember().execute(input);
        
		String expected = "Successfully added.\n" + 
				"MemberID: 0\n" + 
				"Name: Adam\n" + 
				"Email: a@email.com\n" + 
				"Status: Activated\n" + 
				"Limit: 10\n\n";
		assertEquals(expected, getOutput());
	} 
	
}
