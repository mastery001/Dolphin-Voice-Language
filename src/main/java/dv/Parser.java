package dv;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Vector;

import dv.constExpr.And;
import dv.constExpr.BinaryExpr;
import dv.constExpr.Divide;
import dv.constExpr.EvaluationException;
import dv.constExpr.ExprFactory;
import dv.constExpr.Expression;
import dv.constExpr.Minus;
import dv.constExpr.Modulo;
import dv.constExpr.Negative;
import dv.constExpr.Not;
import dv.constExpr.Or;
import dv.constExpr.Plus;
import dv.constExpr.Positive;
import dv.constExpr.ShiftLeft;
import dv.constExpr.ShiftRight;
import dv.constExpr.Times;
import dv.constExpr.Xor;
import dv.entry.FunctionEntry;
import dv.entry.IncludeEntry;
import dv.entry.JoinEntry;
import dv.entry.ModuleEntry;
import dv.entry.SymtabEntry;
import dv.entry.ValueEntry;
import dv.entry.VariableEntry;

/**
 * 语法分析器
 * 
 * @author zouziwen
 *
 *         2016年6月15日 下午7:57:24
 */
class Parser {

	Arguments arguments;

	private String[] keywords;

	Token token = null;

	private TokenBuffer tokenHistory = new TokenBuffer();

	ModuleEntry module;

	Scanner scanner = null;

	Vector<ModuleEntry> emitList = new Vector<ModuleEntry>();

	private ExprFactory exprFactory = null;

	Parser(Arguments arguments, ExprFactory exprFactory, String[] keywords) {
		this.arguments = arguments;
		this.keywords = keywords;
		this.exprFactory = exprFactory;
		module = new ModuleEntry();
	}

	void parse(String file) throws IOException {
		IncludeEntry fileEntry = new IncludeEntry();
		fileEntry.name('"' + file + '"');

		scanner = new Scanner(fileEntry, keywords);

		module.sourceFile(fileEntry);

		token = new Token(0);

		// printToken();
		// System.exit(0);
		try {
			match(0);
			// nothing file when first token is EOF
			if (token.equals(Token.EOF)) {
				ParseException.nothing(file);
			} else {
				specification(module);
			}
		} catch (ParseException exception) {
			// It has already been reported, just end.
		} catch (EOFException exception) {
			// It has already been reported, just end.
			throw exception;
		}
	}

	void printToken() throws IOException {
		Token token = scanner.getToken();
		while (!token.equals(Token.EOF)) {
			System.out.println(token + "----" + Token.toString(token.type));
			token = scanner.getToken();
		}
	}

	private void addToEmitList(ModuleEntry entry) {
		this.emitList.addElement(entry);
	}

	private void specification(ModuleEntry entry) throws IOException {
		while (!token.equals(Token.EOF)) {
			definition(entry);
		}
		addToEmitList(entry);
	}

	private void definition(ModuleEntry entry) throws IOException {
		try {
			switch (token.type) {
			case Token.VAR:
				varType(entry);
				break;
			case Token.FUNCTION:
				functionType(entry);
				break;
			case Token.Identifier:
				// 赋值或调用
				optDcl(entry);
				break;
			default:
				throw ParseException.syntaxError(scanner, new int[] { Token.VAR, Token.FUNCTION, Token.Identifier },
						token.type);
			}
			match(Token.Semicolon);
		} catch (ParseException e) {
			skipToSemicolon();
		}
	}

	/**
	 * 赋值或调用
	 * 
	 * @param entry
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月17日 下午3:34:31
	 */
	private void optDcl(ModuleEntry entry) throws IOException, ParseException {
		String name = token.getName();
		match(Token.Identifier);
		// 赋值
		if (token.equals(Token.Equal)) {
			VariableEntry varEntry = makeVariableEntry(name, entry);
			match(Token.Equal);
			varAssignment(entry, varEntry);
		} else if (token.equals(Token.LeftParen)) {
			FunctionEntry functionEntry = makeFunctionEntry(name, entry, true);
			params(entry, functionEntry);
		}
	}

	private FunctionEntry functionType(ModuleEntry entry) throws IOException, ParseException {
		match(Token.FUNCTION);
		FunctionEntry functionEntry = makeFunctionEntry(null, entry, false);
		if (token.equals(Token.Identifier)) {
			functionEntry.name(token.getName());
			match(Token.Identifier);
		}
		params(entry, functionEntry);
		functionBody(functionEntry);
		return functionEntry;
	}

	/**
	 * 创建FunctionEntry
	 * 
	 * @param functionName
	 * @param isFunctionDefine
	 *            是否是函数定义
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月17日 下午2:02:29
	 */
	private FunctionEntry makeFunctionEntry(String functionName, ModuleEntry entry, boolean invoke)
			throws IOException, ParseException {
		FunctionEntry fe = new FunctionEntry();
		fe.name(functionName);
		fe.comment(tokenHistory.lookBack(2).comment);
		fe.invoke(invoke);
		addToContainer(entry, fe);
		return fe;
	}

	/**
	 * 解析方法参数
	 * 
	 * @param entry
	 * @param functionEntry
	 *            2016年6月18日 下午11:51:45
	 * @throws ParseException
	 * @throws IOException
	 */
	private void params(ModuleEntry entry, FunctionEntry functionEntry) throws IOException, ParseException {
		match(Token.LeftParen);
		addFunctionParam(entry, functionEntry);
		while (token.equals(Token.Comma)) {
			match(Token.Comma);
			addFunctionParam(entry, functionEntry);
		}
		match(Token.RightParen);
	}

	private void addFunctionParam(ModuleEntry entry, FunctionEntry functionEntry) throws IOException, ParseException {
		SymtabEntry newEntry = param(entry, functionEntry);
		if (newEntry != null && isntInList(functionEntry.parameters(), newEntry.name())) {
			functionEntry.addParameter(newEntry);
		}

	}

	private boolean isntInList(Vector<SymtabEntry> list, String name) {
		boolean isnt = true;
		for (Enumeration<SymtabEntry> e = list.elements(); e.hasMoreElements();)
			if (name.equals(e.nextElement().name())) {
				ParseException.alreadyDeclared(scanner, name);
				isnt = false;
				break;
			}
		return isnt;
	}

	private SymtabEntry param(ModuleEntry entry, FunctionEntry functionEntry) throws IOException, ParseException {
		if (token.equals(Token.Identifier) || token.isLiterals()) { // 形如 var a
																	// = b(c,d);
			if (functionEntry.invoke() && token.isLiterals()) {
				JoinEntry je = new JoinEntry();
				je.addEntry(makeValueEntry(token.getName()));
				match(token.type);
				while (token.equals(Token.Plus)) {
					match(Token.Plus);
					SymtabEntry e = param(entry, functionEntry);
					if (e instanceof JoinEntry) {
						e = ((JoinEntry) e).entries().get(0);
					}
					je.addEntry(e);
				}
				return je;
			} else {
				String paramName = token.getName();
				match(Token.Identifier);
				if (functionEntry.invoke() && token.equals(Token.LeftParen)) {
					FunctionEntry fe = makeFunctionEntry(paramName, entry, functionEntry.invoke());
					params(entry, fe);
					// 函数参数中使用函数
					return fe;
				} else {
					return makeVariableEntry(paramName, functionEntry);
				}
			}
		}
		return null;
	}

	private ValueEntry makeValueEntry(Object value) {
		ValueEntry e = new ValueEntry();
		e.value(value);
		return e;
	}

	/**
	 * 解析方法体
	 * 
	 * @param functionEntry
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月18日 下午11:52:06
	 */
	private void functionBody(FunctionEntry functionEntry) throws IOException, ParseException {
		// 如果有方法体则解析方法体
		if (token.equals(Token.LeftBrace)) {
			match(Token.LeftBrace);
			while (!token.equals(Token.EOF) && !token.equals(Token.RightBrace))
				definition(functionEntry);
			match(Token.RightBrace);
		}
	}

	private VariableEntry varType(ModuleEntry entry) throws IOException, ParseException {
		match(Token.VAR);
		VariableEntry varEntry = defineVariable(entry);
		return varEntry;
	}

	/**
	 * 变量定义
	 * 
	 * @param entry
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月21日 下午2:27:41
	 */
	private VariableEntry defineVariable(ModuleEntry entry) throws IOException, ParseException {
		String varName = token.getName();
		VariableEntry varEntry = null;
		match(Token.Identifier);
		// if next token is Semicolon , Is the definition of variable
		if (token.equals(Token.Semicolon) || token.equals(Token.Comma)) {
			varEntry = makeVariableEntry(varName, entry);
		} else if (token.equals(Token.Equal)) {
			varEntry = makeVariableEntry(varName, entry);
			match(Token.Equal);
			while (token.equals(Token.Identifier)) {
				// 形如 var a=c=2;
				VariableEntry tmp = defineVariable(entry);
				varEntry.type(tmp.type());
			}
			varAssignment(entry, varEntry);
		} else {
			throw ParseException.syntaxError(scanner, new int[] { Token.Semicolon, Token.Equal }, token.type);
		}
		// 形如 var a=1,c=2;
		if (token.equals(Token.Comma)) {
			match(Token.Comma);
			// 递归调用
			defineVariable(entry);
		}
		return varEntry;
	}

	/**
	 * 赋值操作
	 * 
	 * @param name
	 * @param entry
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月17日 下午3:17:27
	 */
	private void varAssignment(ModuleEntry entry, VariableEntry varEntry) throws IOException, ParseException {
	
		if (token.isLiterals() || token.isOperator() || token.equals(Token.LeftParen)) {
			ValueEntry ve = makeValueEntry(operateExp(entry).value());
			varEntry.type(ve);
			
//			Token identifierToken = token;
//			match(token.type);
//			ValueEntry ve = makeValueEntry(identifierToken.name, identifierToken.type);
//			varEntry.type(ve);

		} else if (token.equals(Token.FUNCTION)) {
			// 形如: function(a,b,c){..}
			FunctionEntry fe = functionType(entry);
			fe.namedInvoke(varEntry.name());
			varEntry.type(fe);
		} else if (token.equals(Token.Identifier)) {
			String functionName = token.getName();
			match(Token.Identifier);
			varEntry.type(makeFunctionEntry(functionName, entry, false));
		} else {
			throw ParseException.syntaxError(
					scanner, new int[] { Token.BooleanLiteral, Token.IntegerLiteral, Token.CharacterLiteral,
							Token.StringLiteral, Token.FloatingPointLiteral, Token.FUNCTION, Token.Identifier },
					token.type);
		}
	}

	private Expression operateExp(ModuleEntry entry) throws IOException, ParseException {
		Expression expr = orExpr(null, entry);
		try {
			expr.evaluate();
		} catch (EvaluationException e) {
			ParseException.evaluationError (scanner, e.toString ());
		}
		
		return expr;
	}

	private Expression orExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null) {
			e = xorExpr(e, entry);
		} else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(xorExpr(null, entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.Bar)) {
			match(token.type);
			Or or = exprFactory.or(e, null);
			or.rep(e.rep() + " | ");
			return orExpr(or, entry);
		}
		return e;
	}

	private Expression xorExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null) {
			e = andExpr(null, entry);
		} else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(andExpr(null, entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.Carat)) {
			match(token.type);
			Xor xor = exprFactory.xor(e, null);
			xor.rep(e.rep() + " ^ ");
			return xorExpr(xor, entry);
		}
		return e;
	}

	private Expression andExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null)
			e = shiftExpr(null, entry);
		else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(shiftExpr(null, entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.Ampersand)) {
			match(token.type);
			And and = exprFactory.and(e, null);
			and.rep(e.rep() + " & ");
			return andExpr(and, entry);
		}
		return e;
	}

	private Expression shiftExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null)
			e = addExpr(null, entry);
		else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(addExpr(null, entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.ShiftLeft)) {
			match(token.type);
			ShiftLeft sl = exprFactory.shiftLeft(e, null);
			sl.rep(e.rep() + " << ");
			return shiftExpr(sl, entry);
		}
		if (token.equals(Token.ShiftRight)) {
			match(token.type);
			ShiftRight sr = exprFactory.shiftRight(e, null);
			sr.rep(e.rep() + " >> ");
			return shiftExpr(sr, entry);
		}
		return e;
	}

	private Expression addExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null)
			e = multExpr(null, entry);
		else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(multExpr(null, entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.Plus)) {
			match(token.type);
			Plus p = exprFactory.plus(e, null);
			p.rep(e.rep() + " + ");
			return addExpr(p, entry);
		}
		if (token.equals(Token.Minus)) {
			match(token.type);
			Minus m = exprFactory.minus(e, null);
			m.rep(e.rep() + " - ");
			return addExpr(m, entry);
		}
		return e;
	}

	private Expression multExpr(Expression e, ModuleEntry entry) throws IOException, ParseException {
		if (e == null) {
			e = unaryExpr(entry);
		} else {
			BinaryExpr b = (BinaryExpr) e;
			b.right(unaryExpr(entry));
			e.rep(e.rep() + b.right().rep());
		}
		if (token.equals(Token.Star)) {
			match(token.type);
			Times t = exprFactory.times(e, null);
			t.rep(e.rep() + " * ");
			return multExpr(t, entry);
		}
		if (token.equals(Token.Slash)) {
			match(token.type);
			Divide d = exprFactory.divide(e, null);
			d.rep(e.rep() + " / ");
			return multExpr(d, entry);
		}
		if (token.equals(Token.Percent)) {
			match(token.type);
			Modulo m = exprFactory.modulo(e, null);
			m.rep(e.rep() + " % ");
			return multExpr(m, entry);
		}
		return e;
	}

	private Expression unaryExpr(ModuleEntry entry) throws IOException, ParseException {
		if (token.equals(Token.Plus)) {
			match(token.type);
			Expression e = primaryExpr(entry);
			Positive pos = exprFactory.positive(e);
			pos.rep('+' + e.rep());
			return pos;
		}
		if (token.equals(Token.Minus)) {
			match(token.type);
			Expression e = primaryExpr(entry);
			Negative pos = exprFactory.negative(e);
			pos.rep('-' + e.rep());
			return pos;
		}
		if (token.equals(Token.Tilde)) {
			match(token.type);
			Expression e = primaryExpr(entry);
			Not not = exprFactory.not(e);
			not.rep('~' + e.rep());
			return not;
		}
		return primaryExpr(entry);
	}

	private Expression primaryExpr(ModuleEntry entry) throws IOException, ParseException {
		Expression primary = null;
		switch (token.type) {
		case Token.BooleanLiteral:
		case Token.CharacterLiteral:
		case Token.IntegerLiteral:
		case Token.FloatingPointLiteral:
		case Token.StringLiteral:
			primary = literal(entry);
			break;
		case Token.LeftParen:
			match(Token.LeftParen);
			primary = operateExp(entry);
			match(Token.RightParen);
			primary.rep('(' + primary.rep() + ')');
			break;
		default:
			throw ParseException.syntaxError(scanner,
					new int[] { Token.Identifier, Token.DoubleColon, Token.Literal, Token.LeftParen }, token.type);
		}
		return primary;
	}

	private Expression literal(ModuleEntry entry) throws ParseException, IOException {
		String string = token.name;
		Expression literal = null;
		switch (token.type) {
		case Token.IntegerLiteral:
			match(Token.IntegerLiteral);
			try {
				literal = exprFactory.terminal(string, parseStringToInteger(string));
			} catch (NumberFormatException exception) {
				ParseException.notANumber(scanner, string);
				literal = exprFactory.terminal("0", BigInteger.valueOf(0));
			}
			break;
		case Token.CharacterLiteral:
			match(Token.CharacterLiteral);
			literal = exprFactory.terminal("'" + string.substring(1) + "'", new Character(string.charAt(0)));
			break;
		case Token.FloatingPointLiteral:
			match(Token.FloatingPointLiteral);
			try {
				literal = exprFactory.terminal(string, new Double(string));
			} catch (NumberFormatException e) {
				ParseException.notANumber(scanner, string);
			}
			break;
		case Token.BooleanLiteral:
			literal = booleanLiteral();
			break;
		case Token.StringLiteral:
			literal = stringLiteral();
			break;
		default:
			throw ParseException.syntaxError(scanner, Token.Literal, token.type);
		}
		return literal;
	}

	private Expression stringLiteral() throws IOException, ParseException {
		String literal = "";
		do {
			literal += token.name;
			match(Token.StringLiteral);
		} while (token.equals(Token.StringLiteral));
		Expression stringExpr = exprFactory.terminal('"' + literal + '"', literal);
		return stringExpr;
	}

	private Expression booleanLiteral() throws IOException, ParseException {
		Boolean bool = null;
		if (token.name.equals("TRUE"))
			bool = new Boolean(true);
		else if (token.name.equals("FALSE"))
			bool = new Boolean(false);
		else {
			ParseException.invalidConst(scanner, token.name);
			bool = new Boolean(false);
		}
		String name = token.name;
		match(Token.BooleanLiteral);
		return exprFactory.terminal(name, bool);
	}

	private BigInteger parseStringToInteger(String string) {
		int radix = 10;
		if (string.length() > 1)
			if (string.charAt(0) == '0')
				if (string.charAt(1) == 'x' || string.charAt(1) == 'X') {
					string = string.substring(2);
					radix = 16;
				} else
					radix = 8;
		return new BigInteger(string, radix);
	}

	private VariableEntry makeVariableEntry(String name, ModuleEntry entry) {
		VariableEntry varEntry = new VariableEntry();
		varEntry.name(name);
		varEntry.comment(tokenHistory.lookBack(2).comment);
		addToContainer(entry, varEntry);
		return varEntry;
	}

	void addToContainer(ModuleEntry container, SymtabEntry contained) {
		container.addContained(contained);
	}

	/**
	 * 匹配当前token，并读取下一个token
	 * 
	 * @param type
	 * @throws IOException
	 * @throws ParseException
	 *             2016年6月16日 下午10:54:37
	 */
	private void match(int type) throws IOException, ParseException {
		ParseException exception = null;
		if (!token.equals(type)) {
			exception = ParseException.syntaxError(scanner, type, token.type);

			if (type == Token.Semicolon)
				return;
		}

		token = scanner.getToken();

		tokenHistory.insert(token);

		// if(token.equals(Token.Identifier)) {
		//
		// }
		if (exception != null)
			throw exception;
	}

	/**
	   *
	   **/
	private void skipToSemicolon() throws IOException {
		while (!token.equals(Token.EOF) && !token.equals(Token.Semicolon)) {
			if (token.equals(Token.LeftBrace))
				skipToRightBrace();
			try {
				match(token.type);
			} catch (ParseException exception) {
				// The error has already been reported...
			}
		}
		if (token.equals(Token.EOF))
			throw new EOFException();
		try {
			match(Token.Semicolon);
		} catch (Exception exception) {
		}
	} // skipToSemicolon

	/**
	 *
	 **/
	private void skipToRightBrace() throws IOException {
		boolean firstTime = true;
		while (!token.equals(Token.EOF) && !token.equals(Token.RightBrace)) {
			if (firstTime)
				firstTime = false;
			else if (token.equals(Token.LeftBrace))
				skipToRightBrace();
			try {
				match(token.type);
			} catch (ParseException exception) {
				// The error has already been reported...
			}
		}
		if (token.equals(Token.EOF))
			throw new EOFException();
	} // skipToRightBrace
}
