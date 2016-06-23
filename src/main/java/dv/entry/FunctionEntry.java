package dv.entry;

import java.util.Vector;

/**
 * 一个函数相当于一个文件
 * @author zouziwen
 *
 * 2016年6月17日 下午4:35:50
 */
public class FunctionEntry extends ModuleEntry {

	private Vector<SymtabEntry> _parameters = new Vector<SymtabEntry>();

	public void addParameter(SymtabEntry parameter) {
		_parameters.addElement(parameter);
	}

	public Vector<SymtabEntry> parameters() {
		return _parameters;
	}

	private boolean _invoke;
	
	public boolean invoke() {
		return _invoke;
	}
	
	public void invoke(boolean invoke) {
		_invoke = invoke;
	}
	
	@Override
	public void type(SymtabEntry type) {
		super.type(type);
		if (type == null)
			typeName("void");
	}
	
	public void namedInvoke(String callerName) {
		name(String.format(namedInvokePattern, callerName));
	}

	@Override
	public String toString() {
		return super.toString() +";invoke=" + invoke() +  ";_parameters=" + _parameters;
	}
	
	static final String namedInvokePattern = "%s_invoke";
}
