package dv.entry;

import java.util.Vector;

/**
 * 通过+连接的实体
 * @author zouziwen
 *
 * 2016年6月20日 下午2:57:19
 */
public class JoinEntry extends SymtabEntry{
	
	private Vector<SymtabEntry> entries = new Vector<SymtabEntry>();

	public void addEntry(SymtabEntry entry) {
		entries.addElement(entry);
	}

	public Vector<SymtabEntry> entries() {
		return entries;
	}
}
