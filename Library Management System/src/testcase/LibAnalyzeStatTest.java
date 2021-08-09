package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.Library;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.SystemDate;
import library.Transaction;

// # test case = 8
public class LibAnalyzeStatTest {
	private Library lib;

	private void setSystemDate() {
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		
		SystemDate.getInstance().setDate(c.getTime());
	}
	
	private void addResource(String name) {
		lib.addResource(name, "pub", 1, 0.0, ResourceStatus.AVAILABLE);
	}
	
	private void addMember(String name) {
		lib.addMember(name , name + "@email.com");	
	}
	
	@BeforeEach
	private void resetEnv() {
		Library.reset();
		lib = Library.getInstance();
		Member.resetNextID();
		Resource.resetNextID();
		Transaction.resetNextID();
		setSystemDate();
	}
	
	/* Test case 1
	 * Precondition: 1 transaction in library
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat1() {
		addMember("m1");
		addResource("r1");
		lib.lendResource(0, 0);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n"
				+ "r1, pub (0) | 1\n";
		expected += "========\n"
				+ "Statistics:\n"
				+ "r1, pub (0) | 1\n\n";

		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 2
	 * Precondition: 2 borrow transactions of item 0 in the library
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat2() {
		addMember("m1");
		addResource("r1");
		lib.lendResource(0, 0);
		lib.takeBackResource(0, 0);
		lib.lendResource(0, 0);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n"
				+ "r1, pub (0) | 2\n";
		expected += "========\n"
				+ "Statistics:\n"
				+ "r1, pub (0) | 2\n\n";
		
		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 3
	 * Precondition: 0 transactions in the library
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat3() {

		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n";
		expected += "========\n"
				+ "Statistics:\n\n";
		
		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 4
	 * Precondition:
	 * - Transaction record: [item0, item1, item1]
	 * 
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat4() {
		addMember("m1");
		addResource("r1");
		addResource("r2");
		lib.lendResource(0, 0);
		lib.lendResource(0, 1);
		lib.takeBackResource(0, 1);
		lib.lendResource(0, 1);
		lib.takeBackResource(0, 1);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n"
				+ "r2, pub (1) | 2\n"
				+ "r1, pub (0) | 1\n";
		expected += "========\n"
				+ "Statistics:\n"
				+ "r2, pub (1) | 2\n"
				+ "r1, pub (0) | 1\n\n";
		
		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 5
	 * Precondition:
	 * - Transaction record: [item0, item1, item2, item2, item3]
	 * 
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat5() {		
		addMember("m1");
		addResource("r1");
		addResource("r2");
		addResource("r3");
		addResource("r4");
		lib.lendResource(0, 0);
		lib.lendResource(0, 1);
		lib.lendResource(0, 2);
		lib.takeBackResource(0, 2);
		lib.lendResource(0, 2);
		lib.lendResource(0, 3);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n"
				+ "r3, pub (2) | 2\n"
				+ "r1, pub (0) | 1\n"
				+ "r2, pub (1) | 1\n";
		expected += "========\n"
				+ "Statistics:\n"
				+ "r3, pub (2) | 2\n"
				+ "r1, pub (0) | 1\n"
				+ "r2, pub (1) | 1\n"
				+ "r4, pub (3) | 1\n\n";
		
		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 6
	 * Precondition:
	 * - Transaction record: [item0, item1, item2, item2, item2, item3, item1]
	 * 
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat6() {		
		addMember("m1");
		addResource("r0");
		addResource("r1");
		addResource("r2");
		addResource("r3");
		
		lib.lendResource(0, 0);
		lib.lendResource(0, 1);
		lib.lendResource(0, 2);
		lib.takeBackResource(0, 2);
		lib.lendResource(0, 2);
		lib.takeBackResource(0, 2);
		lib.lendResource(0, 2);
		lib.lendResource(0, 3);
		lib.takeBackResource(0, 1);
		lib.lendResource(0, 1);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n"
				+ "r2, pub (2) | 3\n"
				+ "r1, pub (1) | 2\n"
				+ "r0, pub (0) | 1\n";
		expected += "========\n"
				+ "Statistics:\n"
				+ "r2, pub (2) | 3\n"
				+ "r1, pub (1) | 2\n"
				+ "r0, pub (0) | 1\n"
				+ "r3, pub (3) | 1\n\n";
		
		String res = lib.analyzeStat(1);
		assertEquals(expected, res);
	}
	
	/* Test case 7
	 * Precondition:
	 * - Transaction record: [item0, item1, item2, item2, item2, item3, item1]
	 * - All transaction records are 2 days before system date
	 * 
	 * Input: numDay = 1
	 */
	@Test
	public void testAnalyzeStat7() {		
		addMember("m1");
		addResource("r0");
		addResource("r1");
		addResource("r2");
		addResource("r3");
		
		lib.lendResource(0, 0);
		lib.lendResource(0, 1);
		lib.lendResource(0, 2);
		lib.takeBackResource(0, 2);
		lib.lendResource(0, 2);
		lib.takeBackResource(0, 2);
		lib.lendResource(0, 2);
		lib.lendResource(0, 3);
		lib.takeBackResource(0, 1);
		lib.lendResource(0, 1);
		
		String expected = "Item | Frequency\n"
				+ "----------------\n";
		expected += "Recommended items:\n";
		expected += "========\n"
				+ "Statistics:\n\n";
		
		SystemDate.getInstance().addDate(1);
		String res = lib.analyzeStat(0);
		assertEquals(expected, res);
	}
	
	/* Test case 8
	 * To test the validation when the input numDay is negative
	 * Input: numDay = -1
	 */
	@Test
	public void testAnalyzeStat8() {		
		String expected = "Invalid number of days! Please input non-negative number.\n\n";
		
		String res = lib.analyzeStat(-1);
		assertEquals(expected, res);
	}
}
