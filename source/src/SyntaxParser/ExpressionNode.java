package SyntaxParser;

import TokenParser.Token;
import TokenParser.TokenType;
/**
 * 操作符的语法树，
 * mOpType表示操作符的类型，始终，为空时是常数或变量
 * mLeftNode,mRightNode表示操作符的两个数
 * 
 * 
 * @author fallb
 *
 */
public class ExpressionNode {
	//操作符类型
	public TokenType mType;
	
	//操作符
	public ExpressionNode mLeftNode,mRightNode;
	
	//函数
	public ExpressionNode mChildNode;
	public String mFuncName;
	
	//常数
	public double mConstValue;
	
	/**
	 * 常量节点的构造函数
	 * @param type 操作符类别
	 * @param constValue 常量值
	 */
	public ExpressionNode(TokenType type,double constValue){
		this.mType = type;
		this.mConstValue = constValue;
	}
	
	/**
	 * 变量节点的构造函数
	 * @param type 只有一个值，TokenType.T
	 */
	public ExpressionNode(TokenType type) {
		this.mType = type;
	}
	
	/**
	 * 操作符节点的构造函数
	 * @param type 节点类型
	 * @param left 左节点
	 * @param right 右节点
	 */
	public ExpressionNode(TokenType type,ExpressionNode left,ExpressionNode right) {
		this.mType = type;
		this.mLeftNode = left;
		this.mRightNode = right;
	}
	
	/**
	 * 函数节点的构造函数
	 * @param type 节点类型
	 * @param funcName 函数名字
	 * @param child 函数参数
	 */
	public ExpressionNode(TokenType type,String funcName,ExpressionNode child){
		this.mType = type;
		this.mFuncName = funcName;
		this.mChildNode = child;
	}
	
	
	//get方法族
	public TokenType getType() {
		return this.mType;
	}

	public ExpressionNode getLeftNode() {
		return this.mLeftNode;
	}

	public ExpressionNode getRightNode() {
		return this.mRightNode;
	}

	public ExpressionNode getChildNode() {
		return this.mChildNode;
	}

	public String getFuncName() {
		return this.mFuncName;
	}

	public double getConstValue() {
		return this.mConstValue;
	}
}
