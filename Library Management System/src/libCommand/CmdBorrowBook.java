package libCommand;

import library.Library;

public class CmdBorrowBook extends Command {

	/**
	 * It takes cmdParts with length of 3.
	 * <ol>
	 * <li>command "borrow"</li>
	 * <li>memberID</li>
	 * <li>itemID</li>
	 * </ol>
	 * @param String[] cmdParts
	 */
	@Override
	public void execute(String[] cmdParts) {
		// TODO Auto-generated method stub
		// cmd: borrow|memberID|itemID
		
		if (cmdParts.length < 3) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		try {
			long mID = Long.parseLong(cmdParts[1]);
			long rID = Long.parseLong(cmdParts[2]);
			Library.getInstance().lendResource(mID, rID);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}

}
