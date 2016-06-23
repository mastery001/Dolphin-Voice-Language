package dv;

import dv.entry.SymtabEntry;

public interface Interpreter {
	
	void interprete(InterpreterChain chain , SymtabEntry entry , ExecutionContext context) throws InterpreteException;
	
	Class<? extends SymtabEntry> entryClass();
	
}
