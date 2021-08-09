package library;

public interface MemberStatus {
	//TODO
	public String borrow(Member mb);
	
	public String reserve(Member mb);
	public String returnResource(Member mb);
}
