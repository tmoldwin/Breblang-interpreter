import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
public class Parser {
	LexAnalyzer t;

	public Parser(String fileName) {
		t = new LexAnalyzer(fileName);
		t.analyzeThat();
	}

	public Parser(boolean b, String s) {
		t = new LexAnalyzer(b,s);
		t.analyzeThat();
	}

	// <exp> --> <exp1> { “||” <exp1>}*
	public YTerm parseExp() {
		System.out.println("Parsing Exp");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp1();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("||")) {
			v.add(new YTerm(tk));
			tm1 = parseExp1();
			v.add(tm1);
//			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}		
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp, v);
		t.ungetToken();
		System.out.println("Finished Exp");
		return tm;
	}

	// <exp1>--> <exp2> { “&&” <exp2>}*
	public YTerm parseExp1() {
		System.out.println("Parsing Exp1");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp2();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("&&")) {
			v.add(new YTerm(tk));
			tm1 = parseExp2();
//			v.add(tm1);
			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp1, v);
		t.ungetToken();
		System.out.println("Finished Exp1");
		return tm;
	}

	// <exp2> --> <exp3> { “|” <exp3>}*
	public YTerm parseExp2() {
		System.out.println("Parsing Exp2");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp3();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("|")) {
			v.add(new YTerm(tk));
			tm1 = parseExp3();
			v.add(tm1);
			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp2, v);
		t.ungetToken();
		System.out.println("Finished Exp2");
		return tm;
	}

	// <exp3> --> <exp4> { “^” <exp4>}*
	public YTerm parseExp3() {
		System.out.println("Parsing Exp3");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp4();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("^")) {
			v.add(new YTerm(tk));
			tm1 = parseExp4();
			v.add(tm1);
//			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp3, v);
		t.ungetToken();
		System.out.println("Finished Exp3");
		return tm;
	}

	// <exp4> --> <exp5> { “&” <exp5>}*
	public YTerm parseExp4() {
		System.out.println("Parsing Exp4");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp5();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("&")) {
			v.add(new YTerm(tk));
			tm1 = parseExp5();
			v.add(tm1);
//			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp4, v);
		t.ungetToken();
		System.out.println("Finished Exp4");
		return tm;
	}

	// <exp5> --> “~ “<exp5> | <exp6> {(“==”|”!=”) <exp6>}*
	public YTerm parseExp5() {
		System.out.println("Parsing Exp5");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		Token tk = t.getToken();
		if (tk.getValue().equals("~")) {
			v.add(new YTerm(tk));
			v.add(parseExp5());
			return new YComplex(ExpName.Exp5,v);
		} else {
			t.ungetToken();
			YTerm tm;
			YTerm tm1;
			tm = parseExp6();
			v.add(tm);
			tk = t.getToken();
			String s = tk.getValue();
			while (s.equals("==") || s.equals("!=")) {
				v.add(new YTerm(tk));
				tm1 = parseExp6();
				v.add(tm1);
//				v.add(tm);
				tk = t.getToken();
				s = tk.getValue();
			}			
			tm = new YComplex(/*new Token(s),*/ ExpName.Exp5, v);
			t.ungetToken();
			System.out.println("Finished Exp5");
			return tm;
		}
		
	}

	// <exp6> --> <exp7> { <relop> <exp7>}*
	public YTerm parseExp6() {
		System.out.println("Parsing Exp6");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm;
		YTerm tm1;
		tm = parseExp7();
		v.add(tm);
		Token tk = t.getToken();
		String s = tk.getValue();
		while (s.equals("<") || s.equals(">") || s.equals(">=")
				|| s.equals("<=")) {
			v.add(new YTerm(tk));
			tm1 = parseExp7();
			v.add(tm1);
//			v.add(tm);
			tk = t.getToken();
			s = tk.getValue();
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp6, v);
		t.ungetToken();
		System.out.println("Finished Exp6");
		return tm;
	}

	// <exp7> --> <exp8> { <addop> <exp8>}*
	public YTerm parseExp7() {
		System.out.println("Parsing Exp7");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm; // first Exp8
		YTerm tm1; // second Exp8
		tm = parseExp8(); // returns first Exp8
		v.add(tm); // adds the first Exp8 to the list
		Token tk = t.getToken(); // get the next Token(this will be the
		// operator)
		String s = tk.getValue(); //
		while (s.equals("+") || s.equals("-")) { // while there continue to be
			// more +/-
			v.add(new YTerm(tk));
			tm1 = parseExp8(); // get the second
			v.add(tm1); // add this to the list of operands
//			v.add(tm); // this Ycomplex is added to the list
			tk = t.getToken(); // gets the next Token
			s = tk.getValue(); // gets the string value of the Token
		}			
		tm = new YComplex(/*new Token(s),*/ ExpName.Exp7, v);
		t.ungetToken(); // evidently, s was not a + or a -, so the token that we
		// just got is not part of Exp 7, so we need to backstep
		System.out.println("Finished Exp7");

		return tm; // we return tm, which is a Ycomplex of Exp8s separated by +
		// and -
	}

	// <exp8> --> <exp9> { <mulop> <exp9>}*
	public YTerm parseExp8() {
		System.out.println("Parsing Exp8");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm; // first Exp9
		YTerm tm1; // second Exp9
		tm = parseExp9(); // returns first Exp9		
		v.add(tm); // adds the first Exp9 to the list
		Token tk = t.getToken(); // get the next Token(this will be the
		// operator)
		String s = tk.getValue(); //
		while (s.equals("*")||s.equals("/")||s.equals("%")) { // while there continue to be more *
			v.add(new YTerm(tk));
			tm1 = parseExp9(); // get the second
			v.add(tm1); // add this to the list of operands			
//			v.add(tm); // this Ycomplex is added to the list		
			tk = t.getToken(); // gets the next Token
			s = tk.getValue(); // gets the string value of the Token
		}			
		tm = new YComplex(/* new Token(s), */ExpName.Exp8, v);
		t.ungetToken();
		System.out.println("Finished Exp8");
		return tm; // we return tm, which is a Ycomplex of Exp8s separated by *
	}

	// <exp9> --> “(“ <exp> “)” |INTEGER|IDENT|CONSTCH|<uarop> <exp9>
	public YTerm parseExp9() {
		System.out.println("Parsing Exp9");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		Token tk = t.getToken();			
//		System.out.println(tk.getValue());
		if (tk.getValue().equals("(")) {
			YComplex tm = (YComplex) parseExp();
			t.getToken();// move over the ")"
			System.out.println("Finished Exp9");
			return tm;
		} else if (Character.isDigit(tk.getValue().charAt(0))) {
			System.out.println("Finished Exp9");
			return new YTerm(tk, ExpName.Int);
		} else if (Character.isLetter(tk.getValue().charAt(0))) {
			System.out.println("Finished Exp9");
			return new YTerm(tk, ExpName.Ident);
		} else if (tk.getValue().substring(0, 0).equals("'")) {
			tk = t.getToken();
			t.getToken();
			System.out.println("Finished Exp9");
			return new YTerm(tk, ExpName.CONSTCH);
		} else if (tk.getValue().substring(0, 0).equals("+")
				|| tk.getValue().substring(0, 0).equals("-")) {
			v.add(new YTerm(tk));
			Token tk2 = t.getToken();
			v.add(new YTerm(tk2));
			System.out.println("Finished Exp9");
			return new YComplex(ExpName.UnaryTerm, v);
		}
		System.out.println("Finished Exp9");
		return null;

	}

	// <if> --> “if” “(" <exp> ")" <statement> ["else" <statement>]
	public YTerm parseIf() {
		System.out.println("Parsing if");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		Token tk = t.getToken();
		v.add(new YTerm(tk));
		t.getToken(); // skips the (
		v.add(parseExp());
		t.getToken(); // skips the )
		v.add(parseStatement());
		tk = t.getToken();
		if (!tk.getValue().equals("else")) {
			t.ungetToken();
		} else {
			v.add(new YTerm(tk));
			v.add(parseStatement());
		}
		System.out.println("Finished If");
		return new YComplex(ExpName.If, v);
	}

	// <cout> --> "cout" "<<" <listofexpressions>
	public YTerm parseCout() {
		System.out.println("Parsing Cout");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		v.add(new YTerm(t.getToken()));
		v.add(new YTerm(t.getToken()));
		v.add(parseListOfExpressions());
		System.out.println("Finished Cout");
		return new YComplex(ExpName.Cout, v);

	}

	// <cin> --> "cin" ">>" <listofvariables>
	public YTerm parseCin() {
		System.out.println("Parsing cin");

		ArrayList<YTerm> v = new ArrayList<YTerm>();
		v.add(new YTerm(t.getToken()));
		v.add(new YTerm(t.getToken()));
		v.add(parseListOfVariables());
		System.out.println("Finish cin");
		return new YComplex(ExpName.Cin, v);
	}

	// <program> --> [<listofdeclarations>] <listofstatements>
	public YTerm parseProgram() {
		System.out.println("Parsing program");
		ArrayList<YTerm> v=new ArrayList<YTerm>();
		Token tk=t.getToken();
		if((tk.getValue().equals("int")||tk.getValue().equals("char"))){
			t.ungetToken();
			v.add(parseListOfDeclarations());
		}
		v.add(parseListOfStatements());
		System.out.println("finish program");
		return new YComplex(ExpName.Program, v);
	}

	// <listofstatements> --> <statement> ";"
	// <listofstatements --> <statement> “;” <listofstatements>
	public YTerm parseListOfStatements() {
		System.out.println("Parsing LOS");
		ArrayList<YTerm> v=new ArrayList<YTerm>();
		do{
			Token tk=t.getToken();
			String s=tk.getValue();
		//	System.out.println(s);
		if(!s.equals("end")&&(s.equals("cin") || s.equals("cout") || s.equals("if") ||(!Token.IsReservedWord(s))|| s.equals("while") ||s.equals("{"))){
			t.ungetToken();
			v.add(parseStatement());
		}
			else break;
		}
		while(t.getToken().getValue().equals(";"));
		t.ungetToken();
		System.out.println("Finished LOS");
		return new YComplex(ExpName.ListOfStatements,v);

	}

	// <listofdeclarations> --> <declaration>
	// <listofdeclarations> --> <declaration> <listofdeclarations>
	public YTerm parseListOfDeclarations() {
		System.out.println("Parsing LOD");
		ArrayList<YTerm> v=new ArrayList<YTerm>();
		Token tk=t.getToken();
		while(tk.getValue().equals("int")||tk.getValue().equals("char")){
			t.ungetToken();
			v.add(parseDeclaration());
			tk=t.getToken();
		}
		t.ungetToken();
		System.out.println("Finished LOD");
		return new YComplex(ExpName.ListOfDeclarations,v);

	}

	// <declaration> --> <type> <listofvariables> “;”
	public YTerm parseDeclaration() {
		System.out.println("Parsing declaration");
		YComplex tm;
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		v.add(parseType());
		v.add(parseListOfVariables());
		t.getToken(); // gets the semicolon
		tm = new YComplex(null, ExpName.Declaration, v);
		System.out.println("Finished Declaration");
		return tm;
	}

	public YTerm parseType() {
		System.out.println("Parsing type");
		Token tk = t.getToken();
		System.out.println("Finished Type");
		return new YTerm(tk, ExpName.Type);
		// TODO Auto-generated method stub

	}

	// <listofexpressions>--><exp> |<exp>“,”<listofexpressions>
	public YTerm parseListOfExpressions() {
		System.out.println("Parsing LOE");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		do{
			v.add(parseExp());
		}
		while(t.getToken().getValue().equals(","));
		System.out.println("Finished LOE");
		t.ungetToken();
		return new YComplex(ExpName.ListOfExpressions, v);
	}
	
	public YTerm parseVariable() {
		System.out.println("Parsing variable");
		Token tk = t.getToken();
		System.out.println("Finished variable");
		return new YTerm(tk, ExpName.Variable);
	}

	// <statement> --> <cin> | <cout> | <if> | <assign> | <while> | “{“
	// <listofstatements> “}”
	public YTerm parseStatement() {
		System.out.println("Parsing Statement");
		Token tk = t.getToken();
		YTerm tm;
		if (tk.getValue().equals("cin")) {
			t.ungetToken();
			tm= parseCin();
		} else if (tk.getValue().equals("if")) {
			t.ungetToken();
			tm= parseIf();
		} else if (tk.getValue().equals("while")) {
			t.ungetToken();
			tm= parseWhile();
		} else if (tk.getValue().equals("{")) { // this parses the list of
			// statements
			YTerm los = parseListOfStatements();
			tm= los;			
			t.getToken(); // takes care of the }

		} else if (tk.getValue().equals("cout")) {
			t.ungetToken();
			tm= parseCout();			
		}
		else{
			t.ungetToken();
			tm=parseAssign();
		}
		System.out.println("Finished Statement");
		return tm;
	}

	// <assign> --> <listofvariables> "=" <exp>
	public YTerm parseAssign() {
		System.out.println("Parsing Assign");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		YTerm tm, tm1;
		tm = parseListOfVariables();
		v.add(tm);
		v.add(new YTerm(t.getToken())); // gets the = sign
		tm1 = parseExp();
		v.add(tm1);
		System.out.println("Finished assign");
		return new YComplex(/* new Token("="), */ExpName.Assign, v);
	}

	// <listofvariables>--><variable> “,”<listofvariables>
	// <listofvariables>--><variable>
	public YTerm parseListOfVariables() {
		System.out.println("Parsing List of Variables");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		do {
			v.add(parseVariable());
		} while (t.getToken().getValue().equals(","));
		t.ungetToken();		
		System.out.println("Finished List of Variables");
		return new YComplex(ExpName.ListOfVariables, v);

	}

	// <while> --> “while” “(“ <exp> “)” <statement>
	// [while][<exp>][<statement>]
	public YTerm parseWhile() {
		System.out.println("Parsing While");
		ArrayList<YTerm> v = new ArrayList<YTerm>();
		Token tk = t.getToken();
		v.add(new YTerm(tk));
		t.getToken(); // skips the (
		v.add(parseExp()); // this gets the condition
		t.getToken(); // skips the )
		v.add(parseStatement());
		System.out.println("Finished While");
		return new YComplex(ExpName.While, v);
	}


	public static void main(String[] args) {
		Parser p = new Parser(
				"C:\\Program Files\\eclipse-jee-galileo-SR1-win32\\tryit\\LexicalAnalyzer\\src\\test.txt");
		YTerm tm=p.parseProgram();
//		System.out.println(tm);
	}
}
