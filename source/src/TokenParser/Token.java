package TokenParser;

public class Token {
	// 记号类别，枚举类型之一
	private TokenType mType;
	// 记号的字符串表示
	private String mName;
	// 记号的值，用来记录常量符号的值
	private double mValue;
	// 函数记号用来记录对于的java函数名
	private String mPtr;
	
	private int mLineNum;

	public Token(TokenType type, String name, double value, String func,int line) {
		this.mType = type;
		this.mName = name;
		this.mValue = value;
		this.mPtr = func;
		this.mLineNum = line;
	}
	
	public Token(TokenType type, String name, double value, String func) {
		this.mType = type;
		this.mName = name;
		this.mValue = value;
		this.mPtr = func;
	}
	
	
	

	public void setLineNum(int lineNum) {
		this.mLineNum = lineNum;
	}

	public int getLineNum() {
		return this.mLineNum;
	}



	public TokenType getType() {
		return this.mType;
	}



	public void setType(TokenType type) {
		this.mType = type;
	}



	public String getName() {
		return this.mName;
	}



	public void setName(String name) {
		this.mName = name;
	}



	public double getValue() {
		return this.mValue;
	}



	public void setValue(double value) {
		this.mValue = value;
	}



	public String getPtr() {
		return this.mPtr;
	}



	public void setPtr(String ptr) {
		this.mPtr = ptr;
	}



	@Override
	public String toString() {
		return String.format("%-20s%-20s%-20.2f%-20s", mType, mName, mValue, mPtr);
	}

}
