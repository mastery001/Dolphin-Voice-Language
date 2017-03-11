package dv.interpreter;

import java.util.Iterator;
import java.util.Vector;

import dv.ExecutionContext;
import dv.Function;
import dv.InterpreteException;
import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.JoinEntry;
import dv.entry.SymtabEntry;
import dv.entry.ValueEntry;
import dv.entry.VariableEntry;
import dv.interpreter.FunctionInterpreter.DvFunction;
import dv.interpreter.VariableInterpreter.FunctionVariable;
import dv.toJava.RuntimeJavaFunction;

class InterpreterUtils {

	static final String JAVA_PREFIX = "java@";
	
	/**
	 * @param context
	 * @param e
	 * @return
	 * @throws Exception
	 * @Description: 翻译实体对应的值
	 */
	static Object valueOf(ExecutionContext context, SymtabEntry e) throws Exception {
		if (e instanceof JoinEntry) {
			StringBuffer sb = new StringBuffer();
			for (Iterator<SymtabEntry> it = ((JoinEntry) e).entries().iterator(); it.hasNext();) {
				SymtabEntry e1 = it.next();
				if (e1 instanceof ValueEntry) {
					sb.append(((ValueEntry) e1).value());
				} else {
					Object obj = valueOf(context, e1);
					sb.append(obj);
				}
			}
			return sb.toString();
		}else if (e instanceof ValueEntry) {
			return ((ValueEntry) e).value();
		}else if(e instanceof VariableEntry) {
			// VariableEntry
			Variable variable = context.variableOf(e.name());
			if (variable == null) {
				return null;
			}

			if (variable instanceof FunctionVariable) {
				FunctionEntry fe = (FunctionEntry) variable.value();
				return functionOf(context, fe).invoke();
			}
			return variable.value();
		}else {
			return functionOf(context, (FunctionEntry) e).invoke();
		}
	}
	
	static Function functionOf(ExecutionContext context, FunctionEntry entry) throws Exception {
		String name = entry.name();

		Function function = context.functionOf(name);
		
		if (isJavaAssociation(name)) {
			function = InterpreterUtils.getJavaFunction(context.root(), name);
		}
		
		// 如果查找函数不存在，则认为不是注册的Java自带方法
		if (function == null) {
			Variable variable = context.variableOf(name);
			if(variable instanceof FunctionVariable) {
				FunctionEntry fe = (FunctionEntry) variable.value();
				function = context.functionOf(fe.name());
			}else {
				function = new DvFunction(entry, context);
			}
			context.addFunction(function);
		} else {
			if (function instanceof JavaFunction) {
				JavaFunction javaFunction = (JavaFunction) function;
				javaFunction.set(context, entry);
			} else if (function instanceof DvFunction) {
				// 函数声明的实体
				Vector<SymtabEntry> declaredParams = function.entry().parameters();
				Vector<SymtabEntry> invokeParams = entry.parameters();
				SymtabEntry declaredParamEntry = null;
				for (int i = 0; i < declaredParams.size(); i++) {
					declaredParamEntry = declaredParams.get(i);
					SymtabEntry invoke = invokeParams.get(i);
					if (invoke instanceof VariableEntry) {
						Variable variable = context.variableOf(invoke.name());
						declaredParamEntry.type(new ValueEntry(variable.value()));
					} else {
						// 赋值
						declaredParamEntry.type(invokeParams.get(i));
					}
				}
			}
		}
		return function;
	}

	static Function getJavaFunction(ExecutionContext context, String name) {
		String[] names = name.split("->");
		String functionName = JAVA_PREFIX + names[0];
		Function function = context.functionOf(functionName);
		if(function == null) {
			Variable variable = context.variableOf(names[0]);
			if (variable == null) {
				throw new InterpreteException(names[0] + " undefine");
			}
			function = new RuntimeJavaFunction(functionName, variable.value() , names[1]);
			context.addFunction(function);
		}
		return function;
	}

	static boolean isJavaAssociation(String name) {
		return name.matches("[a-zA-Z0-9_.]+->[a-zA-Z0-9_.]+");
	}
}
