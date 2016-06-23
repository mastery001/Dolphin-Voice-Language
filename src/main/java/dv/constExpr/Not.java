package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class Not extends UnaryExpr {

	public Not(Expression unaryOperand) {
		super("~", unaryOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		try {
			Number op = (Number) operand().evaluate();

			if (op instanceof Float || op instanceof Double) {
				String[] parameters = { Util.getMessage("EvaluationException.not"),
						operand().value().getClass().getName() };
				throw new EvaluationException(Util.getMessage("EvaluationException.2", parameters));
			} else {
				BigInteger b = (BigInteger) op;
				value(b.not()); // Should never execute...
			}
		} catch (ClassCastException e) {
			String[] parameters = { Util.getMessage("EvaluationException.not"),
					operand().value().getClass().getName() };
			throw new EvaluationException(Util.getMessage("EvaluationException.2", parameters));
		}
		return value();
	}

}
