package dv.toJava;

import java.util.Iterator;
import java.util.Vector;

import dv.ExecutionContext;
import dv.Function;
import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.JoinEntry;
import dv.entry.SymtabEntry;
import dv.entry.ValueEntry;
import dv.entry.VariableEntry;

public abstract class JavaFunction implements Function{

	private String name;
	
	private ExecutionContext context;
	
	private Vector<SymtabEntry> _parameters;

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
		return null;
	}
	
	@Override
	public Object invoke() {
		Object obj = invoke0(params());
		return obj;
	}

	private Vector<Object> params() {
		Vector<Object> params = new Vector<Object>();
		for(Iterator<SymtabEntry> it = _parameters.iterator(); it.hasNext();) {
			SymtabEntry e = it.next();
			params.add(paramOf(e));
		}
		return params;
	}

	protected abstract Object invoke0(Vector<Object> params);

	public void set(ExecutionContext context , Vector<SymtabEntry> _parameters){
		this.context = context;
		this._parameters = _parameters;
	}

	private Object paramOf(SymtabEntry e) {
		if(e instanceof JoinEntry) {
			StringBuffer sb = new StringBuffer();
			for(Iterator<SymtabEntry> it = ((JoinEntry) e).entries().iterator(); it.hasNext();) {
				SymtabEntry e1 = it.next();
				if(e1 instanceof ValueEntry) {
					sb.append(((ValueEntry) e1).value());
				}else {
					Object obj = paramOf(e1);
					sb.append(obj);
				}
			}
			return sb.toString();
		}else if(e instanceof VariableEntry) {
			Variable variable = context().variableOf(e.name());
			return variable == null ? null :variable.value();
		}else {
			Function function = context().functionOf(e.name());
			return function == null ? null :function.invoke();
		}
	}

}
