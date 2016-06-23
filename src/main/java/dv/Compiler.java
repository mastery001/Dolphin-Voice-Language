package dv;

import java.io.IOException;
import java.util.Enumeration;

import dv.constExpr.DefaultExprFactory;
import dv.constExpr.ExprFactory;
import dv.entry.ModuleEntry;
import dv.interpreter.ModuleInterpreter;

public class Compiler {

	public Compiler() {
	}

	public void start(String[] args) {

		try {
			init(args);
			parse();
			interprete();
//			Printer.print(parser.module.contained());
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
			while (emitList.hasMoreElements()) {
				ModuleEntry entry = (ModuleEntry) emitList.nextElement();
				moduleInterpreter.interprete(moduleInterpreter.chain(), entry, context);
			}
		}
	}

	private void init(String[] args) throws InvalidArgument {
		initFactories();
		arguments.parseArgs(args);
		initInterpreter();
		parser = new Parser(arguments, exprFactory, keywords);
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

		ExecutionContext tmpContext = factories.executionContext();
		if (tmpContext == null)
			context = ExecutionContext.newContext();
		else
			context = tmpContext;

		// Get the language keywords.
		keywords = factories.languageKeywords();
		if (keywords == null)
			keywords = new String[0];
	}

	protected Factories factories() {
		return new Factories();
	}

	public Arguments arguments = null;

	private Factories factories = null;

	private String[] keywords = null;

	private Parser parser = null;

	private Enumeration<ModuleEntry> emitList = null;

	private InterpreterFactory interpreterFactory = null;

	private ExprFactory exprFactory = null;

	private ModuleInterpreter moduleInterpreter = null;

	private ExecutionContext context = null;

}
