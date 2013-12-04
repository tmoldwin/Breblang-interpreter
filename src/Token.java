import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Token {
	static String[] f=	{"+","*","/","=","%","<",">","&","-","~",",","(","|","||",")","{","}",
		"<=","==","&&","<=","<<",">>","!=","if","cout","while","IDENT", "CONSTCH", "INT"};
	
	private String value;
	static LinkedList<String> TokenStrings = new LinkedList<String>(Arrays.asList(f));
	
	public Token(String s){
	value=s;
	}
	
	public static boolean IsReservedWord(String s){
		return TokenStrings.contains(s);
	}
	
	public static int getIndex(String s){
		if (TokenStrings.contains(s)) return TokenStrings.indexOf(s);
		else if (Character.isDigit(s.charAt(0))) return TokenStrings.indexOf("INT");
		else return TokenStrings.indexOf("IDENT");
			
	}
	
	public static int getLength(){
		return f.length;
	}
	
	public String getValue(){
		return value;
	}
	
	public String toString(){
		return value;
	}
}
