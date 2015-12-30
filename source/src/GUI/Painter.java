package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;

import SyntaxParser.ExpressionNode;

public class Painter {
	private static Painter instance;
	private double originX, originY, scaleX, scaleY, rot, start, end, step;
	private ExpressionNode xNode, yNode;
	private JFrame window;
	private JPanel panel;

	private Painter() {
		window = new JFrame("绘图板");
		panel = new JPanel();
		window.setLayout(new BorderLayout());
		window.add(panel,BorderLayout.CENTER);
		window.setSize(700, 700);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static Painter getInstance() {
		if (instance == null) {
			instance = new Painter();
		}
		return instance;
	}

	private void draw() {
		Graphics g = panel.getGraphics();
		double x, y,tempX,tempY;
		g.setColor(Color.BLACK);
		for (double i = start; i <= end; i += step) {
			x = SyntaxParser.Parser.getValue(xNode, i);
			y = SyntaxParser.Parser.getValue(yNode, i);
			//比例变换
			x*=scaleX;
			y*=scaleY;
			//旋转变换
			tempX =x;
			tempY =y;
			x=tempX*Math.cos(rot)+tempY*Math.sin(rot);
			y=tempY*Math.cos(rot)-tempX*Math.sin(rot);
			//平移处理
			x+=originX;
			y+=originY;
			//反向处理
			y= 700-y;
			System.out.println("draw (" + x + "," + y + ")");
			g.drawString("·", (int) x, (int) y);
		}
	}

	public void forDraw(double originX, double originY, double scaleX, double scaleY, double rot, double start,
			double end, double step, ExpressionNode xNod, ExpressionNode yNod) {
		this.originX = originX;
		this.originY = originY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.rot = rot;
		this.start = start;
		this.end = end;
		this.step = step;
		this.xNode = xNod;
		this.yNode = yNod;
		window.setVisible(true);
		draw();
	}

}
