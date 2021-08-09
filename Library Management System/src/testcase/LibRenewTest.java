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

//# test case = 7
public class LibRenewTest {
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
	
	/* Test case 1
	 * Input:
	 * - memberID 0, the member who borrowed item 0
	 * - itemID 0, the item is lent to member 0
	 */
	@Test
	public void testRenewResource1() throws Exception {
		setOutput();
		
		// add member 0 and item 0 to the library
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
		
		// member 0 borrows item 0
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
		
		// member 0 renews item 0
		setOutput();
		lib.renewResource(0, 0);
		expected = "Successfully renewed!\n\n" +
					"Renew Record\n---------\n" +
					"TransactionID: 1\n" + 
					"Issue date: 2019-01-01\n" +
					"Borrower: Name1 (0)\n" +
					"Item: Item, Publisher (0)\n" +
					"Due date: 2019-01-29\n\n";
		
		assertEquals(expected, getOutput());
	}
		
	// Test case 2: Input a memberID that is not in library
	@Test
	public void testRenewResource2() throws Exception {
		setOutput();

		lib.renewResource(0, 0);
		String expected = "Member 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 3: Input a memberID that is in library, resourceID that is not in library
	@Test
	public void testRenewResource3() throws Exception {
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
		lib.renewResource(0, 0);
		expected = "Item 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 4: Input a memberID that is library, resourceID that the resource is not borrowed out
	@Test
	public void testRenewResource4() throws Exception {
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
		lib.renewResource(0, 0);
		expected = "Item 0 is not borrowed out.\n";
		
		assertEquals(expected, getOutput());
	}

	/* Test case 5
	 * Input:
	 * - memberID that is in library
	 * - resourceID that the resource is borrowed out but not by the input member
	 */
	@Test
	public void testRenewResource5() throws Exception {
		setOutput();
		
		// add members and item
		lib.addMember("Member borrow", "emailborrow@email.com");	
		lib.addMember("Member return", "emailreturn@email.com");	
		lib.addResource("Item", "Publisher", 1, 0.0, ResourceStatus.AVAILABLE);
		
		String expected = "Successfully added.\n" + 
							"MemberID: 0\n" + 
							"Name: Member borrow\n" + 
							"Email: emailborrow@email.com\n" + 
							"Status: Activated\n" + 
							"Limit: 10\n\n";
		expected += "Successfully added.\n" + 
				"MemberID: 1\n" + 
				"Name: Member return\n" + 
				"Email: emailreturn@email.com\n" + 
				"Status: Activated\n" + 
				"Limit: 10\n\n";
		expected += "Successfully added.\n" + 
					"ID: 0\n" + 
					"Name: Item\n" + 
					"Publisher: Publisher\n" + 
					"Rack no.: 1\n" + 
					"Status: Available\n\n";
		assertEquals(expected, getOutput());
		
		// member 0 borrows item 0
		setOutput();
		lib.lendResource(0, 0);
		expected = "Successfully borrowed!\n\n" +
					"Borrow Record\n---------\n" +
					"TransactionID: 0\n" + 
					"Issue date: 2019-01-01\n" +
					"Borrower: Member borrow (0)\n" +
					"Item: Item, Publisher (0)\n" +
					"Due date: 2019-01-15\n\n";
		assertEquals(expected, getOutput());
		
		// member 1 tries to renew item 0
		setOutput();
		lib.renewResource(1, 0);
		expected = "Item 0 is not borrowed by member 1.\n";
		
		assertEquals(expected, getOutput());
	}
	
	/* Test case 6
	 * Input:
	 * - memberID that is in library
	 * - resourceID that the resource is borrowed out and renewed
	 */
	@Test
	public void testRenewResource6() throws Exception {
		setOutput();
		
		// add members and item
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
		
		// member 0 borrows item 0
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
		
		// member 0 renew item 0
		setOutput();
		lib.renewResource(0, 0);
		expected = "Successfully renewed!\n\n" +
				"Renew Record\n---------\n" +
				"TransactionID: 1\n" + 
				"Issue date: 2019-01-01\n" +
				"Borrower: Name1 (0)\n" +
				"Item: Item, Publisher (0)\n" +
				"Due date: 2019-01-29\n\n";
	
		assertEquals(expected, getOutput());
		
		setOutput();
		lib.renewResource(0, 0);
		expected = "Item 0 is renew already.\n";
		assertEquals(expected, getOutput());
	}
	
	/* Test case 7
	 * Input:
	 * - memberID that is in library
	 * - resourceID that the resource is borrowed out and expired
	 */
	@Test
	public void testRenewResource7() throws Exception {
		setOutput();
		
		// add members and item
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
		
		// member 0 borrows item 0
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
		
		SystemDate.getInstance().addDate(15);
		
		// member 0 renew item 0
		setOutput();
		lib.renewResource(0, 0);
		expected = "Item 0 is expired. Please return it to the library.\n";
	
		assertEquals(expected, getOutput());
	}
}
