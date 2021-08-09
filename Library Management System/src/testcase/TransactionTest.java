package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.SystemDate;
import library.Transaction;
import library.TransactionType;;

//# test case = 6
public class TransactionTest {
	@BeforeEach
	public void resetEnv() {
		Transaction.resetNextID();
		
		// reset system date
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		
		SystemDate.getInstance().setDate(c.getTime());
	}
	
	// Test case 1: Input an empty list
	@Test
	public void testGetLastestTransaction1() {
		ArrayList<Transaction> list = new ArrayList<>();
		Transaction res = Transaction.getLatestTransaction(list, 0);
		assertEquals(null, res);
	}
	
	// Test case 2: Input list with size 1, item id 0 (exist in list)
	@Test
	public void testGetLastestTransaction2() {
		ArrayList<Transaction> list = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		
		Transaction expected = new Transaction(TransactionType.BORROW, 0, 0, c.getTime());
		list.add(expected);
		
		Transaction res = Transaction.getLatestTransaction(list, 0);
		assertEquals(expected, res);
	}

	/* Test case 3
	 * Input:
	 * - a list with size 2 (one transaction with rID 0, another with rID 1)
	 * - rID: 1
	 */
	@Test
	public void testGetLastestTransaction3() {
		ArrayList<Transaction> list = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		
		list.add(new Transaction(TransactionType.BORROW, 0, 0, c.getTime()));
		Transaction expected = new Transaction(TransactionType.BORROW, 1, 0, c.getTime());
		list.add(expected);
		
		Transaction res = Transaction.getLatestTransaction(list, 1);
		assertEquals(expected.getInfo(), res.getInfo());
	}
	
	/* Test case 4
	 * Input:
	 * - a list with size 2 (both transactions with resourceID 0)
	 * - rID: 0
	 */
	@Test
	public void testGetLastestTransaction4() {
		ArrayList<Transaction> list = new ArrayList<>();
		
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 1, 1, 1, 1);
		list.add(new Transaction(TransactionType.BORROW, 0, 0, c.getTime()));

		SystemDate.getInstance().addDate(1);
		Transaction expected = new Transaction(TransactionType.RETURN, 0, 0, null);
		list.add(expected);
		
		Transaction res = Transaction.getLatestTransaction(list, 0);
		assertEquals(expected.getInfo(), res.getInfo());
	}
	
	/* Test case 5
	 * Input:
	 * - a list with size 2 (both transactions with resourceID 0)
	 * - rID: 0
	 */
	@Test
	public void testGetLastestTransaction5() {
		ArrayList<Transaction> list = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.set(2019, 0, 2, 1, 1, 1);
		
		Transaction expected = new Transaction(TransactionType.BORROW, 0, 0, c.getTime());
		list.add(expected);
		
		SystemDate.getInstance().addDate(-1);
		list.add(new Transaction(TransactionType.RETURN, 0, 0, null));		
		
		Transaction res = Transaction.getLatestTransaction(list, 0);
		assertEquals(expected.getInfo(), res.getInfo());
	}
	
	/* Test case 6
	 * Input:
	 * - a list with size 1 (transactions with resourceID 0)
	 * - rID: 1
	 */
	@Test
	public void testGetLastestTransaction6() {
		ArrayList<Transaction> list = new ArrayList<>();
		
		list.add(new Transaction(TransactionType.RETURN, 0, 0, null));
		
		Transaction res = Transaction.getLatestTransaction(list, 1);
		assertEquals(null, res);
	}
}
