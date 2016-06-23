package dv;

import java.io.EOFException;
import java.io.IOException;

import dv.entry.FunctionEntry;
import dv.entry.IncludeEntry;
import dv.entry.ModuleEntry;
import dv.entry.ValueEntry;
import dv.entry.VariableEntry;
import dv.utils.Printer;

/**
 * 语法分析器
 * 
 * @author zouziwen
 *
 *         2016年6月15日 下午7:57:24
 */
class ParserVersion1 {

	Arguments arguments;

	private String[] keywords;

	Token token = null;

	private TokenBuffer tokenHistory = new TokenBuffer();

	ModuleEntry module;

	Scanner scanner = null;

	ParserVersion1(Arguments arguments, String[] keywords) {
		this.arguments = arguments;
		this.keywords = keywords;
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
		}
		Printer.print(module.contained());
	}

	void printToken() throws IOException {
		Token token = scanner.getToken();
		while (!token.equals(Token.EOF)) {
			System.out.println(token + "----" + Token.toString(token.type));
			token = scanner.getToken();
		}
	}

	private void specification(ModuleEntry entry) throws IOException {
		while (!token.equals(Token.EOF)) {
			definition(entry);
		}
	}

	private void definition(ModuleEntry entry) throws IOException {
		try {
			switch (token.type) {
			case Token.VAR:
				match(Token.VAR);
				varDcl(entry);
				break;
			case Token.FUNCTION:
				entry.addContained(functionDcl(entry));
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

	private FunctionEntry functionDcl(ModuleEntry entry) throws IOException, ParseException {
		match(Token.FUNCTION);
		String functionName = null;
		if (token.equals(Token.Identifier)) {
			functionName = token.getName();
			match(Token.Identifier);
		}
		FunctionEntry functionEntry = functionType(functionName, true);
		match(Token.LeftBrace);
		while (!token.equals(Token.EOF) && !token.equals(Token.RightBrace))
			definition(functionEntry);
		match(Token.RightBrace);
		return functionEntry;
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
			VariableEntry varEntry = makeVariableEntry(name);
			varEntry.comment(tokenHistory.lookBack(1).comment);
			varDcl2(entry, varEntry);
		} else if (token.equals(Token.LeftParen)) {
			entry.addContained(functionType(name, false));
		}
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
	private FunctionEntry functionType(String functionName, boolean isFunctionDefine)
			throws IOException, ParseException {
		FunctionEntry fe = null;
		match(Token.LeftParen);
		fe = new FunctionEntry();
		fe.name(functionName);
		fe.comment(tokenHistory.lookBack(2).comment);
		fe.invoke(isFunctionDefine);
		if (token.equals(Token.Identifier)) { // 形如 var a = b(c,d);
			String paramName = token.getName();
			match(Token.Identifier);
			if (!isFunctionDefine && token.equals(Token.LeftParen)) {
				// 函数参数中使用函数
				fe.addParameter(functionType(paramName, isFunctionDefine));
			} else {
				fe.addParameter(makeVariableEntry(paramName));
			}
			while (token.equals(Token.Comma)) {
				match(Token.Comma);
				if (token.equals(Token.Identifier)) {
					if (!isFunctionDefine && token.equals(Token.LeftParen)) {
						// 函数参数中使用函数
						fe.addParameter(functionType(token.getName(), isFunctionDefine));
					} else {
						fe.addParameter(makeVariableEntry(token.getName()));
					}
					match(Token.Identifier);
				} else {
					throw ParseException.syntaxError(scanner, new int[] { Token.Comma }, token.type);
				}
			}
		}
		match(Token.RightParen);
		return fe;
	}

	private void varDcl(ModuleEntry entry) throws IOException, ParseException {
		String varName = token.getName();
		VariableEntry varEntry = makeVariableEntry(varName);
		varEntry.comment(tokenHistory.lookBack(1).comment);
		match(Token.Identifier);
		varDcl2(entry, varEntry);
	}

	private void varDcl2(ModuleEntry entry, VariableEntry varEntry) throws IOException, ParseException {
		// if next token is Semicolon , Is the definition of variable
		if (token.equals(Token.Semicolon) || token.equals(Token.Comma)) {
			entry.addContained(varEntry);
		} else if (token.equals(Token.Equal)) {
			entry.addContained(varDcl3(entry, varEntry));
		} else {
			throw ParseException.syntaxError(scanner, new int[] { Token.Semicolon, Token.Equal }, token.type);
		}
		if (token.equals(Token.Comma)) {
			match(Token.Comma);
			// 递归调用
			varDcl(entry);
		}
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
	private VariableEntry varDcl3(ModuleEntry entry, VariableEntry varEntry) throws IOException, ParseException {
		match(Token.Equal);
		if (token.isLiterals()) {
			ValueEntry e = new ValueEntry();
			e.value(token.name);
			e.varType(token.type);
			varEntry.type(e);
			match(token.type);
		} else if (token.equals(Token.FUNCTION)) {
			// 形如: function(a,b,c){..}
			FunctionEntry fe = functionDcl(entry);
			varEntry.type(fe);
		} else if (token.equals(Token.Identifier)) {
			String functionName = token.getName();
			match(Token.Identifier);
			varEntry.type(functionType(functionName, false));
		} else {
			throw ParseException.syntaxError(
					scanner, new int[] { Token.BooleanLiteral, Token.IntegerLiteral, Token.CharacterLiteral,
							Token.StringLiteral, Token.FloatingPointLiteral, Token.FUNCTION, Token.Identifier },
					token.type);
		}
		return varEntry;
	}

	private VariableEntry makeVariableEntry(String name) {
		VariableEntry varEntry = new VariableEntry();
		varEntry.name(name);
		return varEntry;
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
