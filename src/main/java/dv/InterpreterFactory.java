package dv;

import dv.interpreter.ModuleInterpreter;

/**
 * 解释器工厂
 * @author zouziwen
 *
 * 2016年6月19日 下午2:05:22
 */
public class InterpreterFactory {
	
	ModuleInterpreter createModuleInterpreter() {
		return new ModuleInterpreter();
	}
}
