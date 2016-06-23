package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class Modulo extends BinaryExpr {

	public Modulo(Expression leftOperand, Expression rightOperand) {
		super("%", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Number l = (Number) left().evaluate();
			Number r = (Number) right().evaluate();

			if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double) {
				String[] parameters = { Util.getMessage("EvaluationException.mod"), left().value().getClass().getName(),
						right().value().getClass().getName() };
				throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
			} else {
				BigInteger tmpL = (BigInteger) l, tmpR = (BigInteger) r;
				value(tmpL.remainder(tmpR));
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.mod"), left().value().getClass().getName(),
					right().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.1", parameters));
		}
		return value();
	}

}
