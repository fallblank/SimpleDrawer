package fallblank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import GUI.Painter;
import TokenParser.Token;

public class Main {

	public static void main(String[] args) throws IOException {
		Painter paiter = Painter.getInstance();
		File source = new File("C:/Users/fallb/Desktop/CompileHomework/data/source.txt");
		File dest = new File("C:/Users/fallb/Desktop/CompileHomework/output/");
		TokenParser.Parser tokenParser = new TokenParser.Parser(source);
		tokenParser.setDestFile(dest);
		tokenParser.parse();
		ArrayList<Token> tokens = tokenParser.getTokenList();
		SyntaxParser.Parser syntaxParser = new SyntaxParser.Parser(tokens);
		syntaxParser.parse();
	}
}
