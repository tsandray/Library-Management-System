package library;

import java.util.Calendar;
import java.util.Date;

public class SystemDate {
	private static SystemDate instance = new SystemDate();
	private Date date = new Date();
	
	public static SystemDate getInstance() { return instance; }
	
	private SystemDate() {}
	
	/**
	 * Add <code>numDay</code> to the current system date.
	 * The <code>numDay</code> can be either positive or negative.
	 * @param numDay
	 */
	public void addDate(int numDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, numDay);
		
		date = c.getTime();
	}
	
	// TODO: to be reviewed...
	public Date getDate() { 
//		return (Date) date.clone(); 
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		return c.getTime();
	}
	
	public void setDate(Date newDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(newDate);
		date = c.getTime();
	}
}
