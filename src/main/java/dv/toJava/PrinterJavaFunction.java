package dv.toJava;

import java.util.Vector;

  
/**  
 *@Description:  println方法的具体执行，实际调用为System.out.println();
 *@Author:zouziwen
 *@Since:2017年3月4日  
 *@Version:1.1.0  
 */
public class PrinterJavaFunction extends JavaFunction{

	public PrinterJavaFunction() {
		super("println");
	}

	@Override
	protected Object invoke0(Vector<Object> params) {
		System.out.println(params.get(0));
		return null;
	}

}
