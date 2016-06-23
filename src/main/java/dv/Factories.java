package dv;

import dv.constExpr.DefaultExprFactory;
import dv.constExpr.ExprFactory;
import dv.toJava.PrinterJavaFunction;

public class Factories {

	public ExprFactory exprFactory(){
		return new DefaultExprFactory();
	}
	
	public Arguments arguments() {
		return new Arguments();
	} // arguments

	public InterpreterFactory interpreterFactory() {
		return new InterpreterFactory();
	}
	
	public ExecutionContext executionContext() {
		ExecutionContext bootstrap = ExecutionContext.newContext();
		bootstrap.addFunction(new PrinterJavaFunction());
		return bootstrap;
	}
	
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
