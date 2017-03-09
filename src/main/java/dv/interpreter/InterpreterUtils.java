package dv.interpreter;

import dv.Variable;
import dv.entry.FunctionEntry;
import dv.entry.JoinEntry;
import dv.entry.SymtabEntry;
import dv.entry.ValueEntry;
import dv.interpreter.VariableInterpreter.FunctionVariable;
import dv.interpreter.VariableInterpreter.JavaVariable;
import dv.utils.TypeUtils;

public class InterpreterUtils {

	static Variable variableOf(SymtabEntry entry) {
		SymtabEntry valueType = entry.type();
		return variableOf(entry, valueType);
	}

	static Variable variableOf(SymtabEntry entry, SymtabEntry valueType) {
		Variable variable = null;
		if (valueType instanceof ValueEntry) {
			ValueEntry valueEntry = (ValueEntry) valueType;
			variable = new JavaVariable(entry.name(), TypeUtils.typeOf(valueEntry.varType()), valueEntry.value());
		} else if (valueType instanceof FunctionEntry) {
			FunctionEntry functionEntry = (FunctionEntry) valueType;
			variable = new FunctionVariable(entry.name() , functionEntry);
		}else if(valueType instanceof JoinEntry) {
			JoinEntry join = (JoinEntry) valueType;
			if(join.entries().size() == 1) {
				return variableOf(entry , join.entries().get(0));
			}
 		}
		return variable;
	}
}	
