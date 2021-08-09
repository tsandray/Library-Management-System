package library;

public class ActivatedStatus implements MemberStatus {

	@Override
	public String borrow(Member mb) {
		return null;
	}

	@Override
	public String returnResource(Member mb) {
		return null;
	}

	@Override
	public String reserve(Member mb) {
		return null;
	}
	
	@Override
	public String toString() { return "Activated"; }
}
