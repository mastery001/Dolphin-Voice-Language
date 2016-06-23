package dv.entry;

/**
 * 符号表
 * 
 * @author zouziwen
 *
 *         2016年6月15日 下午2:41:14
 */
public class SymtabEntry {

	private String _name;
	
	/**
	 * 值的类型
	 * 2016年6月17日 下午12:26:53
	 */
	private SymtabEntry _type;
	
	private String _typeName;

	private IncludeEntry _sourceFile;
	
	private String _comment;

	/**
	 * @return the name of this entry.
	 */
	public String name() {
		return _name;
	} // name

	/**
	 * Set the name.
	 * 
	 * @param newName
	 *            the new name.
	 */
	public void name(String newName) {
		if (newName == null)
			_name = "";
		else
			_name = newName;
	} // name

	public IncludeEntry sourceFile() {
		return _sourceFile;
	}

	public void sourceFile(IncludeEntry file) {
		_sourceFile = file;
	}
	
	public String comment() {
		return _comment;
	}
	
	public void comment(String comment) {
		_comment = comment;
	}

	public SymtabEntry type() {
		return _type;
	}
	
	public void type(SymtabEntry type) {
		_type = type;
	}
	
	public String typeName() {
		return _typeName;
	}
	
	public void typeName(String typeName) {
		_typeName = typeName;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()  + "[_name=" + _name  + ";_type:[" + _type + "]]";
	}
}
