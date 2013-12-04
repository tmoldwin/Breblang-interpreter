
	public class YTerm {
		private Token tk;
		private ExpName en;
		public YTerm(Token t, ExpName type) {
			tk = t;
			en=type;
		}
		public Token getTk() {
			return tk;
		}
		public void setTk(Token tk) {
			this.tk = tk;
		}
		public YTerm(Token tm) {
			tk=tm;
		}
		public YTerm(ExpName type){
			en=type;
		}
		public ExpName getType(){
			return en;
		}
		public String toString(){
			return tk.toString();
		}
		public boolean isYComplex(){
			return false;
		}
	}
