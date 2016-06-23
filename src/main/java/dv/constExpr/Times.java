package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class Times extends BinaryExpr {

	public Times(Expression leftOperand, Expression rightOperand) {
		super("*", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Number l = (Number) left().evaluate();
			Number r = (Number) right().evaluate();

			boolean lIsNonInteger = l instanceof Float || l instanceof Double;
			boolean rIsNonInteger = r instanceof Float || r instanceof Double;

			if (lIsNonInteger && rIsNonInteger)
				value(new Double(l.doubleValue() * r.doubleValue()));
			else if (lIsNonInteger || rIsNonInteger) {
				String[] parameters = { Util.getMessage("EvaluationException.times"),
						left().value().getClass().getName(), right().value().getClass().getName() };
				throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
			} else {
				BigInteger tmpL = (BigInteger) l, tmpR = (BigInteger) r;
				value(tmpL.multiply(tmpR));
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.times"), left().value().getClass().getName(),
					right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
