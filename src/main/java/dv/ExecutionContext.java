package dv;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行上下文
 * @author zouziwen
 *
 * 2016年6月13日 下午2:48:54
 */
public class ExecutionContext{

	private ExecutionContext parent;
	
	private Map<String, Variable> variables = new HashMap<String, Variable>();
	private Map<String, Function> functions = new HashMap<String, Function>();

	public static ExecutionContext newContext() {
		return newContext(null);
	}
	
	public static ExecutionContext newContext(ExecutionContext parent) {
		return new ExecutionContext(parent);
	}
	
	private ExecutionContext(ExecutionContext parent) {
		this.parent = parent;
	}

	public void addVariable(Variable variable) {
		if (variable != null) {
			synchronized (variables) {
				variables.put(variable.name(), variable);
			}
		}
	}

	public void removeVariable(String name) {
		if(name != null) {
			synchronized (variables) {
				variables.remove(name);
			}
		}
	}
	
	public Variable variableOf(String name) {
		synchronized (variables) {
			Variable variable  = variables.get(name);
			ExecutionContext tmp = this;
			// 从上级的上下文查询
			while(variable == null && tmp != tmp.parent()) { 
				tmp = tmp.parent();
				variable  = tmp.variables.get(name);
			}
			return variable;
		}
	}

	public Collection<Variable> variables() {
		return variables.values();
	}

	public void addFunction(Function function) {
		if (function != null) {
			synchronized (functions) {
				functions.put(function.name(), function);
			}
		}
	}

	public Function functionOf(String name) {
		synchronized (functions) {
			Function function  = functions.get(name);
			ExecutionContext tmp = this;
			// 从上级的上下文查询
			while(function == null && tmp != tmp.parent()) { 
				tmp = tmp.parent();
				function  = tmp.functions.get(name);
			}
			return function;
		}
	}

	public Collection<Function> functions() {
		return functions.values();
	}

	public void removeFunction(String name) {
		if(name != null) {
			synchronized (functions) {
				functions.remove(name);
			}
		}
	}
	
	public ExecutionContext parent() {
		if(parent == null) {
			return this;
		}
		return parent;
	}
	
}
