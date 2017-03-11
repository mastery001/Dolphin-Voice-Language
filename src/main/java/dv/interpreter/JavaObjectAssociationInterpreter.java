package dv.interpreter;

import dv.ExecutionContext;
import dv.InterpreteException;
import dv.InterpreterChain;
import dv.Variable;
import dv.entry.JavaObjectAssociationEntry;
import dv.entry.SymtabEntry;
import dv.interpreter.VariableInterpreter.JavaVariable;
import dv.utils.TypeUtils;

class JavaObjectAssociationInterpreter extends AbstractInterpreter<JavaObjectAssociationEntry> {

	@Override
	public Class<? extends SymtabEntry> entryClass() {
		return JavaObjectAssociationEntry.class;
	}

	@Override
	public void interprete0(InterpreterChain chain, JavaObjectAssociationEntry entry, ExecutionContext context)
			throws InterpreteException {
		try {
			Class<?> clazz = TypeUtils.typeOf(entry.javaClassName());
			Variable variable = new JavaVariable(entry.dvObjectName(), clazz, clazz.newInstance());
			context.addVariable(variable);;
		} catch (Exception e) {
			if(e instanceof InterpreteException) {
				throw (InterpreteException)e;
			}
			throw new InterpreteException(e);
		}
	}

}
