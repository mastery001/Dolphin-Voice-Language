package dv.constExpr;

public class DefaultExprFactory implements ExprFactory{

	@Override
	public And and(Expression left, Expression right) {
		return new And(left, right);
	}

	@Override
	public BooleanAnd booleanAnd(Expression left, Expression right) {
		return new BooleanAnd(left, right);
	}

	@Override
	public BooleanNot booleanNot(Expression operand) {
		return new BooleanNot(operand);
	}

	@Override
	public BooleanOr booleanOr(Expression left, Expression right) {
		return new BooleanOr(left, right);
	}

	@Override
	public Divide divide(Expression left, Expression right) {
		return new Divide(left, right);
	}

	@Override
	public Equal equal(Expression left, Expression right) {
		return new Equal(left, right);
	}

	@Override
	public GreaterEqual greaterEqual(Expression left, Expression right) {
		return new GreaterEqual(left, right);
	}

	@Override
	public GreaterThan greaterThan(Expression left, Expression right) {
		return new GreaterThan(left, right);
	}

	@Override
	public LessEqual lessEqual(Expression left, Expression right) {
		return new LessEqual(left, right);
	}

	@Override
	public LessThan lessThan(Expression left, Expression right) {
		return new LessThan(left, right);
	}

	@Override
	public Minus minus(Expression left, Expression right) {
		return new Minus(left, right);
	}

	@Override
	public Modulo modulo(Expression left, Expression right) {
		return new Modulo(left, right);
	}

	@Override
	public Not not(Expression operand) {
		return new Not(operand);
	}

	@Override
	public NotEqual notEqual(Expression left, Expression right) {
		return new NotEqual(left, right);
	}

	@Override
	public Or or(Expression left, Expression right) {
		return new Or(left, right);
	}

	@Override
	public Plus plus(Expression left, Expression right) {
		return new Plus(left, right);
	}

	@Override
	public ShiftLeft shiftLeft(Expression left, Expression right) {
		return new ShiftLeft(left, right);
	}

	@Override
	public ShiftRight shiftRight(Expression left, Expression right) {
		return new ShiftRight(left, right);
	}

	@Override
	public Terminal terminal(String representation, Object value) {
		return new Terminal(representation, value);
	}

	@Override
	public Xor xor(Expression left, Expression right) {
		return new Xor(left, right);
	}

	@Override
	public Positive positive(Expression e) {
		return new Positive(e);
	}

	@Override
	public Negative negative(Expression e) {
		return new Negative(e);
	}

	@Override
	public Times times(Expression left, Expression right) {
		return new Times(left, right);
	}

}
