package dv.constExpr;

import dv.entry.ExpressionEntry;

public class Terminal extends Expression{

	protected Terminal(String representation , Object value) {
		rep(representation);
		value(value);
	}
	
	protected Terminal(ExpressionEntry expressionReference) {
		value(expressionReference);
	}
	
	
	@Override
	public Object evaluate() throws EvaluationException {
		if(value() instanceof ExpressionEntry) {
			return ((ExpressionEntry)value()).value().evaluate();
		}
		return value();
	}

}
