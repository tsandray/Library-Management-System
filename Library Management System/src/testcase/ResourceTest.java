package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.Resource;
import library.ResourceStatus;

//# test case = 2
public class ResourceTest {
	@BeforeEach
	public void resetEnv() {
		Resource.resetNextID();
	}
	
	// Test case 1: search existing id
	@Test
	public void testSearchByID1() {
		ArrayList<Resource> list = new ArrayList<>();
		list.add(new Resource("Name1", "Publisher1", 1, 0.0, ResourceStatus.AVAILABLE));
		list.add(new Resource("Name2", "Publisher2", 1, 0.0, ResourceStatus.AVAILABLE));
		
		Resource item = Resource.searchByID(list, 0);
		assertEquals( "Name1, Publisher1 (0)", item.getInfo());
	}
	
	// Test case 2: search id not existing
	@Test
	public void testSearchByID2() {
		ArrayList<Resource> list = new ArrayList<>();
		list.add(new Resource("Name1", "Publisher1", 1, 0.0, ResourceStatus.AVAILABLE));
		list.add(new Resource("Name2", "Publisher2", 1, 0.0, ResourceStatus.AVAILABLE));
		
		Resource item = Resource.searchByID(list, 3);
		assertEquals(null, item);
	}
}
