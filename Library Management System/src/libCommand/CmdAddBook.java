package libCommand;

import library.Library;
import library.Resource;
import library.ResourceStatus;

public class CmdAddBook extends Command{
	
	/**
	 * It takes cmdParts with length of 5.
	 * <ol>
	 * <li>command "add book"</li>
	 * <li>name</li>
	 * <li>publisher</li>
	 * <li>rackNo</li>
	 * <li>price</li>
	 * </ol>
	 * @param String[] cmdParts
	 */
	@Override
	public void execute(String[] cmdParts) {
		// TODO Auto-generated method stub
		
		if(cmdParts.length<5){
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		String name = cmdParts[1];
		String publisher = cmdParts[2];
		try {
			long rackNo = Long.parseLong(cmdParts[3]);
			double price = Double.parseDouble(cmdParts[4]);

			ResourceStatus status = ResourceStatus.AVAILABLE;
			
			Library lib = Library.getInstance();
			
			Resource r = lib.addResource(name, publisher, rackNo, price, status);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}
	
}
