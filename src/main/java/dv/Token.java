package dv;

  
  
/**  
 *@Description:  Token定义：<p>
 *					1. 变量<p>
 *					2. 符号(操作符、语句、块、注释等分隔符)<p>
 *					3. 关键字<p>
 * 					Notes:注释不属于Token，只属于Token中的属性
 *@Author:zouziwen
 *@Since:2016年6月13日  
 *@Version:1.1.0  
 */
public class Token {

	public static final int 
			/*
		     * Declaration keywords
		     */
			VAR			    = 0,
			FUNCTION        = 1,
			/*
			 * Statement keywords
			 */
			IF				= 2,
			ELSE			= 3,
			FOR				= 4,
			WHILE		    = 5,
			DO              = 6,
			SWITCH          = 7,
			CASE            = 8,
			DEFAULT         = 9,
			BREAK           = 10,
			CONTINUE        = 11,
			RETURN          = 12,
			
			Identifier           =  80, // Identifier
			
			Semicolon = 100,	// 详见 Symbols
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

	// 符号标识，对应上述常量中的相应英文
	public static final String[] Symbols = { ";", "{", "}", ":", ",", "(", ")", "[", "]", "'", "\"", "\\", "::", ".",
			"#", "!", "=", "+", "-", "*", "/", "%", "<", ">", "==", "|", "^", "&", "~", "!=", ">=", "<=", "||", "&&",
			"<<", ">>" };
	

	// dv语言的关键字
	public static final String[] Keywords = { "var" , "function" , "if" , "else" , "for" , "while" , "do" , "switch" , "case" , "default" , "break" , "continue" , "return"};
	
	// 关键字的初始下标，结束下标
	static final int 
	FirstKeyword = 0,
	LastKeyword = 12;
	
	// 符号的初始下标，结束下标
	static final int 
	FirstSymbol = 100,
	LastSymbol = 135;

	// 操作符的初始下标，结束下标
	static final int 
	FitstOperator = 115,
	LastOperator = 135;
	
	
	/**  
	 * @return  是否为操作符
	 * @Description:  
	 */
	boolean isOperator() {
		return type >= FitstOperator && type <= LastOperator;
	}
	
	// 常量的类型的初始下标，结束下标
	static final int
    FirstLiteral = 200,
    LastLiteral  = 299;

	// 常量的类型
	private static final String[] Literals = {
		      Util.getMessage ("Token.boolLit"),
		      Util.getMessage ("Token.charLit"),
		      Util.getMessage ("Token.intLit"),
		      Util.getMessage ("Token.floatLit"),
		      Util.getMessage ("Token.stringLit"),
		      Util.getMessage ("Token.literal")};
	
	/**  
	 * @return  是否为常量
	 * @Description:  
	 */
	boolean isLiterals() {
		return type >= FirstLiteral && type <= LastLiteral;
	}
	
	/**
	 * Code identifying the lexical class to which this token belongs, e.g.,
	 * Keyword, Identifier, ... 2016年6月14日 下午8:24:18
	 */
	int type;

	/**  
	 *   变量名
	 */
	String name;
	
	/**  
	 *   该行语句的注释
	 */
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

	/**  
	 * @param string
	 * @param escapedOK	超出关键字的范围
	 * @param collision	是否为关键字
	 * @return  
	 * @Description:  获得关键字的Token
	 */
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
