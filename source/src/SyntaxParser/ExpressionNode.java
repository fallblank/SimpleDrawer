package SyntaxParser;

import TokenParser.Token;
import TokenParser.TokenType;
/**
 * ���������﷨����
 * mOpType��ʾ�����������ͣ�ʼ�գ�Ϊ��ʱ�ǳ��������
 * mLeftNode,mRightNode��ʾ��������������
 * 
 * 
 * @author fallb
 *
 */
public class ExpressionNode {
	//����������
	public TokenType mType;
	
	//������
	public ExpressionNode mLeftNode,mRightNode;
	
	//����
	public ExpressionNode mChildNode;
	public String mFuncName;
	
	//����
	public double mConstValue;
	
	/**
	 * �����ڵ�Ĺ��캯��
	 * @param type ���������
	 * @param constValue ����ֵ
	 */
	public ExpressionNode(TokenType type,double constValue){
		this.mType = type;
		this.mConstValue = constValue;
	}
	
	/**
	 * �����ڵ�Ĺ��캯��
	 * @param type ֻ��һ��ֵ��TokenType.T
	 */
	public ExpressionNode(TokenType type) {
		this.mType = type;
	}
	
	/**
	 * �������ڵ�Ĺ��캯��
	 * @param type �ڵ�����
	 * @param left ��ڵ�
	 * @param right �ҽڵ�
	 */
	public ExpressionNode(TokenType type,ExpressionNode left,ExpressionNode right) {
		this.mType = type;
		this.mLeftNode = left;
		this.mRightNode = right;
	}
	
	/**
	 * �����ڵ�Ĺ��캯��
	 * @param type �ڵ�����
	 * @param funcName ��������
	 * @param child ��������
	 */
	public ExpressionNode(TokenType type,String funcName,ExpressionNode child){
		this.mType = type;
		this.mFuncName = funcName;
		this.mChildNode = child;
	}
	
	
	//get������
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
