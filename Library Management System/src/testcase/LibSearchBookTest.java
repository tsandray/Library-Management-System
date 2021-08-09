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
public class LibSearchBookTest {
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
	
	private void addResource(String name) {
		lib.addResource(name, "pub", 1, 0.0, ResourceStatus.AVAILABLE);
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
	
	/* Test case 1:
	 * Test searchString that searches name only
	 * Precondition: books in library - [abc, ab, bc, c, d]
	 * Input: ab
	 */
	@Test
	public void testSearchBook1() throws Exception {
		addResource("abc");
		addResource("ab");
		addResource("bc");
		addResource("c");
		addResource("d");
		
		setOutput();
		lib.searchBooks("ab");
		String expected = "Result:\n"
				+ "abc, pub (0)\n"
				+ "ab, pub (1)\n\n";
		assertEquals(expected, getOutput());
	}
	
	/* Test case 2:
	 * Test searchString that searches id only
	 * Precondition: books in library - [abc, ab, bc, c, d]
	 * Input: 0
	 */
	@Test
	public void testSearchBook2() throws Exception {
		addResource("abc");
		addResource("ab");
		addResource("bc");
		addResource("c");
		addResource("d");
		
		setOutput();
		lib.searchBooks("0");
		String expected = "Result:\n"
				+ "abc, pub (0)\n\n";
		assertEquals(expected, getOutput());
	}
	
	/* Test case 3:
	 * Test searchString that searches id and name
	 * Precondition: books in library - [abc, ab, bc, c, item0]
	 * Input: 0
	 */
	@Test
	public void testSearchBook3() throws Exception {
		addResource("abc");
		addResource("ab");
		addResource("bc");
		addResource("c");
		addResource("item0");
		
		setOutput();
		lib.searchBooks("0");
		String expected = "Result:\n"
				+ "abc, pub (0)\n"
				+ "item0, pub (4)\n\n";
		assertEquals(expected, getOutput());
	}
	
	/* Test case 4:
	 * Test searchString that with no match
	 * Precondition: books in library - [abc, ab, bc, c, d]
	 * Input: 0
	 */
	@Test
	public void testSearchBook4() throws Exception {
		addResource("abc");
		addResource("ab");
		addResource("bc");
		addResource("c");
		addResource("d");
		
		setOutput();
		lib.searchBooks("e");
		String expected = "Item is not found.\n\n";
		assertEquals(expected, getOutput());
	}
}
