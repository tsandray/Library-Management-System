package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.ActivatedStatus;
import library.Member;
import library.Resource;
import library.ResourceStatus;
import library.TransactionType;

//# test case = 12
public class MemberTest {
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
		Member.resetNextID();
		Resource.resetNextID();
	}
	
	// Test case 1: Search existing id
	@Test
	public void testSearchByID1() {
		ArrayList<Member> mbs = new ArrayList<>();
		mbs.add(new Member("Name1", "email1@email.com", new ActivatedStatus()));
		mbs.add(new Member("Name2", "email2@email.com", new ActivatedStatus()));
		
		Member mb = Member.searchMemberByID(mbs, 0);
		assertEquals("Name1 (0)", mb.getInfo());
	}
	
	// Test case 2: Search id that does not exist
	@Test
	public void testSearchByID2() {
		ArrayList<Member> mbs = new ArrayList<>();
		mbs.add(new Member("Name1", "email1@email.com", new ActivatedStatus()));
		mbs.add(new Member("Name2", "email2@email.com", new ActivatedStatus()));
		
		Member mb = Member.searchMemberByID(mbs, 3);
		assertEquals(null, mb);
	}
	
	// Test case 3: calculate due date for borrowing item
	@Test
	public void testCalDueDate1() {
		Calendar c = Calendar.getInstance();
		c.set(2019, 1, 1, 1, 1, 1);
		
		Date res = Member.calculateDueDate(c.getTime(), TransactionType.BORROW);
		c.add(Calendar.DATE, 14);
		assertEquals(c.getTime(), res);
	}
	
	// Test case 4: calculate due date for reserving item
	@Test
	public void testCalDueDate2() {
		Calendar c = Calendar.getInstance();
		c.set(2019, 1, 1, 1, 1, 1);
		
		Date res = Member.calculateDueDate(c.getTime(), TransactionType.RESERVE);
		c.add(Calendar.DATE, 7);
		assertEquals(c.getTime(), res);
	}
	
	// Test case 5: calculate due date for returning item
	@Test
	public void testCalDueDate3() {
		Calendar c = Calendar.getInstance();
		c.set(2019, 1, 1, 1, 1, 1);
		
		Date res = Member.calculateDueDate(c.getTime(), TransactionType.RETURN);
		assertEquals(c.getTime(), res);
	}
	
	// Test case 6: member with activated status borrows 1 item
	@Test
	public void testBorrow1() {
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		
		int res = m.borrow(item);
		assertEquals(1, res);
	}
	
	// Test case 7: member with activated status borrows 11 items (i.e. exceed the limit)
	@Test
	public void testBorrow2() throws Exception {
		setOutput();
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		
		int res = 0;
		for (int i = 0; i<10; i++) {
			Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
			
			res += m.borrow(item);
		}
		assertEquals(10, res);
		
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		res = m.borrow(item);
		assertEquals("Not enough quota!\n", getOutput());
		assertEquals(0, res);
	}
	
	// Test case 8: member with activated status reserve 1 item
	@Test
	public void testReserve1() {
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		
		int res = m.reserve(item);
		assertEquals(1, res);
	}
	
	// Test case 9: member with activated status reserve 3 different items (i.e. exceed reserve limit)
	@Test
	public void testReserve2() throws Exception {
		setOutput();
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		
		int res = 0;
		for (int i = 0; i<2; i++) {
			Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
			res += m.reserve(item);
		}
		assertEquals(2, res);
		
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		res = m.reserve(item);
		assertEquals("Not enough reserve quota!\n", getOutput());
		assertEquals(0, res);
	}
	
	// Test case 10: member with activated status try to reserve same item
	@Test
	public void testReserve3() throws Exception {
		setOutput();
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		int res = m.reserve(item);
		assertEquals(1, res);
		
		res = m.reserve(item);
		assertEquals("Item 0 is in the reserved list already!\n", getOutput());
		assertEquals(0, res);
	}
	
	// Test case 11: member with activated status return 1 item
	@Test
	public void testReturn1() {
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		
		int res = m.borrow(item);
		assertEquals(1, res);
		
		res = m.returnResource(item);
		assertEquals(1, res);
	}
	
	// Test case 12: member with activated status return the item without borrowing any item
	@Test
	public void testReturn2() throws Exception {
		setOutput();
		
		Member m = new Member("Name", "a@b.c", new ActivatedStatus());
		Resource item = new Resource("Item", "Pub", 0, 0.0, ResourceStatus.AVAILABLE);
		
		int res = m.returnResource(item);
		assertEquals("Member 0 did not borrow item 0.\n", getOutput());
		assertEquals(0, res);
	}
}
