package dv.entry;  
  
  
/**  
 *@Description:  Java对象关联实体
 *@Author:zouziwen
 *@Since:2017年3月11日  
 *@Version:1.1.0  
 */
public class JavaObjectAssociationEntry extends SymtabEntry{
	
	private final String javaClassName;
	
	private final String dvObjectName;

	public JavaObjectAssociationEntry(String javaClassName, String dvObjectName) {
		super();
		this.javaClassName = javaClassName;
		this.dvObjectName = dvObjectName;
	}

	public String javaClassName() {
		return javaClassName;
	}

	public String dvObjectName() {
		return dvObjectName;
	}

	@Override
	public String toString() {
		return "JavaObjectAssociationEntry [javaClassName=" + javaClassName + ", dvObjectName=" + dvObjectName + "]";
	}
	
}
