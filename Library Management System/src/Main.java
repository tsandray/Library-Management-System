import java.util.Scanner;

import libCommand.CommandHandler;

public class Main {

	public static void main(String[] args) {		
		Scanner in = new Scanner(System.in);
		
		String input = "";
		while (true) {
			System.out.print("Please enter command: ");
			input = in.nextLine();
			if (input.equalsIgnoreCase("quit"))
				break;
			
			CommandHandler.handle(input);
		}
		
		System.out.println("Terminate system.");
		in.close();
	}

}
