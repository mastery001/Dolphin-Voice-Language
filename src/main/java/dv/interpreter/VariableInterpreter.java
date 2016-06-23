package dv.interpreter;

import java.util.Objects;

import dv.ExecutionContext;
import dv.InterpreteException;
import dv.InterpreterChain;
import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.SymtabEntry;
import dv.entry.ValueEntry;
import dv.entry.VariableEntry;
import dv.utils.TypeUtils;

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
		SymtabEntry valueType = entry.type();
		Variable variable = null;
		if (valueType instanceof ValueEntry) {
			ValueEntry valueEntry = (ValueEntry) valueType;
			variable = new JavaVariable(entry.name(), TypeUtils.typeOf(valueEntry.varType()), valueEntry.value());
		} else if (valueType instanceof FunctionEntry) {
			FunctionEntry functionEntry = (FunctionEntry) valueType;
			variable = new FunctionVariable(entry.name() , functionEntry);
		}
		context.addVariable(variable);
	}


	class JavaVariable implements Variable {

		String name;

		Class<?> type;

		Object value;

		JavaVariable(String name) {
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

	class FunctionVariable implements Variable {

		FunctionEntry function;
		
		String name;
		
		FunctionVariable(String name , FunctionEntry f) {
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
	
}
