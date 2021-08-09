package library;

public enum ResourceStatus {
	BORROWED,
	AVAILABLE,
	RESERVED;
	
	public String toString() {
		String lc = this.name().toLowerCase();
		return lc.substring(0, 1).toUpperCase() + lc.substring(1);
	}
}
