package dv.interpreter;

import java.util.Enumeration;

import dv.ExecutionContext;
import dv.Interpreter;
import dv.InterpreterChain;
import dv.entry.ModuleEntry;
import dv.entry.SymtabEntry;

public class ModuleInterpreter extends AbstractInterpreter<ModuleEntry> {

	private final InterpreterChain chain = new InterpreterChain();

	public ModuleInterpreter() {
		this(true);
	}
	
	public ModuleInterpreter(boolean init) {
		if(init) {
			addInterpreter(new FunctionInterpreter());
			addInterpreter(new VariableInterpreter());
		}
	}

	public void interprete0(InterpreterChain chain, ModuleEntry entry, ExecutionContext context) {
		Enumeration<SymtabEntry> containeds = entry.contained().elements();
		while (containeds.hasMoreElements()) {
			SymtabEntry symtabEntry = containeds.nextElement();
			chain.interprete(chain, symtabEntry, context);
		}
	}

	public ModuleInterpreter addInterpreter(Interpreter interpreter) {
		chain.addInterpreter(interpreter);
		return this;
	}
	
	public InterpreterChain chain() {
		return chain;
	}

	@Override
	public Class<ModuleEntry> entryClass() {
		return ModuleEntry.class;
	}

}
