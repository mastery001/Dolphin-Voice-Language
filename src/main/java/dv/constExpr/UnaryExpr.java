package dv.constExpr;

public abstract class UnaryExpr extends Expression {
	public UnaryExpr(String operation, Expression unaryOperand) {
		_op = operation;
		_operand = unaryOperand;
	} // ctor

	public void op(String op) {
		_op = (op == null) ? "" : op;
	}

	public String op() {
		return _op;
	}

	public void operand(Expression operand) {
		_operand = operand;
	}

	public Expression operand() {
		return _operand;
	}

	private String _op = "";
	private Expression _operand = null;
} // class UnaryExpr
