package dv;

import dv.entry.FunctionEntry;

/**
 * 函数
 * @author zouziwen
 *
 * 2016年6月13日 下午3:02:35
 */
public interface Function{

	/**
	 * 该方法的执行上下文
	 * @return
	 * 2016年6月13日 下午3:02:25
	 */
	ExecutionContext context();
	
	/**
	 * 方法名称
	 * @return
	 * 2016年6月19日 下午7:35:06
	 */
	String name();
	
	FunctionEntry entry();
	
	Object invoke();
}
