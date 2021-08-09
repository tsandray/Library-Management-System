package library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//TODO
public class Member {
	private static long nextMemberID = 0;
	private static int loanPeriod = 14;	// Unit: day
	private static int reservePeriod = 7; // Unit: day
	
	private long memberID;
	private int resourceLimit = Constant.MEMBER_RESOURCE_LIMIT;
	private int reserveLimit = Constant.MEMBER_RESERVE_LIMIT;
	private String name;
	private String email;
	private MemberStatus status;
	private ArrayList<Resource> borrowedItems = new ArrayList<>();
	private ArrayList<Resource> reservedItems = new ArrayList<>();
	
	public Member(String name, String email, MemberStatus st) {
		this.name=name;
		this.email=email;
		this.status = st;
		
		this.memberID=nextMemberID;
		nextMemberID++;
	}
	
	/**
	 * Handles the rules for member to borrow items
	 * @param item
	 * @return 1 if success, 0 if failed
	 */
	public int borrow(Resource item) {
		if (!hasQuota()) {
			System.out.print("Not enough quota!\n");
			return 0;
		}
		
		String result = status.borrow(this);
		
		if (result != null) {
			System.out.println(result);
			return 0;
		} else {
			borrowedItems.add(item);
			return 1;
		}
	}
	
	public String getInfo() { return String.format("%s (%d)", name, memberID); }
	public String getDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("MemberID: " + memberID);
		sb.append("\nName: " + name);
		sb.append("\nEmail: " + email);
		sb.append("\nStatus: " + status);
		sb.append("\nLimit: " + resourceLimit);
		sb.append("\n\n");
		
		return sb.toString();
	}
	public long getID() { return memberID; }
	public String getEmail() { return email; }
	
	public boolean hasQuota() { return resourceLimit > borrowedItems.size(); }
	public boolean hasReserveQuota() { return reserveLimit > reservedItems.size(); }

	/**
	 * Handles the rules for member to reserve an item
	 * @param item
	 * @return 1 if success, 0 if failed
	 */
	public int reserve(Resource item) {
		if (!hasReserveQuota()) {
			System.out.print("Not enough reserve quota!\n");
			return 0;
		}
			
		String result = status.reserve(this);
		
		if (reservedItems.contains(item)) {
			System.out.printf("Item %d is in the reserved list already!\n", item.getID());
			return 0;
		} else {
			if(result != null) {
				System.out.println(result);
				return 0;
			} else {
				reservedItems.add(item);
				return 1;
			}
		}
	}
	
	/**
	 * Handles the rules for member to return an item
	 * @param item
	 * @return 1 if success, 0 if failed
	 */
	public int returnResource(Resource item) {
		if (borrowedItems.contains(item)) {
			String res = status.returnResource(this);
			if (res == null)
				return 1;
			
			System.out.println(res);
		} else {
			System.out.printf("Member %d did not borrow item %d.\n", memberID, item.getID());
		}
		
		return 0;
	}
	
	public static Date calculateDueDate(Date current, TransactionType type) {
		Calendar c = Calendar.getInstance();
		c.setTime(current);
		
		int period;
		switch (type) {
		case BORROW:
			period = loanPeriod;
			break;
		case RESERVE:
			period = reservePeriod;
			break;
		default:
			period = 0;
		}
		c.add(Calendar.DATE, period);
		
		return c.getTime();
	}
	
	public static Member searchMemberByID(ArrayList<Member> list, long id) {
		for (Member member: list) {
			if (member.memberID == id)
				return member;
		}

		return null;
	}
	
	// for testing purpose
	public static void resetNextID() { nextMemberID = 0; }
}
