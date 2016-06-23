package dv;

public class Token {

	public static final int 
			ANY = 0,
			VAR = 1,
			FUNCTION = 2,
			
			Identifier           =  80, // Identifier
			
			Semicolon = 100,	// Symbols	
			LeftBrace = 101, 		
			RightBrace = 102, 		
			Colon = 103, 		
			Comma = 104, 		
			LeftParen = 105, 		
			RightParen = 106, 		
			LeftBracket = 107, 		
			RightBracket = 108,		
			Apostrophe = 109, 		
			Quote = 110, 		
			Backslash = 111, 		
			DoubleColon = 112, 		
			Period = 113,		
			Hash = 114, 		
			Exclamation = 115, 		
			Equal = 116, 		
			Plus = 117, 		
			Minus = 118,		
			Star = 119,		
			Slash = 120, 		
			Percent = 121, 		
			LessThan = 122, 		
			GreaterThan = 123, 		
			DoubleEqual = 124, 		
			Bar = 125, 		
			Carat = 126, 		
			Ampersand = 127, 		
			Tilde = 128, 		
			NotEqual = 129, 		
			GreaterEqual = 130, 		
			LessEqual = 131,		
			DoubleBar = 132, 		
			DoubleAmpersand = 133,		
			ShiftLeft = 124, 		
			ShiftRight = 125, 		

			
			BooleanLiteral = 200, // Literals
			CharacterLiteral     = 201,
			IntegerLiteral       = 202,
			FloatingPointLiteral = 203,
			StringLiteral        = 204,
			Literal              = 205,
			
			EOF = 999; // End of Input

	public static final String[] Symbols = { ";", "{", "}", ":", ",", "(", ")", "[", "]", "'", "\"", "\\", "::", ".",
			"#", "!", "=", "+", "-", "*", "/", "%", "<", ">", "==", "|", "^", "&", "~", "!=", ">=", "<=", "||", "&&",
			"<<", ">>" };
	

	public static final String[] Keywords = { "any" , "var" , "function" };
	
	static final int 
	FirstKeyword = 0,
	LastKeyword = 2;
	
	static final int 
	FirstSymbol = 100,
	LastSymbol = 135;

	static final int 
	FitstOperator = 115,
	LastOperator = 135;
	
	boolean isOperator() {
		return type >= FitstOperator && type <= LastOperator;
	}
	
	static final int
    FirstLiteral = 200,
    LastLiteral  = 299;

	private static final String[] Literals = {
		      Util.getMessage ("Token.boolLit"),
		      Util.getMessage ("Token.charLit"),
		      Util.getMessage ("Token.intLit"),
		      Util.getMessage ("Token.floatLit"),
		      Util.getMessage ("Token.stringLit"),
		      Util.getMessage ("Token.literal")};
	
	boolean isLiterals() {
		return type >= FirstLiteral && type <= LastLiteral;
	}
	
	/**
	 * Code identifying the lexical class to which this token belongs, e.g.,
	 * Keyword, Identifier, ... 2016年6月14日 下午8:24:18
	 */
	int type;

	String name;
	
	String comment;

	/**
	 * 是否是关键字
	 * 2016年6月16日 上午12:09:06
	 */
	boolean collidesWithKeyword;

	public Token(int type) {
		this(type, null);
	}

	public Token(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Token(int type, String name , boolean collision) {
		this.type = type;
		this.name = name;
		this.collidesWithKeyword = collision;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		if(type == Identifier) {
			return name;
		}
		return toString(type);
	}

	static String toString(int type) {
		if(type <= LastKeyword) {
			return Keywords[type];
		}
		if(type == Identifier){
			return Util.getMessage ("Token.identifier");
		}
		if(type <= LastSymbol) {
			return Symbols[type - FirstSymbol];
		}
		if (type <= LastLiteral)
		      return Literals[type - FirstLiteral];
		if (type == EOF)
		      return Util.getMessage ("Token.endOfFile");
		return Util.getMessage ("Token.unknown");
	};

	public static Token makeKeywordToken(String string, boolean escapedOK, boolean[] collision) {
		for (int i = Token.FirstKeyword; i <= Token.LastKeyword; ++i) {
			if (string.equals (Token.Keywords[i])) {
				if (i > LastKeyword) {
	                collision[0] |= escapedOK; // escapedOK true iff not preprocessing
	                break ;
	            }
				if (string.equals ("TRUE") || string.equals ("FALSE"))
	                return new Token (Token.BooleanLiteral, string) ;
				else 
					return new Token (i);
			}else if (string.equalsIgnoreCase (Token.Keywords[i])) {
	            collision[0] |= true;
	            break;
	        }
		}
		return null;
	}
	
	public boolean equals (int type){
		return this.type == type;
	} // equals

}
