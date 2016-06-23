package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class GreaterEqual extends BinaryExpr {

	public GreaterEqual(Expression leftOperand, Expression rightOperand) {
		super(">=", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Object left = left().evaluate();
			Object right = right().evaluate();
			if (left instanceof Boolean) {
				String[] parameters = { Util.getMessage("EvaluationException.greaterEqual"),
						left().value().getClass().getName(), right().value().getClass().getName() };
				throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
			} else {
				Number l = (Number) left;
				Number r = (Number) right;
				if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double)
					value(new Boolean(l.doubleValue() >= r.doubleValue()));
				else
					value(new Boolean(((BigInteger) l).compareTo((BigInteger) r) >= 0));
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.greaterEqual"),
					left().value().getClass().getName(), right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
