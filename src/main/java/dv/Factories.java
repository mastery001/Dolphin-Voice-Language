package dv;

import dv.constExpr.DefaultExprFactory;
import dv.constExpr.ExprFactory;
import dv.toJava.PrinterJavaFunction;

  
/**  
 *@Description:  dv中所需要用到的一些工厂类
 *@Author:zouziwen
 *@Since:2016年6月13日
 *@Version:1.1.0  
 */
public class Factories {

	/**  
	 * @return  
	 * @Description:  表达式工厂
	 */
	public ExprFactory exprFactory(){
		return new DefaultExprFactory();
	}
	
	/**  
	 * @return  
	 * @Description:  编译时用到的参数解析
	 */
	public Arguments arguments() {
		return new Arguments();
	} // arguments

	/**  
	 * @return  
	 * @Description:  解释器工厂
	 */
	public InterpreterFactory interpreterFactory() {
		return new InterpreterFactory();
	}
	
	/**  
	 * @return  
	 * @Description:  解释器执行上下文
	 */
	public ExecutionContext executionContext() {
		ExecutionContext bootstrap = ExecutionContext.newContext();
		bootstrap.addFunction(new PrinterJavaFunction());
		return bootstrap;
	}
	
	/**  
	 * @return  
	 * @Description:  dv语言的所有关键字，在实现中尚未解析
	 */
	public String[] languageKeywords() {
		return keywords;
	} // languageKeywords

	static String[] keywords = { 
			"break",  "continue",  "do", 
			"else",   "false",     "final", 
			"for",    "goto",       "if", 
			"null",   "private",    "instanceof",
			"this",   "protected",  "public", 
			"static", "super",  "return", 
			"true",   "while" };
}
