package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class And extends BinaryExpr {

	public And(Expression leftOperand, Expression rightOperand) {
		super("&", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Number l = (Number) left().evaluate();
			Number r = (Number) right().evaluate();

			if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double) {
				String[] parameters = { Util.getMessage("EvaluationException.and"), left().value().getClass().getName(),
						right().value().getClass().getName() };
				throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
			} else {
				//BigInteger uL = (BigInteger) coerceToTarget((BigInteger) l);
				//BigInteger uR = (BigInteger) coerceToTarget((BigInteger) r);
				BigInteger tmpL = (BigInteger) l, tmpR = (BigInteger) r;
				value(tmpL.and(tmpR));
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.and"), left().value().getClass().getName(),
					right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
