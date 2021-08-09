package testcase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import libCommand.CmdAnalyzeStat;
import libCommand.CmdBorrowBook;
import library.Library;
import library.Member;
import library.Resource;
import library.Transaction;

public class CmdAnalyzeTest {
	PrintStream oldPrintStream;
	ByteArrayOutputStream bos;

	private void setOutput() throws Exception {
		oldPrintStream = System.out;
		bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));
	}

	private String getOutput() { // throws Exception
		System.setOut(oldPrintStream);
		return bos.toString();
	}
	
	@BeforeEach
	public void resetEnv() {
		Library.reset();
		Member.resetNextID();
		Resource.resetNextID();
		Transaction.resetNextID();
	}
	
	/* Test case 1: Test analyze with 3 cmdParts
	 * Precondition: 
	 * - Resources in library: []
	 * - Members in library: []
	 * Input: [search book, 0]
	 */
	@Test
	public void testAnalyzeStat1() throws Exception {
		setOutput();
		String[] input = {"analyze", "0"};
		new CmdAnalyzeStat().execute(input);;
		
		String expected = "Item | Frequency\n" + 
				"----------------\n" + 
				"Recommended items:\n" + 
				"========\n" + 
				"Statistics:\n\n";
		
		assertEquals(expected, getOutput());
	}
	
	// Test case 2: Test analyze with 2 (i.e. insufficient arguments) cmdParts
	@Test
	public void testAnalyzeStat2() throws Exception {
		setOutput();
		String[] input = {"analyze"};
		new CmdAnalyzeStat().execute(input);
		
		assertEquals("Insufficient argument!\n", getOutput());
	}	
	
	// Test case 3: Test analyze with invalid argument
	// Input: ["analyze", "0", "-"]
	@Test
	public void testAnalyzeStat3() throws Exception {
		setOutput();
		String[] input = {"analyze","-"};
		new CmdAnalyzeStat().execute(input);
        
		String expected = "Invalid argument!\n\n";
		assertEquals(expected, getOutput());
	} 
}
