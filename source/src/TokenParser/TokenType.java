package TokenParser;

import java.util.HashMap;

public enum TokenType {
	// 保留字
	ORIGIN(0), SCALE(1), ROT(2), IS(3), TO(4), STEP(5), DRAW(6), FOR(7), FROM(8),
	// 变量
	T(9),
	// 分割符号
	SEMICO(10), L_BRACKET(11), R_BRACKET(12), COMMA(13),
	// 计算符号
	PLUS(14), MINUS(15), MUL(16), DIV(17), POWER(18),
	// 函数
	FUNC(19),
	// 常数
	CONST_ID(20),
	// 空记号
	NONTOKEN(21),
	// 错误符号
	ERRTOKEN(22);

	private static HashMap<Integer, String> map = new HashMap<Integer, String>();
	private int mTokenNumber;

	static {
		map.put(0, "ORIGIN");
		map.put(1, "SCALE");
		map.put(2, "ROT");
		map.put(3, "IS");
		map.put(4, "TO");
		map.put(5, "STEP");
		map.put(6, "DRAW");
		map.put(7, "FOR");
		map.put(8, "FRPM");
		map.put(9, "T");
		map.put(10, "SEMICO");
		map.put(11, "L_BRACKET");
		map.put(12, "R_BRACKET");
		map.put(13, "COMMA");
		map.put(14, "PLUS");
		map.put(15, "MINUS");
		map.put(16, "MUL");
		map.put(17, "DIV");
		map.put(18, "POWER");
		map.put(19, "FUNC");
		map.put(20, "CONST_ID");
		map.put(21, "NONTOKEN");
		map.put(22, "ERRTOKEN");

	}

	private TokenType(int tokenNumber) {
		mTokenNumber = tokenNumber;
	}

	@Override
	public String toString() {
		return map.get(mTokenNumber);
	}

}
