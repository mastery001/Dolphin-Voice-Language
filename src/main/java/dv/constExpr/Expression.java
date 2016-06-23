package dv.constExpr;

import java.math.BigInteger;

public abstract class Expression {

	public abstract Object evaluate() throws EvaluationException;

	private Object _value = null;
	/**
	 * String representation of this expression.
	 **/
	private String _rep = null;

	public void value(Object value) {
		_value = value;
	}

	public Object value() {
		return _value;
	}

	public void rep(String rep) {
		_rep = rep;
	}

	public String rep() {
		return _rep;
	}
	
	public static final BigInteger zero   = BigInteger.valueOf (0);
}
