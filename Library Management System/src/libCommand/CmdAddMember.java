package libCommand;

import library.Library;

public class CmdAddMember extends Command {

	/**
	 * It takes cmdParts with length of 3.
	 * <ol>
	 * <li>command</li>
	 * <li>name</li>
	 * <li>email</li>
	 * </ol>
	 * @param String[] cmdParts
	 */
	@Override
	public void execute(String[] cmdParts) {
		if (cmdParts.length < 3) {
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		String name = cmdParts[1];
		String email = cmdParts[2];

		Library.getInstance().addMember(name, email);
		
		// TODO: handle errors
		
	}

}
