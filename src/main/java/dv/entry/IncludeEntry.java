package dv.entry;

import java.util.Vector;

/**
 * 文件表
 * @author zouziwen
 *
 * 2016年6月15日 下午2:41:22
 */
public class IncludeEntry extends SymtabEntry{

	/** Absolute file name for .u file generation. */
	private String _absFilename = null;

	private Vector<IncludeEntry> includeList = new Vector<IncludeEntry>();

	public void absFilename(String afn) {
		_absFilename = afn;
	}

	public String absFilename() {
		return _absFilename;
	}

	/**
	 * Add an IncludeEntry to the list of files which this included file
	 * includes.
	 */
	public void addInclude(IncludeEntry entry) {
		includeList.addElement(entry);
	} // addInclude

	/** Get the list of files which this file includes. */
	public Vector<IncludeEntry> includes() {
		return includeList;
	} // includes
	
	
}
