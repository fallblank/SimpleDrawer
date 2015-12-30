package SyntaxParser;

import java.util.ArrayList;

import GUI.Painter;
import TokenParser.Token;
import TokenParser.TokenType;

public class Parser {
	// 词法分析获得的记号流
	private ArrayList<Token> mTokenList;
	private ArrayList<String> mErrorMeg = new ArrayList<String>();
	private double originX = 0, originY = 0;
	private double scaleX = 1, scaleY = 1;
	private double rot = 0;
	private double start, end, step;
	private Token token;

	public Parser(ArrayList<Token> tokenList) {
		mTokenList = tokenList;
	}

	static int tokenIndex = 0;

	private void getToken() {
		token = mTokenList.get(tokenIndex);
		if (token.getType() == TokenType.ERRTOKEN)
			syntaxError(1, token);
		tokenIndex++;
	}

	// 处理错误信息
	private void syntaxError(int key, Token token) {
		switch (key) {
		case 1:
			String errorToken = String.format("%d,Error token:\"%s\"", token.getLineNum(), token.getName());
			mErrorMeg.add(errorToken);
			break;
		case 2:
			String unexpectedToken = String.format("%d,unexpected token:\"%s\"", token.getLineNum(), token.getName());
			mErrorMeg.add(unexpectedToken);
		default:
			break;
		}
	}

	// 匹配记号
	private void matchToken(TokenType type) {
		if (token.getType() != type)
			syntaxError(2, token);
		getToken();
	}

	// 句子分析程序入口
	public void parse() {
		tokenIndex = 0;
		getToken();
		while (token.getType() != TokenType.NONTOKEN) {
			statement();
			matchToken(TokenType.SEMICO);
		}
	}

	private void statement() {
		switch (token.getType()) {
		case ORIGIN:
			originStatement();
			break;
		case SCALE:
			scaleStatement();
			break;
		case ROT:
			rotStatement();
			break;
		case FOR:
			forStatement();
			break;
		default:
			syntaxError(2, token);
			break;
		}
	}

	private void forStatement() {
		ExpressionNode startNod, endNod, stepNod, xNod, yNod;
		matchToken(TokenType.FOR);
		matchToken(TokenType.T);
		matchToken(TokenType.FROM);
		startNod = expression();
		start = getValue(startNod, 0);
		startNod = null;
		matchToken(TokenType.TO);
		endNod = expression();
		end = getValue(endNod, 0);
		endNod = null;
		matchToken(TokenType.STEP);
		stepNod = expression();
		step = getValue(stepNod, 0);
		stepNod = null;
		matchToken(TokenType.DRAW);
		matchToken(TokenType.L_BRACKET);
		xNod = expression();
		// 获取x坐标
		matchToken(TokenType.COMMA);
		yNod = expression();
		// 获取y坐标
		matchToken(TokenType.R_BRACKET);
		Painter painter = Painter.getInstance();
		painter.forDraw(originX, originY, scaleX, scaleY, rot, start, end, step, xNod, yNod);
	}

	private void rotStatement() {
		ExpressionNode tmp;
		matchToken(TokenType.ROT);
		matchToken(TokenType.IS);
		tmp = expression();
		rot = getValue(tmp, 0);
		tmp = null;
		return;
	}

	private void scaleStatement() {
		ExpressionNode tmp;

		matchToken(TokenType.SCALE);
		matchToken(TokenType.IS);
		matchToken(TokenType.L_BRACKET);
		tmp = expression();
		scaleX = getValue(tmp, 0);
		tmp = null;
		matchToken(TokenType.COMMA);
		tmp = expression();
		scaleY = getValue(tmp, 0);
		tmp = null;
		matchToken(TokenType.R_BRACKET);
		return;
	}

	private void originStatement() {
		ExpressionNode tmp;

		matchToken(TokenType.ORIGIN);
		matchToken(TokenType.IS);
		matchToken(TokenType.L_BRACKET);
		tmp = expression();
		originX = getValue(tmp, 0);
		tmp = null;
		matchToken(TokenType.COMMA);
		tmp = expression();
		originY = getValue(tmp, 0);
		tmp = null;
		matchToken(TokenType.R_BRACKET);
		return;
	}

	private ExpressionNode expression() {
		ExpressionNode left, right;
		TokenType tokenType;

		left = term();
		while (token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS) {
			tokenType = token.getType();
			matchToken(tokenType);
			right = term();
			left = new ExpressionNode(tokenType, left, right);
		}
		return left;
	}

	private ExpressionNode term() {
		ExpressionNode left, right;
		TokenType tokenTmp;
		left = factor();
		while (token.getType() == TokenType.MUL || token.getType() == TokenType.DIV) {
			tokenTmp = token.getType();
			matchToken(token.getType());
			right = factor();
			left = new ExpressionNode(tokenTmp, left, right);
		}
		return left;
	}

	private ExpressionNode factor() {
		ExpressionNode left, right;
		if (token.getType() == TokenType.PLUS) {
			matchToken(TokenType.PLUS);
			right = factor();
		} else if (token.getType() == TokenType.MINUS) {
			matchToken(TokenType.MINUS);
			right = factor();
			left = new ExpressionNode(TokenType.CONST_ID, 0.0);
			right = new ExpressionNode(TokenType.MINUS, left, right);
		} else {
			right = component();
		}
		return right;
	}

	private ExpressionNode component() {
		ExpressionNode left, right;
		left = atom();
		if (token.getType() == TokenType.POWER) {
			matchToken(TokenType.POWER);
			right = component();
			left = new ExpressionNode(TokenType.POWER, left, right);
		}
		return left;
	}

	private ExpressionNode atom() {
		ExpressionNode address = null, tmp = null;
		switch (token.getType()) {
		case CONST_ID:
			address = new ExpressionNode(token.getType(), token.getValue());
			matchToken(TokenType.CONST_ID);
			break;
		case T:
			address = new ExpressionNode(token.getType());
			matchToken(TokenType.T);
			break;
		case FUNC:
			matchToken(TokenType.FUNC);
			String funcName = token.getName();
			matchToken(TokenType.L_BRACKET);
			tmp = expression();
			address = new ExpressionNode(TokenType.FUNC, funcName, tmp);
			matchToken(TokenType.R_BRACKET);
			break;
		case L_BRACKET:
			matchToken(TokenType.L_BRACKET);
			address = expression();
			matchToken(TokenType.R_BRACKET);
			break;
		default:
			syntaxError(2, token);
		}
		return address;
	}

	public static double getValue(ExpressionNode nod, double t) {
		if (nod == null) {
			return 0;
		}
		switch (nod.mType) {
		case PLUS:
			return getValue(nod.mLeftNode, t) + getValue(nod.mRightNode, t);
		case MINUS:
			return getValue(nod.mLeftNode, t) - getValue(nod.mRightNode, t);
		case MUL:
			return getValue(nod.mLeftNode, t) * getValue(nod.mRightNode, t);
		case DIV:
			return getValue(nod.mLeftNode, t) / getValue(nod.mRightNode, t);
		case POWER:
			return Math.pow(getValue(nod.mChildNode, t), getValue(nod.mLeftNode, t));
		case FUNC:
			if (nod.mFuncName == "SIN")
				return Math.sin(getValue(nod.mChildNode, t));
			else if (nod.mFuncName == "COS")
				return Math.cos(getValue(nod.mChildNode, t));
			else if (nod.mFuncName == "TAN")
				return Math.tan(getValue(nod.mChildNode, t));
			else if (nod.mFuncName == "LN")
				return Math.log(getValue(nod.mChildNode, t));
			else if (nod.mFuncName == "SQRT")
				return Math.sqrt(getValue(nod.mChildNode, t));
		case CONST_ID:
			return nod.mConstValue;
		case T:
			return t;
		default:
			return 0;
		}
	}
}