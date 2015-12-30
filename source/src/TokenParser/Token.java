package TokenParser;

public class Token {
	// �Ǻ����ö������֮һ
	private TokenType mType;
	// �Ǻŵ��ַ�����ʾ
	private String mName;
	// �Ǻŵ�ֵ��������¼�������ŵ�ֵ
	private double mValue;
	// �����Ǻ�������¼���ڵ�java������
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
