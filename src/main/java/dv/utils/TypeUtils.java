package dv.utils;

import dv.Token;

public class TypeUtils {

	public static Class<?> typeOf(String type) throws ClassNotFoundException {
		if (type == null) {
			return Object.class;
		}
		if (type.indexOf("int") >= 0) {
			return Integer.class;
		} else if (type.indexOf("char") >= 0) {
			return Character.class;
		} else if (type.indexOf("byte") >= 0) {
			return Byte.class;
		} else if (type.indexOf("short") >= 0) {
			return Short.class;
		} else if (type.indexOf("double") >= 0) {
			return Double.class;
		} else if (type.indexOf("float") >= 0) {
			return Float.class;
		} else if (type.indexOf("long") >= 0) {
			return Long.class;
		} else if (type.indexOf("void") >= 0) {
			return Void.class;
		}
		return Class.forName(type);
	}
	
	public static Class<?> typeOf(int type) {
		Class<?> valueType = null;
		switch (type) {
		case Token.IntegerLiteral:
			valueType = Integer.class;
			break;
		case Token.StringLiteral:
			valueType = String.class;
			break;
		case Token.FloatingPointLiteral:
			valueType = Float.class;
			break;
		case Token.BooleanLiteral:
			valueType = Boolean.class;
			break;
		default:
			valueType = Object.class;
		}
		return valueType;
	}


}
