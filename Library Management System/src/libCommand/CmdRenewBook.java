package libCommand;

import library.Library;

public class CmdRenewBook extends Command{
	
	@Override
	public void execute(String[] cmdParts) {
		// cmd: renew book|memberID|itemID
		
		if (cmdParts.length < 3) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		try {
			long mID = Long.parseLong(cmdParts[1]);
			long rID = Long.parseLong(cmdParts[2]);
			Library.getInstance().renewResource(mID, rID);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}
}
