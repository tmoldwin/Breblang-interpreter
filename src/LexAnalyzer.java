import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class LexAnalyzer {
	private LinkedList<String> lexemes;
	private char[] theFile;
	private String lexeme;
	private int currentIndex;
	//Stores the index of the list lexemes when tokenizing
	private int tokenizingIndex;

	public LexAnalyzer(String fileName) {
		readIt(fileName);
		currentIndex = 0;
		tokenizingIndex=0;
		lexemes = new LinkedList<String>();
		
	}
	
	public LexAnalyzer(boolean b, String s){ //this constructor is for reading strings
		theFile = s.toCharArray();
		currentIndex = 0;
		tokenizingIndex=0;
		lexemes = new LinkedList<String>();
	}

	public void readIt(String fileName) {
		String s = "";
		Scanner reader;
		try {
			reader = new Scanner(new File(fileName));
			while (reader.hasNextLine()) {
				s += '\n' + reader.nextLine();
			}
			s=s.trim();
			theFile = s.toCharArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void analyzeThat() {
		lexeme = "";
		while (currentIndex < theFile.length) {
			if (Character.isDigit(theFile[currentIndex])) {
				while (Character.isDigit(theFile[currentIndex])) {
					addIt();
					if (currentIndex + 1 > theFile.length) {
						lexemes.add(lexeme);
						return;
					} else {
						if (!updateIt())
							return;
					}

				}
				lexemes.add(lexeme);
				lexeme = "";

			} else if (Character.isLetter(theFile[currentIndex])) {
				while (Character.isLetterOrDigit(theFile[currentIndex])) {
					addIt();
					if (!updateIt())
						return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '+'
					|| theFile[currentIndex] == '*'
					|| theFile[currentIndex] == '/'
					|| theFile[currentIndex] == '-'
					|| theFile[currentIndex] == '%'
					|| theFile[currentIndex] == '{'
					|| theFile[currentIndex] == '}'
					|| theFile[currentIndex] == '('
					|| theFile[currentIndex] == ')'
					|| theFile[currentIndex] == ';'
					|| theFile[currentIndex] == '{'
					|| theFile[currentIndex] == '}'
					|| theFile[currentIndex] == ','
					|| theFile[currentIndex] == '^') {
				addIt();
				lexemes.add(lexeme);
				lexeme = "";
				if (!updateIt())
					return;
			} else if (theFile[currentIndex] == '&') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '&') {
					addIt();
					if(!updateIt()) return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '|') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '|') {
					addIt();
					if(!updateIt()) return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '=') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '=') {
					addIt();
					if (!updateIt())
						return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '<') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '=') {
					addIt();
					if (!updateIt())
						return;
				}
				else if (theFile[currentIndex] == '<') {
					addIt();
					if (!updateIt())
						return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '>') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '=') {
					addIt();
					if (!updateIt())
						return;
				}
				else if (theFile[currentIndex] == '>') {
					addIt();
					if (!updateIt())
						return;
				}
				lexemes.add(lexeme);
				lexeme = "";
			} else if (theFile[currentIndex] == '!') {
				addIt();
				if (!updateIt())
					return;
				if (theFile[currentIndex] == '=') {
					addIt();
					if (!updateIt())
						return;
				}
				lexemes.add(lexeme);
				lexeme = "";

			} else if (!updateIt())
				return;
		}
	}

	public boolean updateIt() {
		if (currentIndex + 1 == theFile.length) {
			lexemes.add(lexeme);
			return false;
		} else {
			currentIndex++;
			return true;
		}
	}
	public void addIt(){
		lexeme += theFile[currentIndex];
	}
	
	public String lex(int i){
		return lexemes.get(i);		
	}
	
	public Token getToken(){
		if(!(tokenizingIndex==lexemes.size()-1)){
		Token tk=new Token(lexemes.get(tokenizingIndex));
		tokenizingIndex++;
		return tk;
		}
		return new Token("end");
	}
	
	public void ungetToken(){
		tokenizingIndex--;
	}

	public static void main(String[] args) {
		LexAnalyzer lexy = new LexAnalyzer("C:\\Program Files\\eclipse-jee-galileo-SR1-win32\\tryit\\LexicalAnalyzer\\src\\test.txt");
		lexy.analyzeThat();
		for (String s : lexy.lexemes) {
			System.out.println("Next Token is " + s);
		}
	}
}
