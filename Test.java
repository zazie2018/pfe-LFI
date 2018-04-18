package stanford;

import java.io.IOException;
import java.util.Scanner;

public class Test {
public static void main(String... s) throws IOException {
	PosTagger postagger =new PosTagger();
		Scanner sc = new Scanner(System.in);
		System.out.println("Taper le text à analyser");
	 String  testString = sc.nextLine();
		System.out.println("testString  " + testString);
		
		//String original = "what a smartphone !, it 's fantastic #thephoneofthecentury #lovely @samsung ,,, 1232,15 ";
	
		String taggedString = postagger.tagString(testString);
		System.out.println("tagged:\n" + taggedString);
		
		String simpleString = postagger.convertToSimpleTags(taggedString);
	//	System.out.println("simple-tagged:\n" + simpleString);
	}

}
