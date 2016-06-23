package dv.interpreter;

import java.util.Objects;

import dv.ExecutionContext;
import dv.InterpreteException;
import dv.Interpreter;
import dv.InterpreterChain;
import dv.entry.SymtabEntry;

public abstract class AbstractInterpreter<T extends SymtabEntry> implements Interpreter{

	@SuppressWarnings("unchecked")
	@Override
	public void interprete(InterpreterChain chain , SymtabEntry entry, ExecutionContext context) throws InterpreteException{
		Objects.requireNonNull(entry);
		Objects.requireNonNull(context);
		if(entry.getClass().getSimpleName().equals(entryClass().getSimpleName())) {
			interprete0(chain , (T) entry , context);
		}
	}
	
	public abstract void interprete0(InterpreterChain chain  , T entry, ExecutionContext context) throws InterpreteException;

}
