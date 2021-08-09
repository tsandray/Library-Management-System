package library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Resource {
	private static long nextID = 0;
	
	private long resourceID;
	private String name;
	private String publisher;
	private Double price;
	private long rackNo;
	private ResourceStatus status;
	
	public Resource(String n, String p, long rackNo, double price, ResourceStatus s){
		this.name = n;
		this.publisher = p;
		this.rackNo = rackNo;
		this.price = price;
		this.status = s;
		
		this.resourceID = nextID;
		incID();
	}
	
	private static void incID() { nextID++; }
	
	public static Resource searchByID(ArrayList<Resource> list, long id) {
		for (Resource item: list) {
			if (item.resourceID == id)
				return item;
		}
		
		return null;
	}
	
	// for testing purpose
	public static void resetNextID() { nextID = 0; }
	
	public long getID() { return resourceID; }
	public String getName() { return name; }
	public ResourceStatus getStatus() { return status; }
	
	/**
	 * Get brief information of the resource.
	 * @return string with format: name, publisher (id)
	 */
	public String getInfo() {
		return String.format("%s, %s (%d)", name, publisher, resourceID);
	}
	
	public String getPubDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: " + resourceID + "\n");
		sb.append("Name: " + name + "\n");
		sb.append("Publisher: " + publisher + "\n");
		sb.append("Rack no.: " + rackNo + "\n");
		sb.append("Status: " + status + "\n\n");
		
		return sb.toString();
	}

	public int updateStatus(ResourceStatus newStatus) {
		this.status = newStatus;
		return 1;
	}
}
