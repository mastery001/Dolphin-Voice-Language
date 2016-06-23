package dv.entry;

import java.util.Vector;

/**
 * 一个文件统称为一个组件
 * 
 * @author zouziwen
 *
 *         2016年6月17日 上午10:48:24
 */
public class ModuleEntry extends SymtabEntry {

	/**
	 * 组件下存放的元素，只能是：VariableEntry , FunctionEntry <br>
	 * {@link VariableEntry} <br>
	 * {@link FunctionEntry}
	 * 2016年6月17日 上午10:49:34
	 */
	private Vector<SymtabEntry> _contained = new Vector<SymtabEntry>();

	public void addContained(SymtabEntry entry) {
		_contained.addElement(entry);
	}

	public Vector<SymtabEntry> contained() {
		return _contained;
	}
}
