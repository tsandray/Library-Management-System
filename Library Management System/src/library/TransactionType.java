package library;

public enum TransactionType {
	BORROW,
	RETURN,
	RENEW,
    RESERVE;
	
	public String toString() {
		String lc = this.name().toLowerCase();
		return lc.substring(0, 1).toUpperCase() + lc.substring(1);
	}
}
