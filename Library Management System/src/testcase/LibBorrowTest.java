package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

// # test case = 4
public class LibBorrowTest {
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
	
	// Test case 1: An existing member in library borrows an existing item which is available, from library
	@Test
	public void testLendResource1() throws Exception {
		setOutput();
				
		lib.addMember("Name1", "email@email.com");		
		lib.addResource("Item", "Publisher", 1, 0.0, ResourceStatus.AVAILABLE);
		
		String expected = "Successfully added.\n" + 
							"MemberID: 0\n" + 
							"Name: Name1\n" + 
							"Email: email@email.com\n" + 
							"Status: Activated\n" + 
							"Limit: 10\n\n";
		
		expected += "Successfully added.\n" + 
					"ID: 0\n" + 
					"Name: Item\n" + 
					"Publisher: Publisher\n" + 
					"Rack no.: 1\n" + 
					"Status: Available\n\n";
		assertEquals(expected, getOutput());
		
		setOutput();
		lib.lendResource(0, 0);
		expected = "Successfully borrowed!\n\n" +
					"Borrow Record\n---------\n" +
					"TransactionID: 0\n" + 
					"Issue date: 2019-01-01\n" +
					"Borrower: Name1 (0)\n" +
					"Item: Item, Publisher (0)\n" +
					"Due date: 2019-01-15\n\n";
		
		assertEquals(expected, getOutput());
	}
		
	// Test case 2: Input a memberID that is not in library
	@Test
	public void testLendResource2() throws Exception {
		setOutput();
	
		lib.addResource("Item", "Publisher", 1, 0.0, ResourceStatus.AVAILABLE);

		String expected = "Successfully added.\n" + 
					"ID: 0\n" + 
					"Name: Item\n" + 
					"Publisher: Publisher\n" + 
					"Rack no.: 1\n" + 
					"Status: Available\n\n";
		assertEquals(expected, getOutput());
		
		setOutput();
		lib.lendResource(0, 0);
		expected = "Member 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 3: Input a memberID that is in library, resourceID that is not in library
	@Test
	public void testLendResource3() throws Exception {
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
		lib.lendResource(0, 0);
		expected = "Item 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 4: Input a memberID that is library, resourceID that the resource is not available
	@Test
	public void testLendResource4() throws Exception {
		setOutput();
				
		lib.addMember("Name1", "email@email.com");		
		lib.addResource("Item", "Publisher", 1, 0.0, ResourceStatus.BORROWED);
		
		String expected = "Successfully added.\n" + 
							"MemberID: 0\n" + 
							"Name: Name1\n" + 
							"Email: email@email.com\n" + 
							"Status: Activated\n" + 
							"Limit: 10\n\n";
		
		expected += "Successfully added.\n" + 
					"ID: 0\n" + 
					"Name: Item\n" + 
					"Publisher: Publisher\n" + 
					"Rack no.: 1\n" + 
					"Status: Borrowed\n\n";
		
		assertEquals(expected, getOutput());
		
		setOutput();
		lib.lendResource(0, 0);
		expected = "Item 0 is not available.\n";
		
		assertEquals(expected, getOutput());
	}

}
