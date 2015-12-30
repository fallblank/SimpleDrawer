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
 * 1.������Դ�����е�ȫ���ո���� 2.���ζ�ȡ�Ǻţ��жϷ������ 3.���ɴʷ��������.
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
	 * ��ʼ���ļ����������ǺŻ��������Ǻŷ����б�
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
	 * @return ��������һ���ַ������ַ���������Դ�ļ����������������ϴν������˵��ַ�ջ
	 * @throws IOException
	 *             �ļ���ȡ����ʱ�׳�
	 */
	private int getNextChar() throws IOException {
		if (mCharStack.isEmpty())
			return mIn.read();
		else
			return mCharStack.pop();
	}

	/** 
	 * �����ַ�
	 * 
	 * @param c
	 *            ���˵��ַ�
	 */
	private void rollbaceChar(int c) {
		mCharStack.push((char) c);
	}

	/**
	 * ���ǺŴ���buffer��
	 * 
	 * @param c
	 *            �Ǻ��е��ַ�
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
	 * ���������������ؾ�������һ���Ǻ�
	 * 
	 * @return ��ǰ�����������ŵļǺ�
	 * @throws IOException
	 *             �������ļ����򲻿�ʱ�׳�
	 * 
	 */
	private Token getToken() throws IOException {
		Token token;
		int nextChar;
		clearBuffer();
		while (true) {
			nextChar = getNextChar();
			// �������ļ�����
			if (nextChar == -1) {
				token = new Token(TokenType.NONTOKEN, "EOF", 0.0, null,mLineNumber);
				token.setLineNum(mLineNumber);
				return token;
			}
			// ���з����м�������
			// ע����ӣ�java��isSpaceChar����\r,\n����Ϊ�ո����
			if (nextChar == '\r')
				continue;
			if (nextChar == NEW_LINE) {
				mLineNumber++;
				continue;
			}
			// ���˿ո�
			if (!Character.isSpaceChar(nextChar))
				break;
		}

		appendBuffer(nextChar);

		// �ж��ַ��ǲ�����ĸ��������Ǿ�ʱ�����������֡�����P\E
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
					// ���ע��
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
					// ���ע��
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
