package TokenParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

/**
 * 1.出来掉源程序中的全部空格符号 2.依次读取记号，判断符号类别 3.生成词法分析结果.
 * 
 * @author fallb
 *
 */

public class Parser {
	private static final char NEW_LINE = '\n';

	private int mLineNumber;
	private File mSourceFile;
	private File mDestFile;
	private StringBuffer mTokenBuffer;
	private ArrayList<Token> mTokenList;
	private InputStream mIn;
	private OutputStream mOut;
	private ArrayList<ErrorRecord> mErrorRecords;

	public Parser(File source) {
		this.mSourceFile = source;
	}

	public Parser(File source, File dest) {
		this.mSourceFile = source;
		this.mDestFile = dest;
	}

	/**
	 * 初始化文件输入流，记号缓冲区，记号分析列表
	 */
	private void init() {
		mLineNumber = 1;
		mTokenBuffer = new StringBuffer();
		mTokenList = new ArrayList<Token>();
		mCharStack = new Stack<Character>();
		mErrorRecords = new ArrayList<ErrorRecord>();
		try {
			mIn = new FileInputStream(mSourceFile);
			if (mDestFile == null) {
				File parent = mSourceFile.getParentFile();
				String name = mSourceFile.getName();
				mDestFile = new File(parent, name + ".token");
				mDestFile.createNewFile();
			}
			if(mDestFile.isDirectory()){
				String name = mSourceFile.getName();
				String[] spliteArray = name.split(".");
				mDestFile = new File(mDestFile, name + ".token");
				mDestFile.createNewFile();
			}
			mOut = new FileOutputStream(mDestFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Token> parse() throws IOException {
		init();
		Token token;
		do {
			token = getToken();
			mTokenList.add(token);
		} while (token.getType() != TokenType.NONTOKEN);
		writeResult();
		return mTokenList;
	}

	private void writeResult() {
		PrintStream out = System.out;
		System.setOut(new PrintStream(mOut));
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 80; i++)
			buffer.append('-');
		String line = buffer.toString();
		String headTitle = String.format("%-20s%-20s%-20s%-20s", "TokenType", "TokenName", "TokenValue",
				"TokenFunction");
		System.out.println(headTitle);
		System.out.println(line);
		for (Token token : mTokenList) {
			System.out.println("" + token);
		}
		System.out.println(line);
		int errorSize = mErrorRecords.size();
		String tips = String.format("Token parse finished,totally found:%d errors", errorSize);
		System.out.println(tips);
		if (errorSize != 0) {
			System.out.println(line);
			String errorHead = String.format("%-20s%-20s", "LineNumber","UnidentifiedSymbol");
			System.out.println(errorHead);
			for (ErrorRecord record : mErrorRecords)
				System.out.println("" + record);
		}
		System.setOut(out);
		return;
	}

	private Stack<Character> mCharStack;

	/**
	 * 
	 * @return 解析的下一个字符，该字符可以来自源文件的输入流，或者上次解析回退的字符栈
	 * @throws IOException
	 *             文件读取错误时抛出
	 */
	private int getNextChar() throws IOException {
		if (mCharStack.isEmpty())
			return mIn.read();
		else
			return mCharStack.pop();
	}

	/** 
	 * 回退字符
	 * 
	 * @param c
	 *            会退的字符
	 */
	private void rollbaceChar(int c) {
		mCharStack.push((char) c);
	}

	/**
	 * 将记号存入buffer中
	 * 
	 * @param c
	 *            记号中的字符
	 */
	private void appendBuffer(int c) {
		char ch = (char) c;
		mTokenBuffer.append(ch);
		return;
	}

	private String getBufferString() {
		return mTokenBuffer.toString();
	}

	private void clearBuffer() {
		mTokenBuffer.delete(0, mTokenBuffer.length());
		mTokenBuffer.setLength(0);
		return;
	}

	/**
	 * 解析输入流，返回就下来的一个记号
	 * 
	 * @return 当前解析点后紧跟着的记号
	 * @throws IOException
	 *             当输入文件流打不开时抛出
	 * 
	 */
	private Token getToken() throws IOException {
		Token token;
		int nextChar;
		clearBuffer();
		while (true) {
			nextChar = getNextChar();
			// 解析到文件结束
			if (nextChar == -1) {
				token = new Token(TokenType.NONTOKEN, "EOF", 0.0, null,mLineNumber);
				token.setLineNum(mLineNumber);
				return token;
			}
			// 换行符加行计数变量
			// 注意天坑：java的isSpaceChar不把\r,\n解析为空格符号
			if (nextChar == '\r')
				continue;
			if (nextChar == NEW_LINE) {
				mLineNumber++;
				continue;
			}
			// 过滤空格
			if (!Character.isSpaceChar(nextChar))
				break;
		}

		appendBuffer(nextChar);

		// 判断字符是不是字母，如果是那就时函数、保留字、或常量P\E
		if (Character.isAlphabetic(nextChar)) {
			while (true) {
				nextChar = getNextChar();
				if (Character.isAlphabetic(nextChar))
					appendBuffer(nextChar);
				else
					break;
			}
			rollbaceChar(nextChar);
			String tokenName = getBufferString();
			token = TokenTable.judgeKeyToken(tokenName);
			if (token == null) {
				token = new Token(TokenType.ERRTOKEN, tokenName, 0.0, null,mLineNumber);
				mErrorRecords.add(new ErrorRecord(tokenName, mLineNumber));
			}
			token.setLineNum(mLineNumber);
			return token;
		} else if (Character.isDigit(nextChar)) {
			while (true) {
				nextChar = getNextChar();
				if (Character.isDigit(nextChar))
					appendBuffer(nextChar);
				else
					break;
			}
			if (nextChar == '.') {
				appendBuffer(nextChar);
				while (true) {
					nextChar = getNextChar();
					if (Character.isDigit(nextChar))
						appendBuffer(nextChar);
					else
						break;
				}
			}
			rollbaceChar(nextChar);
			String tokenValue = getBufferString();
			double value = Double.parseDouble(tokenValue);
			token = new Token(TokenType.CONST_ID, "digit", value, null,mLineNumber);
			return token;

		} else {
			switch (nextChar) {
			case ';':
				token = new Token(TokenType.SEMICO, ";", 0.0, null,mLineNumber);
				break;
			case '(':
				token = new Token(TokenType.L_BRACKET, "(", 0.0, null,mLineNumber);
				break;
			case ')':
				token = new Token(TokenType.R_BRACKET, ")", 0.0,  null,mLineNumber);
				break;
			case ',':
				token = new Token(TokenType.COMMA, ",", 0.0,  null,mLineNumber);
				break;
			case '+':
				token = new Token(TokenType.PLUS, "+", 0.0,  null,mLineNumber);
				break;
			case '-':
				nextChar = getNextChar();
				if (nextChar == '-') {
					// 清除注释
					while (nextChar != NEW_LINE && nextChar != -1){
						if(nextChar==NEW_LINE){
							mLineNumber++;
						}
						nextChar = getNextChar();	
					}
					rollbaceChar(nextChar);
					return getToken();
				} else {
					rollbaceChar(nextChar);
					token = new Token(TokenType.MINUS, "-", 0.0,  null,mLineNumber);
					break;
				}
			case '/':
				nextChar = getNextChar();
				if (nextChar == '/') {
					// 清除注释
					while (nextChar != NEW_LINE && nextChar != -1){
						if(nextChar==NEW_LINE){
							mLineNumber++;
						}
						nextChar = getNextChar();
					}
					rollbaceChar(nextChar);
					return getToken();
				} else {
					rollbaceChar(nextChar);
					token = new Token(TokenType.DIV, "/", 0.0,  null,mLineNumber);
					break;
				}
			case '*':
				nextChar = getNextChar();
				if (nextChar == '*') {
					token = new Token(TokenType.POWER, "**", 0.0,  null,mLineNumber);
					break;
				} else {
					rollbaceChar(nextChar);
					token = new Token(TokenType.MUL, "*", 0.0,  null,mLineNumber);
					break;
				}
			default:
				token = new Token(TokenType.ERRTOKEN, getBufferString(), 0.0,  null,mLineNumber);
				mErrorRecords.add(new ErrorRecord(getBufferString(), mLineNumber+1));
				break;
			}
			return token;
		}
	}

	public File getSourceFile() {
		return this.mSourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.mSourceFile = sourceFile;
	}

	public File getDestFile() {
		return this.mDestFile;
	}

	public void setDestFile(File destFile) {
		this.mDestFile = destFile;
	}

	public ArrayList<Token> getTokenList() {
		return this.mTokenList;
	}

}
