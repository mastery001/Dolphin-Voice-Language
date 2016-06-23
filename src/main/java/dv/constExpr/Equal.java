package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class Equal extends BinaryExpr {

	public Equal(Expression leftOperand, Expression rightOperand) {
		super("==", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Object left = left().evaluate();
			if (left instanceof Boolean) {
				Boolean l = (Boolean) left;
				Boolean r = (Boolean) right().evaluate();
				value(new Boolean(l.booleanValue() == r.booleanValue()));
			} else {
				Number l = (Number) left;
				Number r = (Number) right().evaluate();
				if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double)
					value(new Boolean(l.doubleValue() == r.doubleValue()));
				else
					value(new Boolean(((BigInteger) l).equals((BigInteger) r)));
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.equal"), left().value().getClass().getName(),
					right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
