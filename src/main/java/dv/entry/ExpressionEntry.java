package dv.entry;

import dv.constExpr.Expression;

/**
 * 表达式实体
 * @author zouziwen
 *
 * 2016年6月21日 下午3:57:13
 */
public class ExpressionEntry extends SymtabEntry{

	private Expression _value;
	
	public void value(Expression value) {
		_value = value;
	}
	
	public Expression value() {
		return _value;
	}
	
}
