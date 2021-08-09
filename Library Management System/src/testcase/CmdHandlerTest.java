package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CommandHandler;
import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

public class CmdHandlerTest {
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
	
	@Test
	public void testBorrowCmd() throws Exception {
		addMember("Mb");
		addResource("Item");
		
		setOutput();
		String input = "borrow|0|0";
		CommandHandler.handle(input);
		String expected = "Successfully borrowed!\n" + 
				"\n" + 
				"Borrow Record\n" + 
				"---------\n" + 
				"TransactionID: 0\n" + 
				"Issue date: 2019-01-01\n" + 
				"Borrower: Mb (0)\n" + 
				"Item: Item, pub (0)\n" + 
				"Due date: 2019-01-15\n\n";
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testAddBook() throws Exception {
		setOutput();
		String input = "add book|item|pub|0|100";
		CommandHandler.handle(input);
		String expected = "Successfully added.\n" + 
				"ID: 0\n" + 
				"Name: item\n" + 
				"Publisher: pub\n" + 
				"Rack no.: 0\n" + 
				"Status: Available\n" + 
				"\n";
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testAddMember() throws Exception {
		setOutput();
		String input = "add member|member|member@member.lb";
		CommandHandler.handle(input);
		String expected = "Successfully added.\n" + 
				"MemberID: 0\n" + 
				"Name: member\n" + 
				"Email: member@member.lb\n" + 
				"Status: Activated\n" + 
				"Limit: 10\n\n";
		assertEquals(expected, getOutput());
	}
	
	@Test 
	public void testReturnBook() throws Exception {
		addMember("Mb");
		addResource("Item");
		
		setOutput();
		String input = "return book|0|0";
		CommandHandler.handle(input);
		String expected = "Item 0 is not borrowed out.\n";
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testSearchBook() throws Exception {
		setOutput();
		String input = "search book|0";
		CommandHandler.handle(input);
		String expected = "Item is not found.\n\n";
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testReserveBook() throws Exception {
		addMember("mb");
		addResource("item");
		
		setOutput();
		String input = "reserve book|0|0";
		CommandHandler.handle(input);
		String expected = "Successfully reserved!\n" + 
				"\n" + 
				"Reserve Record\n" + 
				"---------\n" + 
				"TransactionID: 0\n" + 
				"Issue date: 2019-01-01\n" + 
				"Borrower: mb (0)\n" + 
				"Item: item, pub (0)\n" + 
				"Due date: 2019-01-08\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testRenewBook() throws Exception {
		addMember("mb");
		addResource("item");
		
		setOutput();
		String input = "renew book|0|0";
		CommandHandler.handle(input);
		
		String expected = "Item 0 is not borrowed out.\n";
		
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testAnalyzeStat() throws Exception {
		setOutput();
		String input = "analyze|0";
		CommandHandler.handle(input);
		
		String expected = "Item | Frequency\n" + 
				"----------------\n" + 
				"Recommended items:\n" + 
				"========\n" + 
				"Statistics:\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testUnknownCmd() throws Exception {
		setOutput();
		String input = "unknown";
		CommandHandler.handle(input);
		
		String expected = "Invalid command. Command usage: \n" + 
				"borrow|memberID|itemID\n" + 
				"return book|memberID|itemID\n" + 
				"add book|name|publisher|rackNo|price\n" + 
				"add member|name|email\n" + 
				"search book|search string\n" + 
				"reserve book|memberID|itemID\n" + 
				"renew book|memberID|itemID\n" +
				"analyze|numDay\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	@Test
	public void testEmptyCmd() throws Exception {
		setOutput();
		String input = "";
		CommandHandler.handle(input);
		
		String expected = "Invalid command. Command usage: \n" + 
				"borrow|memberID|itemID\n" + 
				"return book|memberID|itemID\n" + 
				"add book|name|publisher|rackNo|price\n" + 
				"add member|name|email\n" + 
				"search book|search string\n" + 
				"reserve book|memberID|itemID\n" + 
				"renew book|memberID|itemID\n" +
				"analyze|numDay\n\n";
		
		assertEquals(expected, getOutput());
	}
}
