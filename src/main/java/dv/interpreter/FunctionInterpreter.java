package dv.interpreter;

import java.util.Enumeration;

import dv.ExecutionContext;
import dv.Function;
import dv.InterpreteException;
import dv.InterpreterChain;
import dv.entry.FunctionEntry;
import dv.entry.SymtabEntry;

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
		try {
			Function function = InterpreterUtils.functionOf(context , entry);
			// 函数需要调用时解释
			if(entry.invoke()) {
				function.invoke();
			}
		} catch (Exception e) {
			if(e instanceof InterpreteException) {
				throw (InterpreteException)e;
			}
			throw new InterpreteException(e);
		}
	}
	
	/**  
	 *@Description:  dv语言提供的方法
	 *@Author:zouziwen
	 *@Since:2017年3月9日  
	 *@Version:1.1.0  
	 */
	static class DvFunction implements Function {

		FunctionEntry entry;

		// 当前函数执行上下文
		ExecutionContext context;

		InterpreterChain chain;

		public DvFunction(FunctionEntry entry, ExecutionContext context) {
			this.entry = entry;
			this.context = context;
			this.chain = context.interpreterChain();
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
			return "DvFunction [name=" + name() + "---entry=" + entry + "]";
		}

		@Override
		public FunctionEntry entry() {
			return entry;
		}

		@Override
		public Object invoke() throws Exception {
			Enumeration<SymtabEntry> containeds = entry.contained().elements();
			while (containeds.hasMoreElements()) {
				SymtabEntry symtabEntry = containeds.nextElement();
				chain.interprete(chain, symtabEntry, context());
			}
			SymtabEntry returnEntry = entry.returnEntry();
			if(returnEntry != null) {
				return InterpreterUtils.valueOf(context, returnEntry);
			}
			return null;
		}

	}
}
