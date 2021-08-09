package libCommand;

import library.Library;

public class CmdReserveBook extends Command{

	@Override
	public void execute(String[] cmdParts) {
		// cmd: borrow|memberID|itemID
		
		if (cmdParts.length < 3) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		try {			
			long mID = Long.parseLong(cmdParts[1]);
			long rID = Long.parseLong(cmdParts[2]);
			Library.getInstance().reserveResource(mID, rID);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}
    
} 
