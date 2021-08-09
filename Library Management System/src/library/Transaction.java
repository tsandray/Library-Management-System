package library;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Transaction {
	private static long nextID = 0;
	
	private long transactionID;
	private Date issueDate;
	private Date dueDate;
	private long resourceID;
	private long memberID;
	private TransactionType type;
	
	public Transaction(TransactionType type, long resrcID, long mbID, Date dueDate) {
		this.type = type;
		this.resourceID = resrcID;
		this.memberID = mbID;
		this.dueDate = dueDate;
		this.issueDate = SystemDate.getInstance().getDate();
		this.transactionID = nextID;
		
		incrementID();
	}
	
	/**
	 * Filter transactions which are issued after the input date
	 * @param transaction list
	 * @param date
	 * @return filtered list of transactions
	 */
	public static ArrayList<Transaction> filterListAfterDate(ArrayList<Transaction> transactions, Date date) {
		ArrayList<Transaction> result = new ArrayList<>();
		
		for (Transaction t: transactions) {
			if (!t.issueDate.before(date)) {
				result.add(t);
			}
		}
		
		return result;
	}
	
	/**
	 * Retrieve the last issued transaction with the input rID
	 * @param transaction list
	 * @param resourceID
	 * @return the latest transaction with input resourceID
	 */
	public static Transaction getLatestTransaction(ArrayList<Transaction> transactions, long rID) {
		if (transactions.size() == 0) {
			return null;
		}
		
		Transaction result = null;
		
		for (Transaction t: transactions) {
			if (t.resourceID == rID) {
				if (result == null) {
					result = t;
				} else {
					if (result.issueDate.compareTo(t.issueDate) <= 0) {						
						result = t;
					}
				}
			}
		}
		
		return result;
	}
	
	public static void resetNextID() { nextID = 0; }

	public String getInfo() { 
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		return String.format("%s Record -- issue date: %s, memberID: %d, resourceID: %d", type, sdf.format(issueDate), memberID, resourceID);
	}
	public TransactionType getType() { return type; }
	public long getResourceID() { return resourceID; }
	
	public boolean isExpired() {
		if (dueDate != null) {
			return dueDate.compareTo(SystemDate.getInstance().getDate()) < 0;
		}
		
		return false;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public boolean isMatch(long mID, long rID) { return mID == memberID && rID == resourceID; }
	
	public String toString() {
		// TODO: test
		Library lib = Library.getInstance();
		Resource item = lib.searchResourceByID(resourceID);
		Member mb = lib.searchMemberByID(memberID);
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		
		StringBuilder sb = new StringBuilder();
		sb.append(type + " Record\n---------\n");
		sb.append("TransactionID: " + transactionID + "\n");
		sb.append("Issue date: " + sdf.format(issueDate) + "\n");
		sb.append("Borrower: " + mb.getInfo() + "\n");
		sb.append("Item: " + item.getInfo() + "\n");
		if (type != TransactionType.RETURN) {			
			sb.append("Due date: " + sdf.format(dueDate) + "\n");
		}
		
		sb.append("\n");
		return sb.toString();
	}
	
	private void incrementID() { nextID++; }
}
