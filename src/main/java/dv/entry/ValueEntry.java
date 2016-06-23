package dv.entry;

public class ValueEntry extends SymtabEntry {

	private Object _value;
	
	/**
	 * 值类型
	 * 2016年6月17日 下午1:12:02
	 */
	private int _varType;
	
	public int varType() {
		return _varType;
	}
	
	public void varType(int type) {
		_varType = type;
	}

	public ValueEntry() {

	}

	public Object value() {
		return _value;
	}

	public void value(Object value) {
		_value = value;
	}

	@Override
	public String toString() {
		return "_value=" + _value ;
	}
	
	
}
