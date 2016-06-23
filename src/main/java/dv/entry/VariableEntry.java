package dv.entry;

/**
 * 变量实体
 * @author zouziwen
 *
 * 2016年6月17日 下午12:55:14
 */
public class VariableEntry extends SymtabEntry {
	
	private int _scope;
	
	public int scope() {
		return _scope;
	}
	
	public void scope(int scope) {
		_scope = scope;
	}
	
	 public static final int 
	 		 GLOBAL    = 0,
             PART = 1;

}
