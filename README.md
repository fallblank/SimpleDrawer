# SimpleDrawer
This is a simple interpreter for a simple drawing language.</br>
##Screenshot

##Drawing Language Description
<br>This is a loop drawing language.There is 5 type statements,as following:
<br>1.ORIGIN IS (X,Y)
<br>It sets the origin of the graph which your want to draw.
<br>2.ROT IS Z
<br>It set the degree that you want your graph ratating.The variable Z is radian,such as PI/2,PI.
<br>3.SACALE IS (X,Y)
<br>It set the scaling of your graph.
<br>4.FOR T FROM start TO end STEP step DRAW(x,y)
<br>It is the core statement of this language.It can drow point circularly,Which equals belowe Java statement:
<br>for(double T=start;T &lt end;T+=step){
	//other code for get variable x and y
	draw(x,y);
<br>} 
##Project Directory Description
<br>1.data---there is drawing language source code.
<br>2.output---there is output file directory,which reports tokens of source.
<br>3.souce---including source code of the project.
<br>4.Compiler.jar---the compiled jar file that can execute.
<br>5.run.cmd---cmd script.
