import java.util.ArrayList;
import java.util.Stack;


	public class YComplex extends YTerm {
		private ArrayList<YTerm> terms;
//This constructor is used when there are a list of terms, each separated by the same operator(e.g. <exp6>{+<exp6>}
		
		public YComplex(Token Oper, ExpName type, ArrayList<YTerm> v) {
			super(Oper, type);
			terms = v;
		}
//This constructor is used when there 
		public YComplex(ExpName exp, ArrayList<YTerm> v) {
			super(exp);
			terms = v;
		}
		public String toString(){
			return getTk().toString();
		}
/*		public String toString(){
			String s="";
			Stack<YTerm> stack=new Stack<YTerm>();
			if (!isYComplex()) s+=getTk();
			else for(YTerm y:terms){
				if(!(y.getTk()==null)) s+=y.getTk();
			while(!y.isYComplex()){
				s+=y.toString();
			}
			}
					
			for(YTerm y : terms){
				if(!y.isYComplex())
					s+=y.getTk();
				else while(!y.y.getTk()!=null){
				s+=y.getTk();
				}
			else if(s!=null)s+=y.toString();
			}
			return s;
		}
*/		
		public boolean isYComplex(){
			return true;
		}
		
		public ArrayList<YTerm> getChildren(){
			return terms;
		}
		public YTerm getChild(int i){
			return terms.get(i);
		}

	}