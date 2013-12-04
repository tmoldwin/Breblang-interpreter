import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Toviah
 * An interpreter for the language specified in the EBNF file.
 */
public class Interpreter {
	private Parser myParser;
	private YComplex program;
	private HashMap<String, ExpName> varMap; // the first index in the
	// varlist will be the
	// type
	private HashMap<String, Integer> assignMap;
	private ArrayList<YTerm> statements;

	public Interpreter(String s) {
		myParser = new Parser(s);
		program = (YComplex) myParser.parseProgram();
		statements = new ArrayList<YTerm>();
		assignMap = new HashMap<String, Integer>();
		varMap = new HashMap<String, ExpName>(); // maps the type to the list of
		// variables
	}

	// returns 1 if the program contains variables, 0 otherwise.
	public int storeVariables() {
		if (program.getChildren().get(0).getType() == ExpName.ListOfDeclarations) {
			YComplex LOD = (YComplex) program.getChildren().get(0); // this is
			// the list
			// of
			// declarations
			for (YTerm y : LOD.getChildren()) { // for each declaration
				ArrayList<String> varsList = new ArrayList<String>();
				YComplex variables = (YComplex) ((YComplex) y).getChildren()
						.get(1); // this is the list of variables
				for (YTerm v : variables.getChildren()) {
					varsList.add(v.toString());
				}
				for (String s : varsList)
					varMap.put(s, (((YComplex) y).getChildren().get(0)
							.getType())// gets the type
							); // this is the arraylist of idents
				System.out.println(varMap.keySet().size());
			}
			return 1;
		} else
			return 0;
	}

	public void storeStatements(int i) { // stores the statements-takes the
		// value returned by store variables
		// as a parameter.
		statements = ((YComplex) program.getChildren().get(i)) // list of
				// statements
				.getChildren(); // the individual statements
	}

	public void storeAssignment(YComplex y) { // this takes a YTerm of Type
		// assignment as a paramater
		int i = evaluateExp((YComplex) y.getChildren().get(2));
		for (YTerm c : ((YComplex) y.getChildren().get(0)).getChildren())
			assignMap.put(c.toString(), i);

	}

	private int evaluateExp(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp1((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			String oper = "";
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp1(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp1(tm1);
				if (oper.equals("||")) {
					if (total >= 1 || newValue >= 1)
						return 1;
					else
						total = 0;
					i++;
				}
			}
			return 0;
		}
	}

	private int evaluateExp1(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp2((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			String oper = "";
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp2(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp2(tm1);
				if (oper.equals("&&")) {
					if (total >= 1 && newValue >= 1)
						total = 1;
				}
				i++;
			}
			return 0;
		}
	}

	// this negates stuff.
	private int evaluateExp2(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp3((YComplex) exp.getChildren().get(0));
		} else {
			boolean negate = false;
			if (exp.getChildren().get(0).getTk().getValue().equals("!")) {
				negate = true;
				if (exp.getChildren().size() == 2) {
					if (evaluateExp3((YComplex) exp.getChildren().get(1)) == 0)
						return 1;
					else
						return 0;
				}
			}
			int total;
			int i = 1;
			if (negate)
				i = 2;
			YComplex tm = (YComplex) exp.getChildren().get(i - 1);
			total = evaluateExp3(tm);
			while (i + 1 < exp.getChildren().size()) {
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp3(tm1);
				total = total | newValue;
				i++;
			}
			return total;
		}
	}

	private int evaluateExp3(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp4((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp4(tm);
			while (i + 1 < exp.getChildren().size()) {
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp4(tm1);
				total = total ^ newValue;
				i++;
			}
			return total;
		}
	}

	private int evaluateExp4(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp5((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp5(tm);
			while (i + 1 < exp.getChildren().size()) {
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp5(tm1);
				total = total & newValue;
				i++;
			}
			return total;
		}
	}

	private int evaluateExp5(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp6((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			boolean isTilda = false;
			if (exp.getChildren().get(0).getTk() != null
					&& exp.getChildren().get(0).getTk().getValue().equals("~")) {
				isTilda = true;
				if (exp.getChildren().size() == 1) {
					return ~evaluateExp6((YComplex) exp.getChildren().get(0));
				}
			}
			if (isTilda)
				i = 2;
			String oper = "";
			YComplex tm = (YComplex) exp.getChildren().get(i - 1);
			total = evaluateExp6(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp6(tm1);
				if (oper.equals("==")) {
					if (total == newValue) {
						total = (isTilda) ? 0 : 1;
					} else
						total = (isTilda) ? 1 : 0;
					i++;
				} else if (oper.equals("!=")) {
					if (total != newValue) {
						return (isTilda) ? 0 : 1;
					} else
						total = (isTilda) ? 1 : 0;
					i++;
				}
			}
			return total;
		}
	}

	private int evaluateExp6(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp7((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			String oper = "";
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp7(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp7(tm1);
				if (oper.equals("<=")) {
					if (total <= newValue)
						total = 1;
					else total = 0;
					i++;
				}
				if (oper.equals(">=")) {
					if (total >= newValue)
						total = 1;
					else total = 0;
					i++;
				}
				if (oper.equals(">")) {
					if (total > newValue)
						total = 1;
					else total = 0;
					i++;
				}
				if (oper.equals("<")) {
					if (total < newValue)
						total = 1;
					else total = 0;
					i++;
				}
			}
			return total;
		}
	}

	private int evaluateExp7(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp8((YComplex) exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			String oper = "";
			YComplex tm = (YComplex) exp.getChildren().get(0);
			total = evaluateExp8(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YComplex tm1 = (YComplex) exp.getChildren().get(i);
				int newValue = evaluateExp8(tm1);
				if (oper.equals("+")) {
					total = total + newValue;
					i++;
				}
				if (oper.equals("-")) {
					total = total - newValue;
					i++;
				}
			}
			return total;
		}

	}

	private int evaluateExp8(YComplex exp) {
		if (exp.getChildren().size() == 1) {
			return evaluateExp9(exp.getChildren().get(0));
		} else {
			int total;
			int i = 1;
			String oper = "";
			YTerm tm = exp.getChildren().get(0);
			total = evaluateExp9(tm);
			while (i + 1 < exp.getChildren().size()) {
				oper = exp.getChildren().get(i).getTk().getValue();
				i++;
				YTerm tm1 = exp.getChildren().get(i);
				int newValue = evaluateExp9(tm1);
				if (oper.equals("*")) {
					total = total * newValue;
					i++;
				}
				if (oper.equals("/")) {
					total = total / newValue;
					i++;
				}
				if (oper.equals("%")) {
					total = total % newValue;
					i++;
				}

			}
			return total;
		}

	}

	private int evaluateExp9(YTerm exp) {
		if (exp.getType() == ExpName.Exp) {
			return evaluateExp((YComplex) exp);
		}
		if (exp.getType() == ExpName.Int)
			return Integer.parseInt(exp.getTk().getValue());
		if (exp.getType() == ExpName.UnaryTerm) {
			if (((YComplex) exp).getChildren().get(0).getTk().equals("-"))
				return 0 - evaluateExp9(exp);
			else
				return evaluateExp9(exp);

		}
		if (exp.getType() == ExpName.Ident) {
			return assignMap.get(exp.getTk().getValue());
		}
		if (exp.getType() == ExpName.CONSTCH)
			return Character.getNumericValue(exp.getTk().getValue().charAt(0));
		return 0;

	}

	private void evaluateCout(YComplex c) {
		ArrayList<YTerm> list = ((YComplex) c.getChild(2)).getChildren();
		System.out.println();
		for (int i=0; i<list.size(); i++) {
			String s=""+evaluateExp((YComplex) list.get(i));
			if(i!=list.size()-1){
			s+= ",";
			}
			System.out.print(s);
		}
	}

	private void evaluateIf(YComplex exp) {
		YComplex condition = (YComplex) exp.getChild(1);
		YTerm statement = exp.getChild(2);
		if (evaluateExp(condition) == 1) {
			evaluateStatement(statement);
//			System.out.println("true");
		} else if (exp.getChildren().size() == 5) {
			YTerm elseStatement = exp.getChild(4);
			evaluateStatement(elseStatement);
		}
	}

	private void evaluateWhile(YComplex exp) {
		YComplex condition = (YComplex) exp.getChild(1);
		YTerm statement = exp.getChild(2);
		while(evaluateExp(condition) == 1) {
			evaluateStatement(statement);
		}
	}

	private void EvaluateCin(YComplex cin) {
		Scanner reader = new Scanner(System.in);
		ArrayList<YTerm> list = evaluateVariables((YComplex) cin.getChild(2));
		for (YTerm y : list) {
			System.out.println("Enter a value for " + y.getTk().getValue());
			int s = reader.nextInt();
			assignMap.put(y.getTk().getValue(), s);
		}
	}

	private ArrayList<YTerm> evaluateVariables(YComplex vars) {
		return vars.getChildren();
	}

	public HashMap<String, ExpName> getVarList() {
		for (String y : varMap.keySet()) {
		}

		return varMap;

	}

	public HashMap<String, Integer> getVariableMap() {
		return assignMap;
	}

	public ArrayList<YTerm> getStatements() {
		return statements;
	}

	public void runIt() {
		int i = storeVariables();
		storeStatements(i);
		for (YTerm y : statements) {
			evaluateStatement(y);
		}
	}

	public void evaluateStatement(YTerm y) {
		if (y.getType() == ExpName.Assign)
			storeAssignment((YComplex) y);
		if (y.getType() == ExpName.Cin)
			EvaluateCin((YComplex) y);
		if (y.getType() == ExpName.Cout)
			evaluateCout((YComplex) y);
		if (y.getType() == ExpName.If)
			evaluateIf((YComplex) y);
		if (y.getType() == ExpName.While)
			evaluateWhile((YComplex) y);
		if(y.getType()==ExpName.ListOfStatements)
			evaluateListOfStatements((YComplex) y);
	}

	private void evaluateListOfStatements(YComplex exp) {
		for(YTerm y:exp.getChildren()){
			evaluateStatement(y);
		}
	}

	public static void main(String[] args) {
		Interpreter inter = new Interpreter("test.txt");
		inter.runIt();
	}
}
