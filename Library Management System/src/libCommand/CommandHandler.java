package libCommand;

public class CommandHandler {
	public static void handle(String input) {
		String[] cmdParts = input.split("\\|");
		
		boolean needHelpMsg = false;
		
		String cmd = cmdParts[0];
		if (cmd.equalsIgnoreCase("borrow")) {
			new CmdBorrowBook().execute(cmdParts);;
		} else if (cmd.equalsIgnoreCase("add book")) {
		    new CmdAddBook().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("add member")) {
		    new CmdAddMember().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("return book")) {
		    new CmdReturnBook().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("search book")) {
		    new CmdSearchBook().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("reserve book")){
			new CmdReserveBook().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("renew book")){
			new CmdRenewBook().execute(cmdParts);
		} else if (cmd.equalsIgnoreCase("analyze")) {
			new CmdAnalyzeStat().execute(cmdParts);
		} else {
			needHelpMsg = true;
		}
		
		if (needHelpMsg) {
			StringBuilder sb = new StringBuilder();
			sb.append("Invalid command. Command usage: \n");
			sb.append("borrow|memberID|itemID\n");
			sb.append("return book|memberID|itemID\n");
			sb.append("add book|name|publisher|rackNo|price\n");
			sb.append("add member|name|email\n");
			sb.append("search book|search string\n");
			sb.append("reserve book|memberID|itemID\n");
			sb.append("renew book|memberID|itemID\n");
			sb.append("analyze|numDay\n");
			sb.append("\n");
			
			System.out.print(sb.toString());
		}
	}
}
