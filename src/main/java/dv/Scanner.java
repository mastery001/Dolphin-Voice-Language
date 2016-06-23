package dv;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

import dv.entry.IncludeEntry;

/**
 * 词法分析器，扫描器
 * 
 * @author zouziwen
 *
 *         2016年6月14日 下午12:51:59
 */

/**
 * @author zouziwen
 *
 * 2016年6月15日 下午10:28:00
 */
/**
 * @author zouziwen
 *
 *         2016年6月15日 下午10:28:03
 */
class Scanner {

	Stack<ScannerData> dataStack = new Stack<ScannerData>();
	Vector<String> keywords = new Vector<String>();
	ScannerData data = new ScannerData();

	boolean escapedOK = true;

	Scanner(IncludeEntry file, String[] keywords) throws IOException {
		readFile(file);
		sortKeywords(keywords);
		init();
	}

	/**
	 * 不同类型的扫描器 2016年6月15日 下午10:28:10
	 */
	private CommentScanner commentScanner;
	private StringScanner stringScanner;
	private NumberScanner numberScanner;
	private CharScanner charScanner;

	private void init() {
		commentScanner = new CommentScanner();
		stringScanner = new StringScanner();
		numberScanner = new NumberScanner();
		charScanner = new CharScanner();
	}

	void sortKeywords(String[] keywords) {
		for (int i = 0; i < keywords.length; ++i)
			this.keywords.addElement(keywords[i]);
	}

	void readFile(IncludeEntry file) throws IOException {
		String filename = file.name();
		filename = filename.substring(1, filename.length() - 1);
		readFile(file, filename);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @param filename
	 * @throws IOException
	 *             2016年6月15日 下午3:03:35
	 */
	void readFile(IncludeEntry file, String filename) throws IOException {
		data.fileEntry = file;
		data.filename = filename;

		File idlFile = new File(data.filename);
		int len = (int) idlFile.length();
		FileReader fileReader = new FileReader(idlFile);
		final String EOL = System.getProperty("line.separator");
		data.fileBytes = new char[len + EOL.length()];
		try {
			fileReader.read(data.fileBytes, 0, len);
		} catch (IOException e) {
			throw e;
		} finally {
			fileReader.close();
		}

		for (int i = 0; i < EOL.length(); i++)
			data.fileBytes[len + i] = EOL.charAt(i);

		readChar();

	}

	/**
	 * 读取一个字符
	 * 
	 * @throws IOException
	 *             2016年6月15日 下午3:03:55
	 */
	void readChar() throws IOException {
		if (data.fileIndex >= data.fileBytes.length) {
			if (dataStack.empty()) {
				throw new EOFException();
			} else {
				// reserved
			}
		} else {
			data.ch = (char) (data.fileBytes[data.fileIndex++] & 0x00ff);
			if (data.ch == '\n')
				++data.line;
		}
	}

	Token getToken() throws IOException {
		Token token = null;
		String commentText = new String("");
		while (token == null) {
			try {
				data.oldIndex = data.fileIndex;
				data.oldLine = data.line;
				if (data.ch <= ' ') {
					skipWhiteSpace();
					continue;
				}

				if ((data.ch >= 'a' && data.ch <= 'z') || (data.ch >= 'A' && data.ch <= 'Z') || (data.ch == '_')
						|| Character.isLetter(data.ch)) {
					token = stringScanner.getString();
				} else if ((data.ch >= '0' && data.ch <= '9') || data.ch == '.') {
					token = numberScanner.getNumber();
				} else {
					switch (data.ch) {
					case ';':
						token = new Token(Token.Semicolon);
						break;
					case '{':
						token = new Token(Token.LeftBrace);
						break;
					case '}':
						token = new Token(Token.RightBrace);
						break;
					case ':':
						readChar();
						if (data.ch == ':')
							token = new Token(Token.DoubleColon);
						else {
							unread(data.ch);
							token = new Token(Token.Colon);
						}
						break;
					case ',':
						token = new Token(Token.Comma);
						break;
					case '=':
						readChar();
						if (data.ch == '=')
							token = new Token(Token.DoubleEqual);
						else {
							unread(data.ch);
							token = new Token(Token.Equal);
						}
						break;
					case '+':
						token = new Token(Token.Plus);
						break;
					case '-':
						token = new Token(Token.Minus);
						break;
					case '(':
						token = new Token(Token.LeftParen);
						break;
					case ')':
						token = new Token(Token.RightParen);
						break;
					case '<':
						readChar();
						if (data.ch == '<')
							token = new Token(Token.ShiftLeft);
						else if (data.ch == '=')
							token = new Token(Token.LessEqual);
						else {
							unread(data.ch);
							token = new Token(Token.LessThan);
						}
						break;
					case '>':
						readChar();
						if (data.ch == '>')
							token = new Token(Token.ShiftRight);
						else if (data.ch == '=')
							token = new Token(Token.GreaterEqual);
						else {
							unread(data.ch);
							token = new Token(Token.GreaterThan);
						}
						break;
					case '[':
						token = new Token(Token.LeftBracket);
						break;
					case ']':
						token = new Token(Token.RightBracket);
						break;
					case '\'':
						token = charScanner.getCharacterToken(false);
						break;
					case '"':
						readChar();
						token = new Token(Token.StringLiteral, getUntil('"', false, false, false));
						break;
					case '\\':
						readChar();
						// If this is at the end of a line, then it is the
						// line continuation character - treat it as white space
						if (data.ch == '\n' || data.ch == '\r')
							token = null;
						else
							token = new Token(Token.Backslash);
						break;
					case '|':
						readChar();
						if (data.ch == '|')
							token = new Token(Token.DoubleBar);
						else {
							unread(data.ch);
							token = new Token(Token.Bar);
						}
						break;
					case '^':
						token = new Token(Token.Carat);
						break;
					case '&':
						readChar();
						if (data.ch == '&')
							token = new Token(Token.DoubleAmpersand);
						else {
							unread(data.ch);
							token = new Token(Token.Ampersand);
						}
						break;
					case '*':
						token = new Token(Token.Star);
						break;
					case '/':
						readChar();
						// <21jul1997daz> Extract comments rather than skipping
						// them.
						// Preserve only the comment immediately preceding the
						// next token.
						if (data.ch == '/')
							// skipLineComment ();
							commentText = commentScanner.getLineComment();
						else if (data.ch == '*')
							// skipBlockComment ();
							commentText = commentScanner.getBlockComment();
						else {
							unread(data.ch);
							token = new Token(Token.Slash);
						}
						break;
					case '%':
						token = new Token(Token.Percent);
						break;
					case '~':
						token = new Token(Token.Tilde);
						break;

					// The period token is recognized in getNumber.
					// The period is only valid in a floating ponit number.
					// case '.':
					// token = new Token (Token.Period);
					// break;
					case '!':
						readChar();
						if (data.ch == '=')
							token = new Token(Token.NotEqual);
						else {
							unread(data.ch);
							token = new Token(Token.Exclamation);
						}
						break;
					default:
						throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(),
								currentLinePosition(), data.ch);
					}
					readChar();
				}
			} catch (EOFException e) {
				token = new Token(Token.EOF);
			}
		}
		token.comment = commentText;

		return token;
	}

	class CommentScanner {

		/**
		 * 块注释
		 * 
		 * @return
		 * @throws IOException
		 *             2016年6月15日 下午10:29:04
		 */
		String getBlockComment() throws IOException {
			StringBuffer sb = new StringBuffer("/*");
			try {
				boolean done = false;
				readChar();
				sb.append(data.ch);
				while (!done) {
					while (data.ch != '*') {
						readChar();
						sb.append(data.ch);
					}
					readChar();
					sb.append(data.ch);
					if (data.ch == '/')
						done = true;
				}
			} catch (EOFException e) {
				throw e;
			}
			return sb.toString();
		}

		/**
		 * 行注释
		 * 
		 * @return
		 * @throws IOException
		 *             2016年6月15日 下午10:30:42
		 */
		String getLineComment() throws IOException {
			StringBuffer sb = new StringBuffer("/");
			while (data.ch != '\n') {
				if (data.ch != '\r')
					sb.append(data.ch);
				readChar();
			}
			return sb.toString();
		}

	}

	private class StringScanner {
		Token getString() throws IOException {
			StringBuffer sbuf = new StringBuffer();
			boolean escaped = false;
			boolean[] collidesWithKeyword = { false };
			if (data.ch == '_') {
				sbuf.append(data.ch);
				readChar();
				if (escaped = escapedOK) {
					if (data.ch == '_')
						throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(),
								currentLinePosition(), data.ch);
				}
			}
			while (Character.isLetterOrDigit(data.ch) || (data.ch == '_')) {
				sbuf.append(data.ch);
				readChar();
			}
			String string = sbuf.toString();
			if (!escaped) {
				Token result = Token.makeKeywordToken(string, escapedOK, collidesWithKeyword);
				if (result != null)
					return result;
			}

			string = getIdentifier(string);

			return new Token(Token.Identifier, string , collidesWithKeyword[0]);
		}

		String getIdentifier(String string) {
			if (keywords.contains(string))
				string = '_' + string;
			return string;
		}
	}

	private class CharScanner {
		Token getCharacterToken(boolean isWide) throws IOException {
			Token token = null;
			readChar();
			if (data.ch == '\\') {
				readChar();
				if ((data.ch == 'x') || (data.ch == 'u')) {
					char charType = data.ch;
					int hexNum = getNDigitHexNumber((charType == 'x') ? 2 : 4);
					return new Token(Token.CharacterLiteral,
							((char) hexNum) + "\\" + charType + Integer.toString(hexNum, 16));
				}
				if ((data.ch >= '0') && (data.ch <= '7')) {
					int octNum = get3DigitOctalNumber();
					return new Token(Token.CharacterLiteral, ((char) octNum) + "\\" + Integer.toString(octNum, 8));
				}
				return singleCharEscapeSequence(isWide);
			}
			token = new Token(Token.CharacterLiteral, "" + data.ch + data.ch);
			readChar();
			return token;
		}

		int getNDigitHexNumber(int n) throws IOException {
			readChar();
			if (!isHexChar(data.ch))
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						data.ch);
			String string = "" + data.ch;
			readChar();
			for (int i = 2; i <= n; i++) {
				if (!isHexChar(data.ch))
					break;
				string += data.ch;
				readChar();
			}
			try {
				return Integer.parseInt(string, 16);
			} catch (NumberFormatException e) {
			}
			return 0;
		}

		boolean isHexChar(char ch) {
			return ((data.ch >= '0') && (data.ch <= '9')) || ((data.ch >= 'a') && (data.ch <= 'f'))
					|| ((data.ch >= 'A') && (data.ch <= 'F'));
		}

		int get3DigitOctalNumber() throws IOException {
			char firstDigit = data.ch;
			String string = "" + data.ch;
			readChar();
			if (data.ch >= '0' && data.ch <= '7') {
				string = string + data.ch;
				readChar();
				if (data.ch >= '0' && data.ch <= '7') {
					string = string + data.ch;
					if (firstDigit > '3')
						// This is a 3-digit number bigger than 377
						throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(),
								currentLinePosition(), firstDigit);
					readChar();
				}
			}
			int ret = 0;
			try {
				ret = Integer.parseInt(string, 8);
			} catch (NumberFormatException e) {
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						string.charAt(0));
			}
			return ret;
		}

		/**
		 * 转义字符
		 * 
		 * @param isWide
		 * @return
		 * @throws IOException
		 *             2016年6月15日 下午4:07:49
		 */
		Token singleCharEscapeSequence(boolean isWide) throws IOException {
			Token token;
			if (data.ch == 'n')
				// newline
				token = new Token(Token.CharacterLiteral, "\n\\n");
			else if (data.ch == 't')
				// horizontal tab
				token = new Token(Token.CharacterLiteral, "\t\\t");
			else if (data.ch == 'v')
				// vertical tab
				token = new Token(Token.CharacterLiteral, "\013\\v");
			else if (data.ch == 'b')
				// backspace
				token = new Token(Token.CharacterLiteral, "\b\\b");
			else if (data.ch == 'r')
				// carriage return
				token = new Token(Token.CharacterLiteral, "\r\\r");
			else if (data.ch == 'f')
				// form feed
				token = new Token(Token.CharacterLiteral, "\f\\f");
			else if (data.ch == 'a')
				// alert
				token = new Token(Token.CharacterLiteral, "\007\\a");
			else if (data.ch == '\\')
				// backslash
				token = new Token(Token.CharacterLiteral, "\\\\\\");
			else if (data.ch == '?')
				// question mark
				token = new Token(Token.CharacterLiteral, "?\\?");
			else if (data.ch == '\'')
				// single quote
				token = new Token(Token.CharacterLiteral, "'\\'");
			else if (data.ch == '"')
				// double quote
				token = new Token(Token.CharacterLiteral, "\"\\\"");
			else
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						data.ch);
			readChar();
			return token;
		}

	}

	private class NumberScanner {
		Token getNumber() throws IOException {
			if (data.ch == '.')
				return getFractionNoInteger();
			else if (data.ch == '0')
				return isItHex();
			else // the only other possibliities are 1..9
				return getInteger();
		}

		Token getFractionNoInteger() throws IOException {
			readChar();
			if (data.ch >= '0' && data.ch <= '9') {
				return getFraction(".");
			}
			return new Token(Token.Period);
		}

		Token getFraction(String string) throws IOException {
			while (data.ch >= '0' && data.ch <= '9') {
				string = string + data.ch;
				readChar();
			}
			if (data.ch == 'e' || data.ch == 'E')
				return getExponent(string + 'E');
			else
				return new Token(Token.FloatingPointLiteral, string);
		}

		Token getExponent(String string) throws IOException {
			readChar();
			if (data.ch == '+' || data.ch == '-') {
				string = string + data.ch;
				readChar();
			} else if (data.ch < '0' || data.ch > '9')
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						data.ch);
			while (data.ch >= '0' && data.ch <= '9') {
				string = string + data.ch;
				readChar();
			}
			return new Token(Token.FloatingPointLiteral, string);
		}

		Token isItHex() throws IOException {
			readChar();
			if (data.ch == '.') {
				readChar();
				return getFraction("0.");
			} else if (data.ch == 'x' || data.ch == 'X')
				return getHexNumber("0x");
			else if (data.ch == '8' || data.ch == '9')
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						data.ch);
			else if (data.ch >= '0' && data.ch <= '7')
				return getOctalNumber();
			else if (data.ch == 'e' || data.ch == 'E')
				return getExponent("0E");
			else
				return new Token(Token.IntegerLiteral, "0");
		}

		Token getOctalNumber() throws IOException {
			String string = "0" + data.ch;
			readChar();
			while ((data.ch >= '0' && data.ch <= '9')) {
				if (data.ch == '8' || data.ch == '9')
					throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
							data.ch);
				string = string + data.ch;
				readChar();
			}
			return new Token(Token.IntegerLiteral, string);
		}

		Token getHexNumber(String string) throws IOException {
			readChar();
			if ((data.ch < '0' || data.ch > '9') && (data.ch < 'a' || data.ch > 'f')
					&& (data.ch < 'A' || data.ch > 'F'))
				throw new InvalidCharacter(data.filename, currentLine(), currentLineNumber(), currentLinePosition(),
						data.ch);
			else
				while ((data.ch >= '0' && data.ch <= '9') || (data.ch >= 'a' && data.ch <= 'f')
						|| (data.ch >= 'A' && data.ch <= 'F')) {
					string = string + data.ch;
					readChar();
				}
			return new Token(Token.IntegerLiteral, string);
		}

		Token getInteger() throws IOException {
			String string = "" + data.ch;
			readChar();
			if (data.ch == '.') {
				readChar();
				return getFraction(string + '.');
			} else if (data.ch == 'e' || data.ch == 'E')
				return getExponent(string + 'E');
			else if (data.ch >= '0' && data.ch <= '9')
				while (data.ch >= '0' && data.ch <= '9') {
					string = string + data.ch;
					readChar();
					if (data.ch == '.') {
						readChar();
						return getFraction(string + '.');
					}
				}
			return new Token(Token.IntegerLiteral, string);
		}

	}

	String getUntil(char c) throws IOException {
		return getUntil(c, true, true, true);
	}

	String getUntil(char c, boolean allowQuote, boolean allowCharLit, boolean allowComment) throws IOException {
		String string = "";
		while (data.ch != c)
			string = appendToString(string, allowQuote, allowCharLit, allowComment);
		return string;
	} // getUntil

	private String appendToString(String string, boolean allowQuote, boolean allowCharLit, boolean allowComment)
			throws IOException {
		// Ignore any comments if they are allowed
		if (allowComment && data.ch == '/') {
			readChar();
			if (data.ch == '/')
				skipLineComment();
			else if (data.ch == '*')
				skipBlockComment();
			else
				string = string + '/';
		}
		// Handle line continuation character
		else if (data.ch == '\\') {
			readChar();
			if (data.ch == '\n')
				readChar();
			else if (data.ch == '\r') {
				readChar();
				if (data.ch == '\n')
					readChar();
			} else {
				string = string + '\\' + data.ch;
				readChar();
			}
		}
		// characters within "("...")" and '"'...'"' are ignored.
		// Ie getUntil ',' on (,,,,),X will return (,,,)
		else {
			if (allowCharLit && data.ch == '"') {
				readChar();
				string = string + '"';
				while (data.ch != '"')
					string = appendToString(string, true, false, allowComment);
			} else if (allowQuote && allowCharLit && data.ch == '(') {
				readChar();
				string = string + '(';
				while (data.ch != ')')
					string = appendToString(string, false, false, allowComment);
			} else if (allowQuote && data.ch == '\'') {
				readChar();
				string = string + "'";
				while (data.ch != '\'')
					string = appendToString(string, false, true, allowComment);
			}
			string = string + data.ch;
			readChar();
		}
		return string;
	}

	void skipBlockComment() throws IOException {
		try {
			boolean done = false;
			readChar();
			while (!done) {
				while (data.ch != '*')
					readChar();
				readChar();
				if (data.ch == '/')
					done = true;
			}
		} catch (EOFException e) {
			throw e;
		}
	}

	void skipLineComment() throws IOException {
		while (data.ch != '\n')
			readChar();
	}

	private void unread(char ch) {
		if (ch == '\n')
			--data.line;
		--data.fileIndex;
	}

	private int BOL; // Beginning Of Line

	private String currentLine() {
		BOL = data.fileIndex - 1;
		try {
			// If the current position is at the end of the line,
			// set BOL to before the end of the line so the whole
			// line is returned.
			if (data.fileBytes[BOL - 1] == '\r' && data.fileBytes[BOL] == '\n')
				BOL -= 2;
			else if (data.fileBytes[BOL] == '\n')
				--BOL;
			while (data.fileBytes[BOL] != '\n')
				--BOL;
		} catch (ArrayIndexOutOfBoundsException e) {
			BOL = -1;
		}
		++BOL; // Go to the first character AFTER the newline
		int EOL = data.fileIndex - 1;
		try {
			while (data.fileBytes[EOL] != '\n' && data.fileBytes[EOL] != '\r')
				++EOL;
		} catch (ArrayIndexOutOfBoundsException e) {
			EOL = data.fileBytes.length;
		}
		if (BOL < EOL)
			return new String(data.fileBytes, BOL, EOL - BOL);
		else
			return "";
	}

	int lastTokenLineNumber() {
		return data.oldLine;
	}

	String lastTokenLine() {
		int saveFileIndex = data.fileIndex;
		data.fileIndex = data.oldIndex;
		String ret = currentLine();
		data.fileIndex = saveFileIndex;
		return ret;
	}

	int lastTokenLinePosition() {
		return data.oldIndex - BOL;
	}

	String filename() {
		return data.filename;
	}


	private int currentLineNumber() {
		return data.line;
	}

	private int currentLinePosition() {
		return data.fileIndex - BOL;
	}

	/**
	 * 跳过空白字符
	 * 
	 * @throws IOException
	 *             2016年6月15日 下午10:32:20
	 */
	void skipWhiteSpace() throws IOException {
		while (data.ch <= ' ')
			readChar();
	}

	/**
	 * 扫描器的数据记录
	 * 
	 * @author zouziwen
	 *
	 *         2016年6月14日 下午9:02:59
	 */
	class ScannerData {

		public ScannerData() {
		}

		public ScannerData(ScannerData that) {
			this.indent = that.indent;
			this.fileEntry = that.fileEntry;
			this.filename = that.filename;
			this.fileBytes = that.fileBytes;
			this.fileIndex = that.fileIndex;
			this.oldIndex = that.oldIndex;
			this.ch = that.ch;
			this.line = that.line;
			this.oldLine = that.oldLine;
		}

		// 缩进
		String indent = "";
		IncludeEntry fileEntry = null;
		String filename = "";

		char[] fileBytes = null;
		int fileIndex = 0;
		int oldIndex = 0;
		char ch;
		int line = 1;
		int oldLine = 1;

	}

}
