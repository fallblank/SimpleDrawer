# SimpleDrawer
This is a simple interpreter for a simple drawing language.
##Screenshot

##Drawing Language Description
This is a loop drawing language.There is 5 type statements,as following:
1.ORIGIN IS (X,Y)
It sets the origin of the graph which your want to draw.
2.ROT IS Z
It set the degree that you want your graph ratating.The variable Z is radian,such as PI/2,PI.
3.SACALE IS (X,Y)
It set the scaling of your graph.
4.FOR T FROM start TO end STEP step DRAW(x,y)
It is the core statement of this language.It can drow point circularly,Which equals belowe Java statement:
for(double T = start;T<end;T+=step){
	/*other code for get variable x and y*/
	draw(x,y);
} 
##Project Directory Description
1.data---there is drawing language source code.
2.output---there is output file directory,which reports tokens of source.
3.souce---including source code of the project.
4.Compiler.jar---the compiled jar file that can execute.
5.run.cmd---cmd script.
