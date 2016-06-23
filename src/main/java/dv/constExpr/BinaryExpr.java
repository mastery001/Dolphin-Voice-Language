package dv.constExpr;

/**
 * 二元表达式
 * 
 * @author zouziwen
 *
 *         2016年6月21日 下午8:03:13
 */
public abstract class BinaryExpr extends Expression {

	public BinaryExpr(String operation, Expression leftOperand, Expression rightOperand) {
		_op = operation;
		_left = leftOperand;
		_right = rightOperand;
	} // ctor

	public void op(String op) {
		_op = (op == null) ? "" : op;
	}

	public String op() {
		return _op;
	}

	public void left(Expression left) {
		_left = left;
	}

	public Expression left() {
		return _left;
	}

	public void right(Expression right) {
		_right = right;
	}

	public Expression right() {
		return _right;
	}

	private String _op = "";
	private Expression _left = null;
	private Expression _right = null;
}
