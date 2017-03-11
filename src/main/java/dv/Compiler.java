package dv;

import java.io.IOException;
import java.util.Enumeration;

import dv.constExpr.DefaultExprFactory;
import dv.constExpr.ExprFactory;
import dv.entry.ModuleEntry;
import dv.interpreter.ModuleInterpreter;
import dv.toJava.PrinterJavaFunction;

public class Compiler {

	public Compiler() {
	}

	public void start(String[] args) {

		try {
			init(args);
			parse();
			//Printer.print(parser.module.contained());
			interprete();
		} catch (IOException e) {
			System.err.println(e);
		} catch (InvalidArgument e) {
			System.err.println(e);
		}

	}

	protected Enumeration<ModuleEntry> parse() throws IOException {
		parser.parse(arguments.file);
		emitList = parser.emitList.elements();
		return emitList;
	}

	private void interprete() {
		if (emitList != null) {

			bootstrap = ExecutionContext.newContext(moduleInterpreter.chain());
			bootstrap.addFunction(new PrinterJavaFunction());
			
			while (emitList.hasMoreElements()) {
				ModuleEntry entry = (ModuleEntry) emitList.nextElement();
				moduleInterpreter.interprete(moduleInterpreter.chain(), entry, bootstrap);
			}
		}
	}

	private void init(String[] args) throws InvalidArgument {
		initFactories();
		arguments.parseArgs(args);
		initInterpreter();
		parser = new Parser(arguments, exprFactory);
	}

	private void initInterpreter() {
		moduleInterpreter = interpreterFactory.createModuleInterpreter();
	}

	private void initFactories() {
		factories = factories();
		if (factories == null)
			factories = new Factories();

		Arguments tmpArgs = factories.arguments();
		if (tmpArgs == null)
			arguments = new Arguments();
		else
			arguments = tmpArgs;

		InterpreterFactory tmpInterF = factories.interpreterFactory();
		if (tmpInterF == null)
			interpreterFactory = new InterpreterFactory();
		else
			interpreterFactory = tmpInterF;

		ExprFactory tmpExprF = factories.exprFactory();
		if (tmpExprF == null)
			exprFactory = new DefaultExprFactory();
		else
			exprFactory = tmpExprF;
	}

	protected Factories factories() {
		return new Factories();
	}

	public Arguments arguments = null;

	private Factories factories = null;

	private Parser parser = null;

	private Enumeration<ModuleEntry> emitList = null;

	private InterpreterFactory interpreterFactory = null;

	private ExprFactory exprFactory = null;

	private ModuleInterpreter moduleInterpreter = null;

	private ExecutionContext bootstrap = null;

}
