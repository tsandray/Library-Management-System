package libCommand;

import library.Library;

public class CmdAnalyzeStat extends Command{

	@Override
	public void execute(String[] cmdParts) {
		if (cmdParts.length < 2) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		try {
			int numDay = Integer.parseInt(cmdParts[1]);
			String res = Library.getInstance().analyzeStat(numDay);
			System.out.print(res);
		} catch (Exception e) {
			System.out.print("Invalid argument!\n\n");
		}
	}
}
