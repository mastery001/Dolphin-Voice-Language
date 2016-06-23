package dv.constExpr;

public interface ExprFactory {

	And and(Expression left, Expression right);

	BooleanAnd booleanAnd(Expression left, Expression right);

	BooleanNot booleanNot(Expression operand);

	BooleanOr booleanOr(Expression left, Expression right);

	Divide divide(Expression left, Expression right);

	Equal equal(Expression left, Expression right);

	GreaterEqual greaterEqual(Expression left, Expression right);

	GreaterThan greaterThan(Expression left, Expression right);

	LessEqual lessEqual(Expression left, Expression right);

	LessThan lessThan(Expression left, Expression right);

	Minus minus(Expression left, Expression right);

	Modulo modulo(Expression left, Expression right);

	Not not(Expression operand);

	NotEqual notEqual(Expression left, Expression right);

	Or or(Expression left, Expression right);

	Plus plus(Expression left, Expression right);

	ShiftLeft shiftLeft(Expression left, Expression right);

	ShiftRight shiftRight(Expression left, Expression right);

	Terminal terminal(String representation, Object value);
	
	Xor xor(Expression left, Expression right);

	Positive positive(Expression e);

	Negative negative(Expression e);	

	Times times(Expression left, Expression right);

}
