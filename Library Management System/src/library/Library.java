package library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Library { 
	private static Library instance = new Library();
	
	private ArrayList<Resource> resources = new ArrayList<>();
	private ArrayList<Member> members = new ArrayList<>();
	private ArrayList<Transaction> transactionRecords = new ArrayList<>();
	
	public static Library getInstance() { return instance; }
	public static void reset() { instance = new Library(); }
	
	private Library() {}
	
	public Member addMember(String name, String email) {
	    
	    for(Member m : members ) {
		     if(m.getEmail().equals(email)){
		    	 System.out.print("Member already exists.\n");
		    	 return null;
		     }
	    }
	    
	    Member m = new Member(name, email, new ActivatedStatus());
	    members.add(m);
	    System.out.printf("Successfully added.\n%s", m.getDetail());
	    return m;
	}
	
    public Resource addResource(String name, String publisher, long rackNo, double price, ResourceStatus status) {
		Resource r = new Resource(name, publisher, rackNo, price, status);
		resources.add(r);
		System.out.printf("Successfully added.\n%s", r.getPubDetail());
		return r;												
	}
    
	public String analyzeStat(int numDay) {
		if (numDay < 0) {
			return "Invalid number of days! Please input non-negative number.\n\n";
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(SystemDate.getInstance().getDate());
		c.add(Calendar.DATE, 0 - numDay);
		
		ArrayList<Transaction> listInPeriod = Transaction.filterListAfterDate(transactionRecords, c.getTime());
		
		// sort the list by value
		ArrayList<Long> items = new ArrayList<>();
		ArrayList<Integer> freq = new ArrayList<>();
		for (Transaction t: listInPeriod) {
			if (t.getType() == TransactionType.RETURN)
				continue;
			
			Long id = t.getResourceID();
			int index = items.indexOf(id);
			
			if (index == -1) {
				items.add(id);
				freq.add(1);
			} else {
				Integer updatedVal = freq.get(index) + 1;
				freq.set(index, updatedVal);
				// prevent array index out of bound
				int prevInd = index - 1 < 0 ? 0 : index - 1;
				
				while (prevInd >= 0 && freq.get(prevInd).compareTo(updatedVal) < 0) {
					Collections.swap(items, index, prevInd);
					Collections.swap(freq, index, prevInd);
					index = prevInd;
					prevInd = index - 1;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Item | Frequency\n");
		sb.append("----------------\n");
		sb.append("Recommended items:\n");
		for (int i = 0; i<items.size() && i<3; i++) {
			Resource item = Resource.searchByID(resources, items.get(i));
			sb.append(item.getInfo() + " | " + freq.get(i) + "\n");
		}
		
		sb.append("========\n");
		sb.append("Statistics:\n");
		for (int i = 0; i<items.size(); i++) {
			Resource item = Resource.searchByID(resources, items.get(i));
			sb.append(item.getInfo() + " | " + freq.get(i) + "\n");
		}
		
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * Handle borrow book request
	 * @param memberID
	 * @param resourceID
	 */
	public void lendResource(long memberID, long resourceID) {
		Member member = Member.searchMemberByID(members, memberID);
		if (member == null) {
			System.out.printf("Member %s is not found.\n", memberID);
			return;
		}
				
		Resource item = Resource.searchByID(resources, resourceID);
		if (item == null) {
			System.out.printf("Item %s is not found.\n", resourceID);
			return;
		}
		
		if (item.getStatus() != ResourceStatus.AVAILABLE) {
			System.out.printf("Item %s is not available.\n", resourceID);
			return;
		}
		
		member.borrow(item);
		
		Date dueDate = Member.calculateDueDate(SystemDate.getInstance().getDate(), TransactionType.BORROW);

		item.updateStatus(ResourceStatus.BORROWED);
	
		Transaction record = new Transaction(TransactionType.BORROW, item.getID(), member.getID(), dueDate);
		transactionRecords.add(record);
		System.out.printf("Successfully borrowed!\n\n%s", record);
	}
	
	/**
	 * Print all members' info in the library
	 */
	public void printAllMembers() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name (ID) | Email\n"); 
		sb.append("-----------------\n");
		
		for (Member m: members) {
			sb.append(m.getInfo() + " | " + m.getEmail() + "\n");
		}
		
		sb.append("\n");
		System.out.print(sb.toString());
	}
	
	public void printAllResources() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name, Publisher (ID) | Status\n");
		sb.append("-----------------------------\n");
		
		for (Resource r: resources) {
			sb.append(r.getInfo() + " | " + r.getStatus() + "\n");
		}
		
		sb.append("\n");
		System.out.print(sb.toString());
	}

    public void searchBooks(String bookSearch) {
    	StringBuilder sb = new StringBuilder();

        for (Resource r : resources)
        {
        	String strID = String.valueOf(r.getID());
	        if (r.getName().contains(bookSearch) || strID.contains(bookSearch))
	        {
//	             return r; 
	             sb.append(r.getInfo() + "\n");
	        }	
    	}
        
        if (sb.length() == 0) {
        	System.out.printf("Item is not found.\n\n");
//	    return null; //no book was found
        } else {
        	System.out.printf("Result:\n%s\n", sb.toString());
        }
    }
	
	public Resource searchResourceByID(long id) {
		return Resource.searchByID(resources, id);
	}
	
	public Member searchMemberByID(long id) {
		return Member.searchMemberByID(members, id);
	}
	
	/**
	 * 
	 * @param memberID
	 * @param resourceID
	 */
	public void takeBackResource(long memberID, long resourceID) {
	    //check member exists
		Member member = Member.searchMemberByID(members, memberID);
		if (member == null) {
			System.out.printf("Member %s is not found.\n", memberID);
			return;
		}
		
		Resource item = Resource.searchByID(resources, resourceID);
		if (item == null) {
			System.out.printf("Item %s is not found.\n", resourceID);
			return;
		}
		
		if (item.getStatus() != ResourceStatus.BORROWED) {
			System.out.printf("Item %s is not borrowed out.\n", resourceID);
			return;
		}
		
		// 0 represents failure to borrow item
		if (member.returnResource(item) == 0) {
			return;
		}
				
		item.updateStatus(ResourceStatus.AVAILABLE);
		
		Transaction record = new Transaction(TransactionType.RETURN, item.getID(), member.getID(), null);
		transactionRecords.add(record);
		
		System.out.printf("Successfully returned!\n\n%s", record);
	}
	
	public void reserveResource(long memberID, long resourceID) {
		Member member = Member.searchMemberByID(members, memberID);
		if (member == null) {
			System.out.printf("Member %s is not found.\n", memberID);
			return;
		}
				
		Resource item = Resource.searchByID(resources, resourceID);
		if (item == null) {
			System.out.printf("Item %s is not found.\n", resourceID);
			return;
		}
		
		if (item.getStatus() != ResourceStatus.AVAILABLE) {
			System.out.printf("Item %s is not available.\n", resourceID);
			return;
		}
		
		// 0 represents failure to reserve item
		if (member.reserve(item) == 0) {
			return;
		}
		
		item.updateStatus(ResourceStatus.RESERVED);
		
		Date dueDate = Member.calculateDueDate(SystemDate.getInstance().getDate(), TransactionType.RESERVE);

		Transaction record = new Transaction(TransactionType.RESERVE, item.getID(), member.getID(), dueDate);
		transactionRecords.add(record);

		System.out.printf("Successfully reserved!\n\n%s", record);
	}
	
	/**
	 * Handle renew book request
	 * @param memberID
	 * @param resourceID
	 */
	public void renewResource(long memberID, long resourceID) {
		Member member = Member.searchMemberByID(members, memberID);
		if (member == null) {
			System.out.printf("Member %s is not found.\n", memberID);
			return;
		}
				
		Resource item = Resource.searchByID(resources, resourceID);
		if (item == null) {
			System.out.printf("Item %s is not found.\n", resourceID);
			return;
		} else if (item.getStatus() != ResourceStatus.BORROWED) {
			System.out.printf("Item %s is not borrowed out.\n", resourceID);
			return;
		}
		
		Transaction borrowRecord = Transaction.getLatestTransaction(transactionRecords, item.getID());
		if (borrowRecord.getType() == TransactionType.BORROW) {
			if (!borrowRecord.isMatch(member.getID(), item.getID())) {				
				System.out.printf("Item %s is not borrowed by member %s.\n", resourceID, memberID);
				return;
			}
		} else {
			System.out.printf("Item %s is %s already.\n", resourceID, borrowRecord.getType().toString().toLowerCase());
			return;
		}
		
		if (borrowRecord.isExpired()) {
			System.out.printf("Item %s is expired. Please return it to the library.\n", resourceID);
			return;
		}
//		
//		if (!member.getBorrowedItems(item.getID())) {
//			System.out.printf("Renew item %s not allowed.", resourceID);
//			return;
//		}
		
		Date dueDate = Member.calculateDueDate(borrowRecord.getDueDate(), TransactionType.BORROW);
		Transaction record = new Transaction(TransactionType.RENEW, item.getID(), member.getID(), dueDate);
		transactionRecords.add(record);
		
		System.out.printf("Successfully renewed!\n\n%s", record);
	}
}
