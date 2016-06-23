package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class BooleanAnd extends BinaryExpr {

	public BooleanAnd(Expression leftOperand, Expression rightOperand) {
		super("&&", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Object tmpL = left().evaluate();
			Object tmpR = right().evaluate();
			Boolean l;
			Boolean r;

			if (tmpL instanceof Number) {
				if (tmpL instanceof BigInteger)
					l = new Boolean(((BigInteger) tmpL).compareTo(BigInteger.valueOf(0)) != 0);
				else
					l = new Boolean(((Number) tmpL).longValue() != 0);
			} else
				l = (Boolean) tmpL;
			if (tmpR instanceof Number) {
				if (tmpR instanceof BigInteger)
					r = new Boolean(((BigInteger) tmpR).compareTo(zero) != 0);
				else
					r = new Boolean(((Number) tmpR).longValue() != 0);
			} else
				r = (Boolean) tmpR;

			value(new Boolean(l.booleanValue() && r.booleanValue()));
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.booleanAnd"),
					left().value().getClass().getName(), right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
