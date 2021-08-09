package libCommand;

import library.Library;

public class CmdReturnBook extends Command {

	/**
	 * It takes cmdParts with length of 3.
	 * <ol>
	 * <li>command "return book"</li>
	 * <li>memberID</li>
	 * <li>itemID</li>
	 * </ol>
	 * @param String[] cmdParts
	 */
	@Override
	public void execute(String[] cmdParts) {
    	// Return|memberID|ResourceID
		
		if (cmdParts.length < 3) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		try {			
			long mID = Long.parseLong(cmdParts[1]);
			long rID = Long.parseLong(cmdParts[2]);
			Library.getInstance(). takeBackResource(mID, rID);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}
}