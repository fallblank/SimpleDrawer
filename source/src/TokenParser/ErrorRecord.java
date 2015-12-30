package TokenParser;

public class ErrorRecord {
	private String mTokenName;
	private int mLineNumber;
	
	public ErrorRecord(String name,int line){
		this.mTokenName =name;
		this.mLineNumber = line;
	}

	@Override
	public String toString() {
		return String.format("%-20d%-20s", mLineNumber,mTokenName);
	}
	
	

}
