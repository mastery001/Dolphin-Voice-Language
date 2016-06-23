package dv;

import java.util.HashMap;
import java.util.Map;

import dv.entry.SymtabEntry;

/**
 * 解释链
 * @author zouziwen
 *
 * 2016年6月19日 下午3:24:24
 */
public class InterpreterChain implements Interpreter{

	private Map<String , Interpreter> interpreters = new HashMap<String , Interpreter>();
	
	public InterpreterChain addInterpreter(Interpreter interpreter) {
		if(interpreter != null) {
			interpreters.put(interpreter.entryClass().getSimpleName(), interpreter);
		}
		return this;
	}

	@Override
	public Class<? extends SymtabEntry> entryClass() {
		return null;
	}

	@Override
	public void interprete(InterpreterChain chain, SymtabEntry entry, ExecutionContext context)  throws InterpreteException{
		Interpreter ipt = interpreters.get(entry.getClass().getSimpleName());
		if(ipt == null) {
			System.out.println (Util.getMessage ("Interpreter.Error", entry.name ()));
		}else {
			ipt.interprete(chain , entry, context);
		}
	}

}
