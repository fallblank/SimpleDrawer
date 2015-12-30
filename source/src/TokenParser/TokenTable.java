package TokenParser;

/**
 * 这是一个静态类，无法构造对象，主要是用来生成符号表。 符号表采用Token数组存储。
 * 
 * @author fallb
 *
 */
public class TokenTable {
	private TokenTable() {
	}

	//符号表
	public final static Token[] TOKEN_TABLE = new Token[] { new Token(TokenType.CONST_ID, "PI", 31415926, null),
			new Token(TokenType.CONST_ID, "E", 2.71828, null), new Token(TokenType.T, "T", 0.0, null),
			new Token(TokenType.FUNC, "SIN", 0.0, "sin"), new Token(TokenType.FUNC, "COS", 0.0, "cos"),
			new Token(TokenType.FUNC, "TAN", 0.0, "tan"), new Token(TokenType.FUNC, "LN", 0.0, "ln"),
			new Token(TokenType.FUNC, "EXP", 0.0, "exp"), new Token(TokenType.FUNC, "SQRT", 0.0, "sqrt"),
			new Token(TokenType.ORIGIN, "ORIGIN", 0.0, null), new Token(TokenType.IS, "IS", 0.0, null),
			new Token(TokenType.FOR, "FOR", 0.0, null), new Token(TokenType.FROM, "FROM", 0.0, null),
			new Token(TokenType.TO, "TO", 0.0, null), new Token(TokenType.STEP, "STEP", 0.0, null),
			new Token(TokenType.DRAW, "DRAW", 0.0, null),new Token(TokenType.SCALE,"SCALE",0.0,null),
			new Token(TokenType.ROT,"ROT",0.0,null)

	};
	
	/**
	 * 判断符号是不是预定义的，如果是返回预定义的符号
	 * @param name 需要判断的符号名字
	 * @return 符号
	 */
	public static Token judgeKeyToken(String name){
		String upper = name.toUpperCase();
		for (Token token : TOKEN_TABLE) {
			if(token.getName().equals(upper))
				return token;
		}
		return null;
	}

}
