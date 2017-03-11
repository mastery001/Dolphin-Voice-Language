package dv.interpreter;

import java.util.Iterator;

import dv.ExecutionContext;
import dv.Function;
import dv.entry.FunctionEntry;
import dv.entry.SymtabEntry;

  
/**  
 *@Description:  Java方法的抽象执行
 *@Author:zouziwen
 *@Since:2016年6月13日  
 *@Version:1.1.0  
 */
public abstract class JavaFunction implements Function{

	private String name;
	
	private ExecutionContext context;
	
	private FunctionEntry entry;
	
	public JavaFunction(String name) {
		this.name = name;
	}
	
	@Override
	public ExecutionContext context() {
		return context;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public FunctionEntry entry() {
		return entry;
	}
	
	@Override
	public Object invoke() throws Exception {
		return invoke0(params());
	}

	private Object[] params() throws Exception {
		Object[] params = new Object[entry.parameters().size()];
		int index = 0;
		for(Iterator<SymtabEntry> it = entry.parameters().iterator(); it.hasNext();) {
			SymtabEntry e = it.next();
			params[index++] = paramOf(e);
		}
		return params;
	}

	protected abstract Object invoke0(Object[] params) throws Exception;

	public void set(ExecutionContext context , FunctionEntry entry){
		this.context = context;
		this.entry = entry;
	}

	private Object paramOf(SymtabEntry e) throws Exception {
		return InterpreterUtils.valueOf(context(), e);
	}

}
