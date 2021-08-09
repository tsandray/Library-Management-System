package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.Transaction;

// # test case = 5
public class LibraryTest {
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
	
	@BeforeEach
	public void resetEnv() {
		Library.reset();
		lib = Library.getInstance();
		Member.resetNextID();
		Resource.resetNextID();
		Transaction.resetNextID();
	}
	
	// Test case 1: Add 1 member to the library
	@Test
	public void testAddMember1() throws Exception {
		setOutput();
		
		lib.addMember("Name", "email@email.com");
		String expected = "Successfully added.\n" + 
						"MemberID: 0\n" + 
						"Name: Name\n" + 
						"Email: email@email.com\n" + 
						"Status: Activated\n" + 
						"Limit: 10\n\n";
		expected += "Name (ID) | Email\n-----------------\n"
							+ "Name (0) | email@email.com\n\n";
		lib.printAllMembers();
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Add 2 members to the library. The 2 members has no conflict.
	@Test
	public void testAddMember2() throws Exception {
		setOutput();
		
		lib.addMember("Name1", "email1@email.com");
		lib.addMember("Name2", "email2@email.com");
		
		String expected = "Successfully added.\n" + 
				"MemberID: 0\n" + 
				"Name: Name1\n" + 
				"Email: email1@email.com\n" + 
				"Status: Activated\n" + 
				"Limit: 10\n\n";
		
		expected += "Successfully added.\n" + 
					"MemberID: 1\n" + 
					"Name: Name2\n" + 
					"Email: email2@email.com\n" + 
					"Status: Activated\n" + 
					"Limit: 10\n\n";
		
		expected += "Name (ID) | Email\n-----------------\n"
				+ "Name1 (0) | email1@email.com\n"
				+ "Name2 (1) | email2@email.com\n\n";

		lib.printAllMembers();
		assertEquals(expected, getOutput());
	}
	
	// Test case 3: Add 2 members to the library. The 2 members has the same email.
	@Test
	public void testAddMember3() throws Exception {
		setOutput();
		
		lib.addMember("Name1", "email@email.com");
		String expected = "Successfully added.\n" + 
							"MemberID: 0\n" + 
							"Name: Name1\n" + 
							"Email: email@email.com\n" + 
							"Status: Activated\n" + 
							"Limit: 10\n\n";
		assertEquals(expected, getOutput());
		
		setOutput();
		lib.addMember("Name2", "email@email.com");
		assertEquals("Member already exists.\n", getOutput());
		
		setOutput();
		expected = "Name (ID) | Email\n-----------------\n"
				+ "Name1 (0) | email@email.com\n\n";

		lib.printAllMembers();
		assertEquals(expected, getOutput());
	}
	
	// Test case 1: Add 1 resource to the library.
	@Test
	public void testAddResource1() throws Exception {
		setOutput();
		
		lib.addResource("Item", "Publisher", 0, 0.0, ResourceStatus.AVAILABLE);
		
		String expected = "Successfully added.\n" + 
							"ID: 0\n" + 
							"Name: Item\n" + 
							"Publisher: Publisher\n" + 
							"Rack no.: 0\n" + 
							"Status: Available\n\n";
		assertEquals(expected, getOutput());
		
		setOutput();
		expected = "Name, Publisher (ID) | Status\n"
							+ "-----------------------------\n"
							+ "Item, Publisher (0) | Available\n\n";
		
		lib.printAllResources();
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Add 2 resource to the library.
	@Test
	public void testAddResource2() throws Exception {
		setOutput();
		
		lib.addResource("Item1", "Publisher1", 0, 0.0, ResourceStatus.AVAILABLE);		
		lib.addResource("Item2", "Publisher2", 1, 0.0, ResourceStatus.AVAILABLE);

		String expected = "Successfully added.\n" + 
							"ID: 0\n" + 
							"Name: Item1\n" + 
							"Publisher: Publisher1\n" + 
							"Rack no.: 0\n" + 
							"Status: Available\n\n";

		expected += "Successfully added.\n" + 
					"ID: 1\n" + 
					"Name: Item2\n" + 
					"Publisher: Publisher2\n" + 
					"Rack no.: 1\n" + 
					"Status: Available\n\n";

		expected += "Name, Publisher (ID) | Status\n"
							+ "-----------------------------\n"
							+ "Item1, Publisher1 (0) | Available\n"
							+ "Item2, Publisher2 (1) | Available\n\n";
		
		lib.printAllResources();
		assertEquals(expected, getOutput());
	}
}
