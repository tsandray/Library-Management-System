package libCommand;
import library.Library;

public class CmdSearchBook extends Command{
    @Override
	public void execute(String[] cmdParts) {		
		if(cmdParts.length<2){
			System.out.print("Insufficient argument!\n");
			return;
		}
		
		String bookSearch = cmdParts[1];
		Library lib = Library.getInstance();
        lib.searchBooks(bookSearch);
    }
}