package dv.toJava;

import java.util.Vector;

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
