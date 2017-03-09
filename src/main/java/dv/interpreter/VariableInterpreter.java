package dv.interpreter;

import java.util.Objects;

import dv.ExecutionContext;
import dv.InterpreteException;
import dv.InterpreterChain;
import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.VariableEntry;

/**
 * 变量解释器
 * 
 * @author zouziwen
 *
 *         2016年6月13日 下午6:16:38
 */
class VariableInterpreter extends AbstractInterpreter<VariableEntry> {

	@Override
	public Class<VariableEntry> entryClass() {
		return VariableEntry.class;
	}

	@Override
	public void interprete0(InterpreterChain chain, VariableEntry entry, ExecutionContext context)  throws InterpreteException{
		context.addVariable(InterpreterUtils.variableOf(entry));
	}
	
	/**  
	 *@Description:  方法型变量
	 *@Author:zouziwen
	 *@Since:2017年3月9日  
	 *@Version:1.1.0  
	 */
	static class FunctionVariable implements Variable {

		FunctionEntry function;

		String name;

		public FunctionVariable(String name, FunctionEntry f) {
			this.name = name;
			function = f;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public Object value() {
			return function;
		}

		@Override
		public Class<?> type() {
			return FunctionEntry.class;
		}

	}
	
	/**  
	 *@Description:  Java变量
	 *@Author:zouziwen
	 *@Since:2017年3月9日  
	 *@Version:1.1.0  
	 */
	static class JavaVariable implements Variable {

		String name;

		Class<?> type;

		Object value;

		public JavaVariable(String name) {
			this(name, null, null);
		}

		public JavaVariable(String name, Class<?> type, Object value) {
			Objects.requireNonNull(name);
			this.name = name;
			this.type = type;
			this.value = value;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public Object value() {
			return value;
		}

		@Override
		public Class<?> type() {
			return type;
		}

		@Override
		public String toString() {
			return "JavaVariable [name=" + name + ", type=" + type + ", value=" + value + "]";
		}

	}

}
