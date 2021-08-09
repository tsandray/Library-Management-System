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

//# test case = 5
public class LibReserveTest {
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
	 * - memberID 0
	 * - itemID 0, the item is available
	 */
	@Test
	public void testReserveResource1() throws Exception {
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
		lib.reserveResource(0, 0);
		expected = "Successfully reserved!\n\n" +
					"Reserve Record\n---------\n" +
					"TransactionID: 0\n" + 
					"Issue date: 2019-01-01\n" +
					"Borrower: Name1 (0)\n" +
					"Item: Item, Publisher (0)\n" +
					"Due date: 2019-01-08\n\n";
		
		assertEquals(expected, getOutput());
	}
		
	// Test case 2: Input a memberID that is not in library
	@Test
	public void testReserveResource2() throws Exception {
		setOutput();

		lib.reserveResource(0, 0);
		String expected = "Member 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 3: Input a memberID that is in library, resourceID that is not in library
	@Test
	public void testReserveResource3() throws Exception {
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
		lib.reserveResource(0, 0);
		expected = "Item 0 is not found.\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 4: Input a memberID that is library, resourceID that the resource is not available
	@Test
	public void testReserveResource4() throws Exception {
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
		lib.reserveResource(0, 0);
		expected = "Item 0 is not available.\n";
		
		assertEquals(expected, getOutput());
	}

	/* Test case 5
	 * Input:
	 * - memberID: 0, which the member is in library and has not enough reserve quota
	 * - resourceID: 0
	 */
	@Test
	public void testReserveResource5() throws Exception {
		setOutput();
		
		// add members and item
		lib.addMember("Member", "email@email.com");	
		lib.addResource("Item1", "Publisher", 1, 0.0, ResourceStatus.AVAILABLE);
		lib.addResource("Item2", "Publisher", 2, 0.0, ResourceStatus.AVAILABLE);
		lib.addResource("Item3", "Publisher", 3, 0.0, ResourceStatus.AVAILABLE);

		
		String expected = "Successfully added.\n" + 
							"MemberID: 0\n" + 
							"Name: Member\n" + 
							"Email: email@email.com\n" + 
							"Status: Activated\n" + 
							"Limit: 10\n\n";
		expected += "Successfully added.\n" + 
					"ID: 0\n" + 
					"Name: Item1\n" + 
					"Publisher: Publisher\n" + 
					"Rack no.: 1\n" + 
					"Status: Available\n\n";
		expected += "Successfully added.\n" + 
				"ID: 1\n" + 
				"Name: Item2\n" + 
				"Publisher: Publisher\n" + 
				"Rack no.: 2\n" + 
				"Status: Available\n\n";
		expected += "Successfully added.\n" + 
				"ID: 2\n" + 
				"Name: Item3\n" + 
				"Publisher: Publisher\n" + 
				"Rack no.: 3\n" + 
				"Status: Available\n\n";
		assertEquals(expected, getOutput());
		
		// member 0 reserve item 0
		setOutput();
		lib.reserveResource(0, 0);
		lib.reserveResource(0, 1);
		expected = "Successfully reserved!\n\n" +
					"Reserve Record\n---------\n" +
					"TransactionID: 0\n" + 
					"Issue date: 2019-01-01\n" +
					"Borrower: Member (0)\n" +
					"Item: Item1, Publisher (0)\n" +
					"Due date: 2019-01-08\n\n";
		expected += "Successfully reserved!\n\n" +
				"Reserve Record\n---------\n" +
				"TransactionID: 1\n" + 
				"Issue date: 2019-01-01\n" +
				"Borrower: Member (0)\n" +
				"Item: Item2, Publisher (1)\n" +
				"Due date: 2019-01-08\n\n";
		assertEquals(expected, getOutput());
		
		// member 0 tries to renew item 0 again
		setOutput();
		lib.reserveResource(0, 2);
		expected = "Not enough reserve quota!\n";
		assertEquals(expected, getOutput());
	}
}
