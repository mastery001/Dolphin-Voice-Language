package dv;

import dv.entry.FunctionEntry;
import dv.entry.SymtabEntry;
import dv.entry.VariableEntry;
import dv.interpreter.ModuleInterpreter;

  
/**  
 *@Description:  解析执行器，逐行解释运行
 *@Author:zouziwen
 *@Since:2017年3月4日  
 *@Version:1.1.0  
 *@see ModuleInterpreter
 */
public interface Interpreter {
	
	void interprete(InterpreterChain chain , SymtabEntry entry , ExecutionContext context) throws InterpreteException;
	
	/**  
	 * @return  
	 * @Description:  当前解释的实体
	 * @see ModuleEntry
	 * @see FunctionEntry
	 * @see VariableEntry
	 */
	Class<? extends SymtabEntry> entryClass();
	
}
