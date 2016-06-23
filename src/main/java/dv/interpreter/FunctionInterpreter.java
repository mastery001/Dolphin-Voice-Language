package dv.interpreter;

import java.util.Enumeration;

import dv.ExecutionContext;
import dv.Function;
import dv.InterpreteException;
import dv.InterpreterChain;
import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.SymtabEntry;
import dv.interpreter.VariableInterpreter.FunctionVariable;
import dv.toJava.JavaFunction;

/**
 * 方法解释器
 * @author zouziwen
 *
 * 2016年6月19日 下午1:52:03
 */
class FunctionInterpreter extends AbstractInterpreter<FunctionEntry> {

	@Override
	public Class<FunctionEntry> entryClass() {
		return FunctionEntry.class;
	}

	@Override
	public void interprete0(InterpreterChain chain , FunctionEntry entry, ExecutionContext context)  throws InterpreteException{
		Function function = functionOf(chain , entry, context);
		// 函数需要调用时解释
		if(entry.invoke()) {
			function.invoke();
		}
	}

	private Function functionOf(InterpreterChain chain , FunctionEntry entry, ExecutionContext context) {
		String name = entry.name();
		Function function = context.functionOf(name);
		if(function == null) {
			Variable variable = context.variableOf(name);
			if(variable instanceof FunctionVariable) {
				FunctionEntry fe = (FunctionEntry) variable.value();
				function = context.functionOf(fe.name());
			}else {
				function = new DvFunction(entry, ExecutionContext.newContext(context) , chain);
			}
		}
		if(function instanceof JavaFunction) {
			JavaFunction javaFunction = (JavaFunction) function;
			javaFunction.set(context, entry.parameters());
		}
		context.addFunction(function);
		return function;
	}

	class DvFunction implements Function {

		FunctionEntry entry;
		
		// 当前函数执行上下文
		ExecutionContext context ;
		
		InterpreterChain chain;
		
		DvFunction(FunctionEntry entry , ExecutionContext context , InterpreterChain chain) {
			this.entry = entry;
			this.context = context;
			this.chain = chain;
		}

		@Override
		public ExecutionContext context() {
			return context;
		}

		@Override
		public String name() {
			return entry.name();
		}

		@Override
		public String toString() {
			return "DvFunction [name="+ name()+"---entry=" + entry + "]";
		}

		@Override
		public FunctionEntry entry() {
			return entry;
		}

		@Override
		public Object invoke() {
			Enumeration<SymtabEntry> containeds = entry.contained().elements();
			while (containeds.hasMoreElements()) {
				SymtabEntry symtabEntry = containeds.nextElement();
				chain.interprete(chain, symtabEntry, context());
			}
			return null;
		}
		
	}
	
}
